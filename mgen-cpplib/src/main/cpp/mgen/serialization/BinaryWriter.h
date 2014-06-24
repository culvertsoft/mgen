/*
 * BinaryWriter.h
 *
 *  Created on: 3 mar 2014
 *      Author: GiGurra
 */

#ifndef MGENBINARYWRITER_H_
#define MGENBINARYWRITER_H_

#include "mgen/classes/MGenBase.h"
#include "mgen/serialization/VarInt.h"
#include "mgen/exceptions/SerializationException.h"
#include "mgen/util/missingfields.h"
#include "mgen/util/endian.h"

namespace mgen {

template<typename Stream, typename Registry>
class BinaryWriter {
public:

    BinaryWriter(Stream& outputStream, const Registry& classRegistry) :
            m_outputStream(outputStream), m_classRegistry(classRegistry) {
    }

    void writeMgenObject(const MGenBase& object) {
        writePoly(object, true);
    }

    template<typename T>
    void visit(const T& v, const Field& field, const bool isSet) {
        if (isSet) {
            writeFieldStart(field.id(), field.type().tag());
            write(v, false);
        }
    }

    template<typename MGenType>
    void beginVisit(const MGenType& object, const int nFieldsSet, const int nFieldsTotal) {

    	missingfields::ensureNoMissingFields(object);

        const std::vector<short>& typeIds = MGenType::_type_ids_16bit();
        writeSize(typeIds.size());
        for (std::size_t i = 0; i < typeIds.size(); i++)
            write(typeIds[i], false);
        writeSize(nFieldsSet);

    }

    void endVisit() {
    }

private:

    void writeFieldStart(const short fieldId, const char tag) {
        write(fieldId, false);
        writeTypeTag(tag);
    }

    void writePoly(const MGenBase& v, const bool doTag) {
        writeTagIf(Type::TAG_CUSTOM, doTag);
        m_classRegistry.visitObject(v, *this);
    }

    template<typename MGenType>
    void write(const MGenType& v, const bool doTag) {
        writeTagIf(Type::TAG_CUSTOM, doTag);
        v._accept(*this);
    }

    template<typename T>
    void write(const std::vector<T>& v, const bool verifyTag) {
        static const Type::TAG tag = Type::TAG_OF(T());
        writeTagIf(Type::TAG_LIST, verifyTag);
        writeSize(v.size());
        if (!v.empty()) {
            writeTagIf(tag, true);
            for (std::size_t i = 0; i < v.size(); i++)
                write(v[i], false);
        }
    }

    template<typename K, typename V>
    void write(const std::map<K, V>& v, const bool verifyTag) {
        writeTagIf(Type::TAG_MAP, verifyTag);
        writeSize(v.size());
        if (!v.empty()) {
            std::vector<K> keys(v.size());
            std::vector<V> values(v.size());
            int i = 0;
            for (typename std::map<K, V>::const_iterator it = v.begin(); it != v.end(); it++) {
                keys[i] = it->first;
                values[i] = it->second;
                i++;
            }
            write(keys, true);
            write(values, true);
        }
    }

    template<typename T>
    void write(const Polymorphic<T>& v, const bool doTag) {
        if (v.get()) {
            writePoly(*v, doTag);
        } else {
            writeTagIf(Type::TAG_CUSTOM, doTag);
            writeSize(0);
        }
    }

    void write(const bool v, const bool doTag) {
        writeTagIf(Type::TAG_BOOL, doTag);
        write(v ? 0x01 : 0x00, false);
    }

    void write(const char v, const bool doTag) {
        writeTagIf(Type::TAG_INT8, doTag);
        writeRaw(endian::hton(v));
    }

    void write(const short v, const bool doTag) {
        writeTagIf(Type::TAG_INT16, doTag);
        writeRaw(endian::hton(v));
    }

    void write(const int v, const bool doTag) {
        writeTagIf(Type::TAG_INT32, doTag);
        writeSignedVarint32(v);
    }

    void write(const long long v, const bool doTag) {
        writeTagIf(Type::TAG_INT64, doTag);
        writeSignedVarint64(v);
    }

    void write(const float v, const bool doTag) {
        writeTagIf(Type::TAG_FLOAT32, doTag);
        writeRaw(endian::hton(v));
    }

    void write(const double v, const bool doTag) {
        writeTagIf(Type::TAG_FLOAT64, doTag);
        writeRaw(endian::hton(v));
    }

    void write(const std::string& v, const bool doTag) {
        writeTagIf(Type::TAG_STRING, doTag);
        writeSize(v.size());
        if (!v.empty())
            m_outputStream.write(v.data(), v.size());
    }

    void writeTagIf(const Type::TAG tagValue, const bool doTag) {
        if (doTag)
            writeTypeTag(tagValue);
    }

    void writeTypeTag(const char tag) {
        write(tag, false);
    }

    void writeSize(const int sz) {
        writeUnsignedVarint32(sz);
    }

    void writeUnsignedVarint32(const unsigned int v) {
        varint::writeUnsigned32(v, m_outputStream);
    }

    void writeSignedVarint32(const int v) {
        varint::writeSigned32(v, m_outputStream);
    }

    void writeSignedVarint64(const long long v) {
        varint::writeSigned64(v, m_outputStream);
    }

    void writeByte(const char c) {
        m_outputStream.write(&c, 1);
    }

    template<typename T>
    void writeRaw(const T& v) {
        m_outputStream.write(&v, sizeof(T));
    }

    Stream& m_outputStream;
    const Registry& m_classRegistry;
};

} /* namespace mgen */

#endif /* MGENBINARYWRITER_H_ */
