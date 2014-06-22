/*
 * BinaryReader.h
 *
 *  Created on: 3 mar 2014
 *      Author: GiGurra
 */

#ifndef MGENBINARYREADER_H_
#define MGENBINARYREADER_H_

#include "mgen/classes/MGenBase.h"
#include "mgen/serialization/VarInt.h"
#include "mgen/exceptions/StreamCorruptedException.h"
#include "mgen/classes/ClassRegistryBase.h"
#include "mgen/util/missingfields.h"

namespace mgen {

template<typename Stream, typename Registry>
class BinaryReader {
public:

    BinaryReader(Stream& inputStream, const Registry& classRegistry) :
            m_inputStream(inputStream), m_classRegistry(classRegistry) {

    }

    MGenBase * readMgenObject() {
        return readMgenObject(true);
    }

    template<typename T>
    void readField(const Field& /*field*/, const int /*context*/, T& v) {
        read(v, true);
    }

    void handleUnknownField(const short /*fieldId*/, const int /*context*/) {
        // TODO: Implement error handling for unknown fields
        skip(readTag());
    }

    template<typename ClassType>
    void readFields(ClassType& object, const int /*context*/) {

        const int nFields = readNumFields();

        for (int i = 0; i < nFields; i++) {
            const short fieldId = readFieldId();
            object._readField(fieldId, fieldId, *this);
        }

        if (!object._validate(SHALLOW)) {

            const std::string missingFieldsString = missingfields::requiredAsString(object);

            throw SerializationException(
                    std::string("mgen::Reader::readFields(..) failed: Missing required fields ").append(
                            missingFieldsString).append(" for object of type: ").append(
                            object._typeName()));
        }

    }

private:

    int readNumFields() {
        const int out = readSize();
        if (out < 0)
            throw StreamCorruptedException("BinaryReader::readNumFields: Unexpected field count(<0)");
        return out;
    }

    MGenBase * readMgenObject(
            const bool verifyTag,
            const bool constrained = false,
            const long long constraintTypeId = -1,
            MGenBase * object = 0) {

        if (!verifyTag || readTag() == Type::TAG_CUSTOM) {

            const ClassRegistryEntry * classRegistryEntry = readMGenBaseHeader();

            if (classRegistryEntry) {

                if (classRegistryEntry == mgen::ClassRegistryEntry::NULL_ENTRY())
                    return 0;

                if (constraintTypeId != -1 && !classRegistryEntry->isInstanceOfTypeId(constraintTypeId))
                    return 0;

                if (!object)
                    object = classRegistryEntry->newInstance();

                int ctx;
                m_classRegistry.readObjectFields(*object, ctx, *this);
                return object;

            } else {

                // TODO: Handle unknown type
                skipCustom(classRegistryEntry);

                return 0;
            }
        } else {
            throw StreamCorruptedException(
                    "BinaryReader::readMgenObject(): expected Type::TAG_CUSTOM");
        }
    }

    const ClassRegistryEntry * readMGenBaseHeader() {

        const int nTypeIds = readSize();

        if (nTypeIds > 0) {

            std::vector<short> typeIds16Bit(nTypeIds);
            for (int i = 0; i < nTypeIds; i++)
                read(typeIds16Bit[i], false);

            const ClassRegistryEntry * entry = m_classRegistry.getByTypeIds16Bit(typeIds16Bit);
            if (entry)
                return entry;

        } else if (nTypeIds < 0) {
            throw StreamCorruptedException(
                    "BinaryReader::readMGenBaseHeader(): Unexpected typeIds count (<0)");
        } else {
            return mgen::ClassRegistryEntry::NULL_ENTRY();
        }

        return 0;

    }

    void skipList() {
        const int sz = readSize();
        if (sz > 0) {
            const Type::TAG tag = readTag();
            for (int i = 0; i < sz; i++)
                skip(tag);
        } else if (sz < 0) {
            throw StreamCorruptedException(
                    "BinaryReader::skipList(..): Unexpected (<0) list length");
        }
    }

    void skipMap() {
        const int sz = readSize();
        if (sz > 0) {
            skipList();
            skipList();
        } else if (sz < 0) {
            throw StreamCorruptedException("BinaryReader::skipMap: unexpected (<0) size");
        }
    }

    void skipCustom(const ClassRegistryEntry * classRegistryEntry) {
        if (classRegistryEntry != ClassRegistryEntry::NULL_ENTRY()) {
            const int nFields = readNumFields();
            for (int i = 0; i < nFields; i++) {
                readFieldId();
                skip(readTag());
            }
        }
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
        SKIP_CASE_READ(Type::TAG_CUSTOM, skipCustom(readMGenBaseHeader()))
        default:
            throw StreamCorruptedException("BinaryReader::skipField(..): Unexpected tag");
        }
    }
#undef SKIP_CASE_READ

    template<typename T>
    void read(std::vector<T>& v, const bool verifyTag) {
        verifyReadTagIf(Type::TAG_OF(v), verifyTag);
        const int sz = readSize();
        if (sz > 0) {
            verifyReadTagIf(Type::TAG_OF(T()), true);
            v.resize(sz);
            for (int i = 0; i < sz; i++)
                read(v[i], false);
        } else if (sz < 0) {
            throw StreamCorruptedException(
                    "BinaryReader::skipList(..): Unexpected (<0) list length");
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
        } else if (sz < 0) {
            throw StreamCorruptedException(
                    "BinaryReader::skipList(..): Unexpected (<0) list length");
        }
    }

    template<typename T>
    void read(Polymorphic<T>& v, const bool verifyTag) {
        verifyReadTagIf(Type::TAG_OF(v), verifyTag);
        v.set((T*) readMgenObject(false, true, (short) T::_type_id));
    }

    void read(MGenBase& v, const bool verifyTag) {
        verifyReadTagIf(Type::TAG_OF(v), verifyTag);
        readMgenObject(false, true, v._typeId(), &v);
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
        if (sz < 0) {
            throw StreamCorruptedException("BinaryReader::read(string): String size < 0");
        } else if (sz == 0) {
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

    void verifyReadTagIf(const Type::TAG expTag, const bool doCheck) {
        if (doCheck) {
            if (readTag() != expTag) {
                // TODO: Implement error handling
                throw StreamCorruptedException("BinaryReader::readCheckTagIf: Unexpected tag");
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
        return varint::readUnsigned32(m_inputStream);
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

    Stream& m_inputStream;
    const Registry& m_classRegistry;

};

} /* namespace mgen */

#endif /* MGENBINARYREADER_H_ */
