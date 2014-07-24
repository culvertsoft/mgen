/*
 * BinaryReader.h
 *
 *  Created on: 3 mar 2014
 *      Author: GiGurra
 */

#ifndef MGEN_MGENBINARYREADER_H_
#define MGEN_MGENBINARYREADER_H_

#include "mgen/serialization/VarInt.h"
#include "mgen/serialization/BinaryTags.h"
#include "mgen/util/BuiltInSerializerUtil.h"
#include "mgen/util/missingfields.h"

/*********************************************
 *
 *
 *              HELPER MACROS
 *
 ********************************************/

#define READ_OBJ_HEADER(retCall) \
    const int nIdsOrFields = readSize(); \
    if (nIdsOrFields == 0) retCall; \
    const bool hasIds = (nIdsOrFields & 0x01) != 0; \
    const int nIds = hasIds ? (nIdsOrFields >> 2) : 0; \
    std::vector<short> ids(nIds); \
    for (int i = 0; i < nIds; i++) \
        read(ids[i], false); \
    const int nFields = hasIds ? readSize() : (nIdsOrFields >> 2);

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

    BinaryReader(MGenStreamType& inputStream, const ClassRegistryType& classRegistry, const bool excessiveTypeChecking =
            false) :
                    m_inputStream(inputStream),
                    m_classRegistry(classRegistry),
                    m_excessiveTypeChecking(excessiveTypeChecking) {
    }

    MGenBase * readObject() {
        return readPoly(true, false, -1);
    }

    template<typename MGenType>
    MGenType * readObject() {
        return (MGenType*) readPoly(true, true, MGenType::_type_id);
    }

    template<typename MGenType>
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

    MGenBase * readPoly(const bool verifyTag, const bool constrained, const long long expectTypeId) {

        verifyReadTagIf(BINARY_TAG_CUSTOM, verifyTag);

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

    void skipList(const bool tag) {
        if (tag)
            verifyReadTagIf(BINARY_TAG_LIST, tag);
        const int sz = readSize();
        if (sz > 0) {
            const BINARY_TAG tag = readTag();
            for (int i = 0; i < sz; i++)
                skip(tag);
        }
    }

    void skipMap(const bool tag) {
        if (tag)
            verifyReadTagIf(BINARY_TAG_MAP, tag);
        const int sz = readSize();
        if (sz > 0) {
            skipList(true);
            skipList(true);
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

    template<typename T>
    void skip(const bool checkTag) {
        T out;
        read(out, checkTag);
    }
    
#define SKIP_CASE_READ(tag, skipcall) case tag: {skipcall; break;}
    void skip(const BINARY_TAG tag) {
        switch (tag) {
        SKIP_CASE_READ(BINARY_TAG_BOOL, skip<bool>(false))
        SKIP_CASE_READ(BINARY_TAG_INT8, skip<char>(false))
        SKIP_CASE_READ(BINARY_TAG_INT16, skip<short>(false))
        SKIP_CASE_READ(BINARY_TAG_INT32, skip<int>(false))
        SKIP_CASE_READ(BINARY_TAG_INT64, skip<long long>(false))
        SKIP_CASE_READ(BINARY_TAG_FLOAT32, skip<float>(false))
        SKIP_CASE_READ(BINARY_TAG_FLOAT64, skip<double>(false))
        SKIP_CASE_READ(BINARY_TAG_STRING, skip<std::string>(false))
        SKIP_CASE_READ(BINARY_TAG_LIST, skipList(false))
        SKIP_CASE_READ(BINARY_TAG_MAP, skipMap(false))
        SKIP_CASE_READ(BINARY_TAG_CUSTOM, skipCustom())
        default:
            throw UnexpectedTypeException("BinaryReader::skipField(..): Unexpected tag");
        }
    }
#undef SKIP_CASE_READ

    template<typename T>
    void read(std::vector<T>& v, const bool verifyTag) {
        verifyReadTagIf(BINARY_TAG_LIST, verifyTag);
        const int sz = readSize();
        if (sz > 0) {
            verifyReadTagIf(BINARY_TAG_OF((T*) 0), true);
            v.resize(sz);
            for (int i = 0; i < sz; i++)
                read(v[i], false);
        }
    }

    template<typename K, typename V>
    void read(std::map<K, V>& v, const bool verifyTag) {
        verifyReadTagIf(BINARY_TAG_MAP, verifyTag);
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
        verifyReadTagIf(BINARY_TAG_CUSTOM, verifyTag);
        v.set((T*) readPoly(false, true, T::_type_id));
    }

    template<typename EnumType>
    void read(EnumType& e, const int /* type_evidence */, const bool verifyTag) {
        std::string str;
        read(str, verifyTag);
        e = get_enum_value(e, str);
    }

    template<typename MGenType>
    void read(MGenType& object, const MGenBase& /* type_evidence */, const bool verifyTag) {
        verifyReadTagIf(BINARY_TAG_CUSTOM, verifyTag);
        READ_OBJ_HEADER({
            mgen::missingfields::ensureNoMissingFields(object);
            return;
        });
        if (m_excessiveTypeChecking)
            serialutil::checkExpType(m_classRegistry, &object, ids);
        readFields(object, nFields);
    }

    template<typename MGenTypeOrEnum>
    void read(MGenTypeOrEnum& v, const bool verifyTag) {
        read(v, v, verifyTag);
    }

    void read(bool& v, const bool verifyTag) {
        verifyReadTagIf(BINARY_TAG_BOOL, verifyTag);
        v = readByte() != 0;
    }

    void read(char & v, const bool verifyTag) {
        verifyReadTagIf(BINARY_TAG_INT8, verifyTag);
        v = readByte();
    }

    void read(short & v, const bool verifyTag) {
        verifyReadTagIf(BINARY_TAG_INT16, verifyTag);
		v = read16();
    }

    void read(int& v, const bool verifyTag) {
        verifyReadTagIf(BINARY_TAG_INT32, verifyTag);
        v = readSignedVarInt32();
    }

    void read(long long& v, const bool verifyTag) {
        verifyReadTagIf(BINARY_TAG_INT64, verifyTag);
        v = readSignedVarInt64();
    }

    void read(float & v, const bool verifyTag) {
        verifyReadTagIf(BINARY_TAG_FLOAT32, verifyTag);
        reinterpret_cast<unsigned int&>(v) = read32();
    }

    void read(double & v, const bool verifyTag) {
        verifyReadTagIf(BINARY_TAG_FLOAT64, verifyTag);
        reinterpret_cast<unsigned long long&>(v) = read64();
    }
	
    unsigned short read16() {
	
        unsigned char bytes[2];
        m_inputStream.read(bytes, 2);
		
        return
            ( ((unsigned short) bytes[0]) << 8) |
            ( ((unsigned short) bytes[1]) << 0);
    }

    unsigned int read32() {
	
        unsigned char bytes[4];
        m_inputStream.read(bytes, 4);
		
        return
            ( ((unsigned int) bytes[0]) << 24) |
            ( ((unsigned int) bytes[1]) << 16) |
            ( ((unsigned int) bytes[2]) << 8) |
            ( ((unsigned int) bytes[3]) << 0);
    }
	
    unsigned long long read64() {
	
        unsigned char src[8];
        m_inputStream.read(src, 8);
		
        return
            ( ((unsigned long long) src[0]) << 56) | 
            ( ((unsigned long long) src[1]) << 48) |
            ( ((unsigned long long) src[2]) << 40) | 
            ( ((unsigned long long) src[3]) << 32) | 
            ( ((unsigned long long) src[4]) << 24) | 
            ( ((unsigned long long) src[5]) << 16) |
            ( ((unsigned long long) src[6]) << 8) | 
            ( ((unsigned long long) src[7]) << 0);
    }
	
    void read(std::string& v, const bool verifyTag) {
        verifyReadTagIf(BINARY_TAG_STRING, verifyTag);
        const int sz = readSize();
        if (sz == 0) {
            v = "";
        } else {
            v.resize(sz);
            m_inputStream.read(&v[0], sz);
        }
    }

    void verifyReadTagIf(const BINARY_TAG expTag, const bool check) {
        if (check) {
            const BINARY_TAG tag = readTag();
            if (tag != expTag) {
                throw UnexpectedTypeException(
                        toString("BinaryReader::verifyReadTagIf: Unexpected tag ").append(toString(expTag)).append(
                                " but got ").append(toString(tag)));
            }
        }
    }

    BINARY_TAG readTag() {
        return (BINARY_TAG) readByte();
    }

    short readFieldId() {
        return read16();
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

    MGenStreamType& m_inputStream;
    const ClassRegistryType& m_classRegistry;
    const bool m_excessiveTypeChecking;

};

} /* namespace mgen */

#undef READ_OBJ_HEADER

#endif /* MGEN_MGENBINARYREADER_H_ */

