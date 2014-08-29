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

 /**
  * Convenience macro to encapsulate common logic for reading headers 
  * of MGen objects from MGen binary data streams.
  */
#define READ_MGEN_OBJ_HEADER(retCall) \
    const int nIdsOrFields = readSize(); \
    if (nIdsOrFields == 0) retCall; \
    const bool hasIds = (nIdsOrFields & 0x01) != 0; \
    const int nIds = hasIds ? (nIdsOrFields >> 2) : 0; \
    std::vector<short> ids(nIds); \
    for (int i = 0; i < nIds; i++) \
        read(ids[i], false); \
    const int nFields = hasIds ? readSize() : (nIdsOrFields >> 2);

namespace mgen {

/**
 * A class for reading data streams written in the MGen binary wire format.
 * BinaryReader objects are constructed with two type parameters: MGenStreamType
 * and ClassRegistryType. These specify which type of data input stream is to
 * be read from and what classes can be marshalled, respectively.
 */
template<typename MGenStreamType, typename ClassRegistryType>
class BinaryReader {
public:

    /**
     * Constructs a BinaryReader with the given a data input stream (data source)
     * and class registry. A third optional parameter can also be used to specify
     * if this reader should perform extra type checking for incoming data.
     * For situations where metadata on the wire is not actually needed, it will
     * normally be ignored. However with the extraTypeChecking parameter set to true,
     * redundant metadata will also be checked.
     */
    BinaryReader(
            MGenStreamType& inputStream, 
            const ClassRegistryType& classRegistry, 
            const bool extraTypeChecking = false) :
                m_inputStream(inputStream),
                m_classRegistry(classRegistry),
                m_extraTypeChecking(extraTypeChecking) {
    }

    /**
     * Primary interface method for reading polymorphic objects from streams. 
     * Returns a read back object (on the heap), or NULL if an object of unknown 
     * type was received (in which case it's just skipped past in the stream).
     */
    MGenBase * readObject() {
        return readPoly(true, false, -1);
    }

    /**
     * Similar to readObject(), this method also checks that the read back
     * object is of a certain type (or base type), as specified with the
     * template parameter.
     */
    template<typename MGenType>
    MGenType * readObject() {
        return (MGenType*) readPoly(true, true, MGenType::_type_id);
    }

    /**
     * Similar to readObject<MGenType>(), except that this method reads back 
     * the object directly to the stack and discards any potential subtype 
     * information.
     */
    template<typename MGenType>
    MGenType readStatic() {
        MGenType out;
        read(out, true);
        return out;
    }

    /**
     * Method called while reading the fields of an object from a data stream.
     * When reading an object from stream, fields may appear in any order depending
     * on the serialization format and also if the developer reorders the field
     * in an object between different code versions. This method is what connects
     * the field in the stream to the field on the object being read.
     */
    template<typename T>
    void readField(const Field& /*field*/, const int /*context*/, T& v) {
        read(v, true);
    }

    /**
     * When reading objects from a data stream, new fields may have been added 
     * to the data model without us having generated source code for it yet.
     * New/Unknown fields are simply discarded and skipped past in the stream.
     * This callback is meant to be overloaded should you want to deal with
     * unknown fields in a different manner.
     */
    void handleUnknownField(const short /*fieldId*/, const int /*context*/) {
        skip(readTag());
    }

    /**
     * Invoked dynamically through the generated ClassRegistry object. This method
     * is required to support reading of polymorphic objects from data streams.
     * The requirement comes from C++'s inability to natively support double
     * dynamic dispatch, which we get around by emitting our own 'vtables'
     * in generated ClassRegistry code.
     */
    template<typename ClassType>
    void readFields(ClassType& object, const int nFields) {
        for (int i = 0; i < nFields; i++) {
            const short fieldId = readFieldId();
            object._readField(fieldId, fieldId, *this);
        }
        mgen::missingfields::ensureNoMissingFields(object);
    }

private:

