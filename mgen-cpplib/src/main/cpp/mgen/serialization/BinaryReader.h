/*
 * BinaryReader.h
 *
 *  Created on: 3 mar 2014
 *      Author: GiGurra
 */

#ifndef MGENBINARYREADER_H_
#define MGENBINARYREADER_H_

#include "mgen/serialization/VarInt.h"
#include "mgen/classes/ClassRegistryBase.h"
#include "mgen/util/missingfields.h"
#include "mgen/util/BuiltInSerializerUtil.h"

/*********************************************
 *
 *
 *              HELPER MACROS
 *
 ********************************************/

#define S(x) toString(x)

#define READ_OBJ_HEADER(retCall) \
    const int nIdsOrFields = readSize(); \
    if (nIdsOrFields == 0) retCall; \
    const bool hasIds = (nIdsOrFields & 0x01) != 0; \
    const int nIds = hasIds ? (nIdsOrFields >> 1) : 0; \
    std::vector<short> ids(nIds); \
    for (int i = 0; i < nIds; i++) \
        read(ids[i], false); \
    const int nFields = hasIds ? readSize() : (nIdsOrFields >> 1);

/*********************************************
 *
 *
 *                  IMPL
 *
 ********************************************/

namespace mgen {

template<typename MGenStreamType, typename ClassRegistryType>
class BinaryReader {
public:

    BinaryReader(
            MGenStreamType& inputStream,
            const ClassRegistryType& classRegistry,
            const bool excessiveTypeChecking = false) :
                    m_inputStream(inputStream),
                    m_classRegistry(classRegistry),
                    m_excessiveTypeChecking(excessiveTypeChecking) {
    }

    MGenBase * readObject() {
        return readPoly(true, false, -1);
    }

    template <typename MGenType>
    MGenType * readObject() {
        return (MGenType*) readPoly(true, true, MGenType::_type_id);
    }

    template <typename MGenType>
    MGenType readStatic() {
        MGenType out;
        read(out, true);
        return out;
    }

    template<typename T>
    void readField(const Field& /*field*/, const int /*context*/, T& v) {
        read(v, true);
    }

    void handleUnknownField(const short /*fieldId*/, const int /*context*/) {
        skip(readTag());
    }

    template<typename ClassType>
    void readFields(ClassType& object, const int nFields) {
        for (int i = 0; i < nFields; i++) {
            const short fieldId = readFieldId();
            object._readField(fieldId, fieldId, *this);
        }
        mgen::missingfields::ensureNoMissingFields(object);
    }

private:

    MGenBase * readPoly(
            const bool verifyTag,
            const bool constrained,
            const long long expectTypeId) {

        verifyReadTagIf(Type::TAG_CUSTOM, verifyTag);

        READ_OBJ_HEADER(return 0);

        const ClassRegistryEntry * entry = serialutil::getCompatibleEntry(
                m_classRegistry,
                ids,
                constrained,
                expectTypeId);

        if (entry) {
            return serialutil::readObjInternal(*this, m_classRegistry, nFields, 0, *entry);
        } else {
            skipFields(nFields);
            return 0;
        }

    }

    void skipList() {
        const int sz = readSize();
        if (sz > 0) {
            const Type::TAG tag = readTag();
            for (int i = 0; i < sz; i++)
                skip(tag);
        }
    }

    void skipMap() {
        const int sz = readSize();
        if (sz > 0) {
            skipList();
            skipList();
        }
    }

    void skipFields(const int nFields) {
        for (int i = 0; i < nFields; i++) {
            readFieldId();
            skip(readTag());
        }
    }

    void skipCustom() {
        READ_OBJ_HEADER(return);
        skipFields(nFields);
    }

#define SKIP_CASE_READ(tag, skipcall) case tag: {skipcall; break;}
    void skip(const Type::TAG tag) {
        switch (tag) {
        SKIP_CASE_READ(Type::TAG_BOOL, read<bool>(false))
        SKIP_CASE_READ(Type::TAG_INT8, read<char>(false))
        SKIP_CASE_READ(Type::TAG_INT16, read<short>(false))
        SKIP_CASE_READ(Type::TAG_INT32, read<int>(false))
        SKIP_CASE_READ(Type::TAG_INT64, read<long long>(false))
        SKIP_CASE_READ(Type::TAG_FLOAT32, read<float>(false))
        SKIP_CASE_READ(Type::TAG_FLOAT64, read<double>(false))
        SKIP_CASE_READ(Type::TAG_STRING, read<std::string>(false))
        SKIP_CASE_READ(Type::TAG_LIST, skipList())
        SKIP_CASE_READ(Type::TAG_MAP, skipMap())
        SKIP_CASE_READ(Type::TAG_CUSTOM, skipCustom())
        default:
            throw UnexpectedTypeException("BinaryReader::skipField(..): Unexpected tag");
        }
    }
#undef SKIP_CASE_READ

