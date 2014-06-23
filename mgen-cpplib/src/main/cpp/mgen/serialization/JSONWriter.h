/*
 * JSONWriter.h
 *
 *  Created on: 29 mar 2014
 *      Author: GiGurra
 */

#ifndef JSONWRITER_H_
#define JSONWRITER_H_

#include "mgen/classes/MGenBase.h"
#include "mgen/serialization/VarInt.h"
#include "mgen/exceptions/SerializationException.h"
#include "mgen/util/missingfields.h"
#include "mgen/ext/rapidjson/writer.h"

namespace mgen {
namespace internal {

template<typename OutputStreamType>
class JSONOutStream {
public:
    JSONOutStream(OutputStreamType& stream) : m_stream(stream) {}
    void Put(const char c) { m_stream.write(&c, 1); }

private:
    OutputStreamType& m_stream;
};

} /* namespace internal */

template<typename Stream, typename Registry>
class JSONWriter {
public:

    JSONWriter(Stream& outputStream, const Registry& classRegistry) :
                    m_outputStream(outputStream),
                    m_jsonStream(m_outputStream),
                    m_rapidJsonWriter(m_jsonStream),
                    m_classRegistry(classRegistry) {
    }

    void writeMgenObject(const MGenBase& object) {
        writePoly(object);
    }

    template<typename T>
    void visit(const T& v, const Field& field, const bool isSet) {
        if (isSet) {
            m_rapidJsonWriter.String(field.name().c_str());
            write(v);
        }
    }

    template<typename MGenType>
    void beginVisit(const MGenType& object, const int nFieldsSet, const int nFieldsTotal) {

        if (!object._validate(SHALLOW)) {

            const std::string missingFieldsString = missingfields::requiredAsString(object);

            throw SerializationException(
                    std::string("mgen::JSONWriter::write(..) failed: Missing required fields ").append(
                            missingFieldsString).append(" for object of type: ").append(
                            object._typeName()));
        }

        const std::vector<std::string>& types = object._typeIds16BitBase64();

        m_rapidJsonWriter.StartObject();
        m_rapidJsonWriter.String("__t");
        m_rapidJsonWriter.StartArray();
        for (std::size_t i = 0; i < types.size(); i++)
            m_rapidJsonWriter.String(types[i].c_str());
        m_rapidJsonWriter.EndArray();

    }

    void endVisit() {
        m_rapidJsonWriter.EndObject();
    }

private:

    void writePoly(const MGenBase& v) {
        m_classRegistry.visitObject(v, *this);
    }

    template<typename MGenType>
    void write(const MGenType& v) {
        v._accept(*this);
    }

    template<typename T>
    void write(const std::vector<T>& v) {
        m_rapidJsonWriter.StartArray();
        for (std::size_t i = 0; i < v.size(); i++)
            write(v[i]);
        m_rapidJsonWriter.EndArray();
    }

    template<typename K, typename V>
    void write(const std::map<K, V>& v) {
        m_rapidJsonWriter.StartObject();
        for (typename std::map<K, V>::const_iterator it = v.begin(); it != v.end(); it++) {
            write(it->first);
            write(it->second);
        }
        m_rapidJsonWriter.EndObject();
    }

    template<typename T>
    void write(const Polymorphic<T>& v) {
        if (v.get()) {
            writePoly(*v);
        } else {
            m_rapidJsonWriter.Null();
        }
    }

    void write(const bool v) { m_rapidJsonWriter.Bool(v); }
    void write(const char v) { m_rapidJsonWriter.Int(v); }
    void write(const short v) { m_rapidJsonWriter.Int(v); }
    void write(const int v) { m_rapidJsonWriter.Int(v); }
    void write(const long long v) { m_rapidJsonWriter.Int64(v); }
    void write(const float v) { m_rapidJsonWriter.Double(v); }
    void write(const double v) { m_rapidJsonWriter.Double(v); }
    void write(const std::string& v) { m_rapidJsonWriter.String(v.c_str()); }

    Stream& m_outputStream;
    internal::JSONOutStream<Stream> m_jsonStream;
    rapidjson::Writer<internal::JSONOutStream<Stream> > m_rapidJsonWriter;
    const Registry& m_classRegistry;
};

template<typename Stream, typename Registry>
inline JSONWriter<Stream, Registry> * new_JSONWriter(
        Stream& outputStream,
        const Registry& classRegistry) {
    return new JSONWriter<Stream, Registry>(outputStream, classRegistry);
}

} /* namespace mgen */

#endif /* JSONWRITER_H_ */
