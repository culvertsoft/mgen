/*
 * BinaryWriter.h
 *
 *  Created on: 3 mar 2014
 *      Author: GiGurra
 */

#ifndef MGEN_MGENBINARYWRITER_H_
#define MGEN_MGENBINARYWRITER_H_

#include "mgen/serialization/VarInt.h"
#include "mgen/serialization/BinaryTags.h"
#include "mgen/util/missingfields.h"

namespace mgen {

/**
 * A class for writing data streams in the MGen binary wire format.
 * BinaryWriter objects are constructed with two type parameters: MGenStreamType
 * and ClassRegistryType. These specify which type of data output stream is to
 * be written to and what classes can be written, respectively.
 */
template<typename MGenStreamType, typename ClassRegistryType>
class BinaryWriter {
public:

    /**
     * All built-in MGen writers and wire-formats support what we call compact and
     * standard modes. In standard mode MGen objects are always prepended by a 
     * series of 16 bit ids which uniquely identify the class of the object being
     * written. Compact mode turns off writing of these IDs where they can be inferred
     * by the data model/reader (e.g. the field is of a specific object type and the object 
     * being written is of exactly this type - not a sub type).
     */
    static const bool default_compact = false;

    /**
     * Constructs a BinaryWriter around a provided data output stream and class registry.
     * A third optional parameter can be used to specify whether this writer should use 
     * compact or standard mode (see 'default_compact').
     */
    BinaryWriter(
            MGenStreamType& outputStream, 
            const ClassRegistryType& classRegistry, 
            const bool compact = default_compact) :
                    m_compact(compact),
                    m_expectType(-1),
                    m_outputStream(outputStream),
                    m_classRegistry(classRegistry) {
    }

    /**
     * Primary interface method for writing MGen objects to this writer. The class of the
     * object must be registered in class registry provided to this BinaryWriter when it was
     * constructed (this is always the case unless you have multiple data models in parallel).
     */
    void writeObject(const MGenBase& object) {
        m_expectType = -1;
        writePoly(object, true);
    }

    /**
     * When this writer is visiting an object it should write, this method will be called
     * before starting to visit any fields. The purpose is mainly to let this writer know
     * how many fields will follow, so that this value can be written to the output stream.
     */
    template<typename MGenType>
    void beginVisit(const MGenType& object, const int nFieldsToVisit) {

        missingfields::ensureNoMissingFields(object);

        if (shouldOmitIds(MGenType::_type_id)) {
            writeSize((nFieldsToVisit << 2) | 0x02);
        } else {
            const std::vector<short>& ids = MGenType::_type_ids_16bit();
            writeSize((int(ids.size()) << 2) | 0x01);
            for (std::size_t i = 0; i < ids.size(); i++)
                write(ids[i], false);
            writeSize(nFieldsToVisit);
        }

    }

    /**
     * When this writer is visiting an object it should write, this method will be called
     * for each selected object field to be written.
     */
    template<typename T>
    void visit(const T& v, const Field& field) {
        writeFieldStart(field.id(), BINARY_TAG_OF(&v));
        write(v, false);
    }
    
    /**
     * Method called when all selected fields of an object have been visited. In this writer
     * implementation, it doesn't do anything, but writers for other wire formats may use this method.
     */
    void endVisit() {
    }

private:

    /**
     * Internal method for writing a polymorphic MGen object.
     */
    void writePoly(const MGenBase& v, const bool doTag) {
        writeTagIf(BINARY_TAG_CUSTOM, doTag);
        m_classRegistry.visitObject(v, *this, mgen::ALL_SET_NONTRANSIENT);
    }

    /**
     * Internal method for writing an MGen object of statically known class.
     */
    template<typename MGenType>
    void write(const MGenType& v, const MGenBase&, const bool doTag) {
        m_expectType = MGenType::_type_id;
        writeTagIf(BINARY_TAG_CUSTOM, doTag);
        v._accept(*this, ALL_SET_NONTRANSIENT);
    }

    /**
     * Internal method for writing an enum.
     */
    template<typename EnumType>
    void write(const EnumType v, const int, const bool doTag) {
        write(get_enum_name(v), doTag);
    }

    /**
     * Internal method for writing an enum or MGen object.
     */
    template<typename MGenTypeOrEnum>
    void write(const MGenTypeOrEnum& v, const bool doTag) {
        write(v, v, doTag);
    }

    /**
     * Internal method for writing a polymorphic MGen object.
     */
    template<typename MGenType>
    void write(const Polymorphic<MGenType>& v, const bool doTag) {
        if (v.get()) {
            m_expectType = MGenType::_type_id;
            writePoly(*v, doTag);
        } else {
            writeTagIf(BINARY_TAG_CUSTOM, doTag);
            writeSize(0);
        }
    }

    /**
     * Internal method for writing a list.
     */
    template<typename T>
    void write(const std::vector<T>& v, const bool doTag) {
        writeTagIf(BINARY_TAG_LIST, doTag);
        writeSize(v.size());
        if (!v.empty()) {
            writeTagIf(BINARY_TAG_OF((T*) 0), true);
            for (std::size_t i = 0; i < v.size(); i++)
                write(v[i], false);
        }
    }

    /**
     * Internal method for writing a map.
     */
    template<typename K, typename V>
    void write(const std::map<K, V>& v, const bool doTag) {
        writeTagIf(BINARY_TAG_MAP, doTag);
        writeSize(v.size());
        if (!v.empty()) {

            writeTagIf(BINARY_TAG_OF((K*) 0), true);
            writeTagIf(BINARY_TAG_OF((V*) 0), true);

            for (typename std::map<K, V>::const_iterator it = v.begin(); it != v.end(); it++) {
                write(it->first, false);
                write(it->second, false);
            }

        }
    }