    template<typename T>
    void read(std::vector<T>& v, const bool verifyTag) {
        static const Type::TAG elemTag = Type::TAG_OF(T());
        verifyReadTagIf(Type::TAG_OF(v), verifyTag);
        const int sz = readSize();
        if (sz > 0) {
            verifyReadTagIf(elemTag, true);
            v.resize(sz);
            for (int i = 0; i < sz; i++)
                read(v[i], false);
        }
    }

    template<typename K, typename V>
    void read(std::map<K, V>& v, const bool verifyTag) {
        verifyReadTagIf(Type::TAG_OF(v), verifyTag);
        const int sz = readSize();
        if (sz > 0) {
            std::vector<K> keys(sz);
            std::vector<V> values(sz);
            read(keys, true);
            read(values, true);
            for (int i = 0; i < sz; i++)
                v[keys[i]] = values[i];
        }
    }

    template<typename T>
    void read(Polymorphic<T>& v, const bool verifyTag) {
        verifyReadTagIf(Type::TAG_OF(v), verifyTag);
        v.set((T*) readPoly(false, true, T::_type_id));
    }

    template<typename MGenType>
    void read(MGenType& object, const bool verifyTag) {
        verifyReadTagIf(Type::TAG_OF(object), verifyTag);
        READ_OBJ_HEADER(return);
        if (m_excessiveTypeChecking)
            serialutil::checkExpType(m_classRegistry, &object, ids);
        readFields(object, nFields);
    }

    void read(bool& v, const bool verifyTag) {
        verifyReadTagIf(Type::TAG_OF(v), verifyTag);
        v = readRaw<char>() != 0;
    }

    void read(char & v, const bool verifyTag) {
        verifyReadTagIf(Type::TAG_OF(v), verifyTag);
        v = endian::ntoh(readRaw<char>());
    }

    void read(short & v, const bool verifyTag) {
        verifyReadTagIf(Type::TAG_OF(v), verifyTag);
        v = endian::ntoh(readRaw<short>());
    }

    void read(int& v, const bool verifyTag) {
        verifyReadTagIf(Type::TAG_OF(v), verifyTag);
        v = readSignedVarInt32();
    }

    void read(long long& v, const bool verifyTag) {
        verifyReadTagIf(Type::TAG_OF(v), verifyTag);
        v = readSignedVarInt64();
    }

    void read(float & v, const bool verifyTag) {
        verifyReadTagIf(Type::TAG_OF(v), verifyTag);
        v = endian::ntoh(readRaw<float>());
    }

    void read(double & v, const bool verifyTag) {
        verifyReadTagIf(Type::TAG_OF(v), verifyTag);
        v = endian::ntoh(readRaw<double>());
    }

    void read(std::string& v, const bool verifyTag) {
        verifyReadTagIf(Type::TAG_OF(v), verifyTag);
        const int sz = readSize();
        if (sz == 0) {
            v = "";
        } else {
            v.resize(sz);
            m_inputStream.read(&v[0], sz);
        }
    }

    template<typename T>
    T read(const bool checkTag) {
        T out;
        read(out, checkTag);
        return out;
    }

    void verifyReadTagIf(const Type::TAG expTag, const bool check) {
        if (check) {
            const Type::TAG tag = readTag();
            if (tag != expTag) {
                throw UnexpectedTypeException(
                        S("BinaryReader::verifyReadTagIf: Unexpected tag ").append(S(expTag)).append(
                                " but got ").append(S(tag)));
            }
        }
    }

    Type::TAG readTag() {
        return (Type::TAG) readByte();
    }

    short readFieldId() {
        return read<short>(false);
    }

    int readSignedVarInt32() {
        return varint::readSigned32(m_inputStream);
    }

    long long readSignedVarInt64() {
        return varint::readSigned64(m_inputStream);
    }

    int readSize() {
        const int out = varint::readUnsigned32(m_inputStream);
        if (out < 0)
            throw StreamCorruptedException("BinaryReader::readSize() < 0");
        return out;
    }

    char readByte() {
        char out;
        m_inputStream.read(&out, 1);
        return out;
    }

    template<typename T>
    T readRaw() {
        T out;
        m_inputStream.read(&out, sizeof(T));
        return out;
    }

    MGenStreamType& m_inputStream;
    const ClassRegistryType& m_classRegistry;
    const bool m_excessiveTypeChecking;

};

} /* namespace mgen */

#undef READ_OBJ_HEADER
#undef S

#endif /* MGENBINARYREADER_H_ */