    /**
     * Internal method for reading a polymorphic object from a data stream.
     */
    MGenBase * readPoly(const bool verifyTag, const bool constrained, const long long expectTypeId) {

        verifyReadTagIf(BINARY_TAG_CUSTOM, verifyTag);

        READ_MGEN_OBJ_HEADER(return 0);

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

    /**
     * Internal method for skipping a list in a data stream.
     */
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

    /**
     * Internal method for skipping a map in a data stream.
     */
    void skipMap(const bool tag) {
        if (tag)
            verifyReadTagIf(BINARY_TAG_MAP, tag);
        const int sz = readSize();
        if (sz > 0) {

            const BINARY_TAG keyTag = readTag();
            const BINARY_TAG valueTag = readTag();

            for (int i = 0; i< sz; i++) {
                skip(keyTag);
                skip(valueTag);
            }

        }
    }

    /**
     * Internal method for skipping a number of fields in a data stream.
     */
    void skipFields(const int nFields) {
        for (int i = 0; i < nFields; i++) {
            readFieldId();
            skip(readTag());
        }
    }

    /**
     * Internal method for skipping an MGen object in a data stream.
     */
    void skipCustom() {
        READ_MGEN_OBJ_HEADER(return);
        skipFields(nFields);
    }

    /**
     * Internal method for skipping a value in a data stream.
     */
    template<typename T>
    void skip(const bool checkTag) {
        T out;
        read(out, checkTag);
    }
    
#define SKIP_CASE_READ(tag, skipcall) case tag: {skipcall; break;}
    /**
     * Internal method for skipping values in a data stream.
     */
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

    /**
     * Internal method for reading a list of values in a data stream.
     */
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

    /**
     * Internal method for reading a map of values in a data stream.
     */
    template<typename K, typename V>
    void read(std::map<K, V>& v, const bool verifyTag) {
        verifyReadTagIf(BINARY_TAG_MAP, verifyTag);

        const int sz = readSize();
		v.clear();
        if (sz > 0) {
            verifyReadTagIf(BINARY_TAG_OF((K*) 0), true);
            verifyReadTagIf(BINARY_TAG_OF((V*) 0), true);

            for (int i = 0; i < sz; i++) {
                K key;
                read(key, false);
                read(v[key], false);
            }

        }
    }

    /**
     * Internal method for reading a polymorphic object in a data stream.
     */
    template<typename T>
    void read(Polymorphic<T>& v, const bool verifyTag) {
        verifyReadTagIf(BINARY_TAG_CUSTOM, verifyTag);
        v.set((T*) readPoly(false, true, T::_type_id));
    }

    /**
     * Internal method for reading an enum in a data stream.
     */
    template<typename EnumType>
    void read(EnumType& e, const int /* type_evidence */, const bool verifyTag) {
        std::string str;
        read(str, verifyTag);
        e = get_enum_value(e, str);
    }

    /**
     * Internal method for reading an MGen object of a statically known type in a data stream.
     */
    template<typename MGenType>
    void read(MGenType& object, const MGenBase& /* type_evidence */, const bool verifyTag) {
        verifyReadTagIf(BINARY_TAG_CUSTOM, verifyTag);
        READ_MGEN_OBJ_HEADER({
            mgen::missingfields::ensureNoMissingFields(object);
            return;
        });
        if (m_extraTypeChecking)
            serialutil::checkExpType(m_classRegistry, &object, ids);
        readFields(object, nFields);
    }

    /**
     * Internal method for reading an MGen object or an enum in a data stream.
     */
    template<typename MGenTypeOrEnum>
    void read(MGenTypeOrEnum& v, const bool verifyTag) {
        read(v, v, verifyTag);
    }

    /**
     * Internal method for reading an bool in a data stream.
     */
    void read(bool& v, const bool verifyTag) {
        verifyReadTagIf(BINARY_TAG_BOOL, verifyTag);
        v = readByte() != 0;
    }

    /**
     * Internal method for reading an int8 in a data stream.
     */
    void read(char & v, const bool verifyTag) {
        verifyReadTagIf(BINARY_TAG_INT8, verifyTag);
        v = readByte();
    }

    /**
     * Internal method for reading an int16 in a data stream.
     */
    void read(short & v, const bool verifyTag) {
        verifyReadTagIf(BINARY_TAG_INT16, verifyTag);
		v = read16();
    }

    /**
     * Internal method for reading an int32 in a data stream.
     */
    void read(int& v, const bool verifyTag) {
        verifyReadTagIf(BINARY_TAG_INT32, verifyTag);
        v = readSignedVarInt32();
    }

    /**
     * Internal method for reading an int64 in a data stream.
     */
    void read(long long& v, const bool verifyTag) {
        verifyReadTagIf(BINARY_TAG_INT64, verifyTag);
        v = readSignedVarInt64();
    }

    /**
     * Internal method for reading a float32 in a data stream.
     */
    void read(float & v, const bool verifyTag) {
        verifyReadTagIf(BINARY_TAG_FLOAT32, verifyTag);
        reinterpret_cast<unsigned int&>(v) = read32();
    }

    /**
     * Internal method for reading a float64 in a data stream.
     */
    void read(double & v, const bool verifyTag) {
        verifyReadTagIf(BINARY_TAG_FLOAT64, verifyTag);
        reinterpret_cast<unsigned long long&>(v) = read64();
    }
	
    /**
     * Internal method for reading fixed length 16 bit big-endian integer in a data stream.
     */
    unsigned short read16() {
	
        unsigned char bytes[2];
        m_inputStream.read(bytes, 2);
		
        return
            ( ((unsigned short) bytes[0]) << 8) |
            ( ((unsigned short) bytes[1]) << 0);
    }

    /**
     * Internal method for reading fixed length 32 bit big-endian integer in a data stream.
     */
    unsigned int read32() {
	
        unsigned char bytes[4];
        m_inputStream.read(bytes, 4);
		
        return
            ( ((unsigned int) bytes[0]) << 24) |
            ( ((unsigned int) bytes[1]) << 16) |
            ( ((unsigned int) bytes[2]) << 8) |
            ( ((unsigned int) bytes[3]) << 0);
    }
	
    /**
     * Internal method for reading fixed length 64 bit big-endian integer in a data stream.
     */
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
	
    /**
     * Internal method for reading a string in a data stream.
     */
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

    /**
     * Internal method for verifying a type tag in a data stream.
     */
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

    /**
     * Internal method for reading a type tag in a data stream.
     */
    BINARY_TAG readTag() {
        return (BINARY_TAG) readByte();
    }

    /**
     * Internal method for reading a field id in a data stream.
     */
    short readFieldId() {
        return read16();
    }

    /**
     * Internal method for reading a signed 32 bit varint in a data stream.
     */
    int readSignedVarInt32() {
        return varint::readSigned32(m_inputStream);
    }

    /**
     * Internal method for reading a signed 64 bit varint in a data stream.
     */
    long long readSignedVarInt64() {
        return varint::readSigned64(m_inputStream);
    }

    /**
     * Internal method for reading a size value in a data stream.
     */
    int readSize() {
        const int out = varint::readUnsigned32(m_inputStream);
        if (out < 0)
            throw StreamCorruptedException("BinaryReader::readSize() < 0");
        return out;
    }

    /**
     * Internal convenience method for reading a byte (int8) value in a data stream.
     */
    char readByte() {
        char out;
        m_inputStream.read(&out, 1);
        return out;
    }

    MGenStreamType& m_inputStream;
    const ClassRegistryType& m_classRegistry;
    const bool m_extraTypeChecking;

};

} /* namespace mgen */

#undef READ_MGEN_OBJ_HEADER

#endif /* MGEN_MGENBINARYREADER_H_ */