    /**
     * Internal method for writing a bool.
     */
    void write(const bool v, const bool doTag) {
        writeTagIf(BINARY_TAG_BOOL, doTag);
        writeByte(v ? 0x01 : 0x00);
    }

    /**
     * Internal method for writing an int8.
     */
    void write(const char v, const bool doTag) {
        writeTagIf(BINARY_TAG_INT8, doTag);
        writeByte(v);
    }

    /**
     * Internal method for writing an int16.
     */
    void write(const short v, const bool doTag) {
        writeTagIf(BINARY_TAG_INT16, doTag);
        write16(reinterpret_cast<const unsigned short&>(v));
    }

    /**
     * Internal method for writing an int32.
     */
    void write(const int v, const bool doTag) {
        writeTagIf(BINARY_TAG_INT32, doTag);
        writeSignedVarint32(v);
    }

    /**
     * Internal method for writing an int64.
     */
    void write(const long long v, const bool doTag) {
        writeTagIf(BINARY_TAG_INT64, doTag);
        writeSignedVarint64(v);
    }

    /**
     * Internal method for writing a float32.
     */
    void write(const float v, const bool doTag) {
        writeTagIf(BINARY_TAG_FLOAT32, doTag);
        write32(reinterpret_cast<const unsigned int&>(v));
    }

    /**
     * Internal method for writing a float64.
     */
    void write(const double v, const bool doTag) {
        writeTagIf(BINARY_TAG_FLOAT64, doTag);
        write64(reinterpret_cast<const unsigned long long&>(v));
    }

    /**
     * Internal method for writing a string.
     */
    void write(const std::string& v, const bool doTag) {
        writeTagIf(BINARY_TAG_STRING, doTag);
        writeSize(v.size());
        if (!v.empty())
            m_outputStream.write(v.data(), v.size());
    }

    /**
     * Internal method for writing a field id and type tag.
     */
    void writeFieldStart(const short fieldId, const char tag) {
        write(fieldId, false);
        writeTypeTag(tag);
    }

    /**
     * Internal method for writing type tag if a given condition is true.
     */
    void writeTagIf(const BINARY_TAG tagValue, const bool doTag) {
        if (doTag)
            writeTypeTag(tagValue);
    }

    /**
     * Internal method for writing type tag.
     */
    void writeTypeTag(const char tag) {
        write(tag, false);
    }

    /**
     * Internal method for writing a size value.
     */
    void writeSize(const int sz) {
        writeUnsignedVarint32(sz);
    }

    /**
     * Internal method for writing an unsigned varint32.
     */
    void writeUnsignedVarint32(const unsigned int v) {
        varint::writeUnsigned32(v, m_outputStream);
    }

    /**
     * Internal method for writing a signed varint32.
     */
    void writeSignedVarint32(const int v) {
        varint::writeSigned32(v, m_outputStream);
    }

    /**
     * Internal method for writing a signed varint64.
     */
    void writeSignedVarint64(const long long v) {
        varint::writeSigned64(v, m_outputStream);
    }

    /**
     * Internal method for writing a byte (int8).
     */
    void writeByte(const char c) {
        m_outputStream.write(&c, 1);
    }

    /**
     * Internal method for writing a fixed length big-endian int16.
     */
    void write16(const unsigned short data) {
        unsigned char buf[2];
        buf[0] = (unsigned char)(data >> 8);
        buf[1] = (unsigned char)(data >> 0);
        m_outputStream.write(buf, 2);
    }

    /**
     * Internal method for writing a fixed length big-endian int32.
     */
    void write32(const unsigned int data) {
        unsigned char buf[4];
        buf[0] = (unsigned char)(data >> 24);
        buf[1] = (unsigned char)(data >> 16);
        buf[2] = (unsigned char)(data >> 8);
        buf[3] = (unsigned char)(data >> 0);
        m_outputStream.write(buf, 4);
    }

    /**
     * Internal method for writing a fixed length big-endian int64.
     */
    void write64(const unsigned long long data) {
        unsigned char buf[8];
        buf[0] = (unsigned char)(data >> 56);
        buf[1] = (unsigned char)(data >> 48);
        buf[2] = (unsigned char)(data >> 40);
        buf[3] = (unsigned char)(data >> 32);
        buf[4] = (unsigned char)(data >> 24);
        buf[5] = (unsigned char)(data >> 16);
        buf[6] = (unsigned char)(data >> 8);
        buf[7] = (unsigned char)(data >> 0);
        m_outputStream.write(buf, 8);
    }

    /**
     * Internal convenience method for testing if we need to write type id
     * metadata for the object which is currently to be written.
     */
    bool shouldOmitIds(const long long expId) {
        return m_compact && expId == m_expectType;
    }

    const bool m_compact;
    long long m_expectType;
    MGenStreamType& m_outputStream;
    const ClassRegistryType& m_classRegistry;
};

template<typename MGenStreamType, typename ClassRegistryType>
inline BinaryWriter<MGenStreamType, ClassRegistryType> make_BinaryWriter(
		MGenStreamType& stream,
		const ClassRegistryType& classRegistry,
		const bool compact = false) {
	return BinaryWriter<MGenStreamType, ClassRegistryType>(stream, classRegistry, compact);
}

} /* namespace mgen */

#endif /* MGEN_MGENBINARYWRITER_H_ */
