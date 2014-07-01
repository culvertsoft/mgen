#ifndef JSON_WRITER_BASE_H_
#define JSON_WRITER_BASE_H_

#include "JsonOutputStream.h"
#include "mgen/util/missingfields.h"

namespace mgen {

template<typename MGenStreamType, typename ClassRegistryType, typename RapidJsonWriterType>
class JsonWriterBase {
public:

    static const bool default_compact = false;
    static const int default_max_depth = 256;

    JsonWriterBase(
            MGenStreamType& outputStream,
            const ClassRegistryType& classRegistry,
            const bool compact = default_compact,
            const int maxDepth = default_max_depth) :
                    m_compact(compact),
                    m_expectType(maxDepth),
                    m_depth(0),
                    m_outputStream(outputStream),
                    m_jsonStream(m_outputStream),
                    m_rapidJsonWriter(m_jsonStream),
                    m_classRegistry(classRegistry) {
    }

    void writeObject(const MGenBase& object) {
        m_depth = 0;
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
        static const std::string& idsString = MGenType::_type_ids_16bit_base64_string();

        missingfields::ensureNoMissingFields(object);

        m_rapidJsonWriter.StartObject();

        if (!shouldOmitIds(MGenType::_type_id)) {
            m_rapidJsonWriter.String("__t");
            m_rapidJsonWriter.String(idsString.c_str());
        }

        m_depth++;
        if (m_depth >= m_expectType.size())
            throw SerializationException("Max recursion depth reached");

    }

    void endVisit() {
        m_depth--;
        m_rapidJsonWriter.EndObject();
    }

private:

    void writePoly(const MGenBase& v) {
        m_classRegistry.visitObject(v, *this);
    }

    template<typename MGenType>
    void write(const MGenType& v) {
        m_expectType[m_depth] = MGenType::_type_id;
        v._accept(*this);
    }

    template<typename MGenType>
    void write(const Polymorphic<MGenType>& v) {
        if (v.get()) {
            m_expectType[m_depth] = MGenType::_type_id;
            writePoly(*v);
        } else {
            m_rapidJsonWriter.Null();
        }
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

    void write(const bool v) {
        m_rapidJsonWriter.Bool(v);
    }
    void write(const char v) {
        m_rapidJsonWriter.Int(v);
    }
    void write(const short v) {
        m_rapidJsonWriter.Int(v);
    }
    void write(const int v) {
        m_rapidJsonWriter.Int(v);
    }
    void write(const long long v) {
        m_rapidJsonWriter.Int64(v);
    }
    void write(const float v) {
        m_rapidJsonWriter.Double(v);
    }
    void write(const double v) {
        m_rapidJsonWriter.Double(v);
    }
    void write(const std::string& v) {
        m_rapidJsonWriter.String(v.c_str());
    }

    bool shouldOmitIds(const long long expId) {
        return m_compact && m_depth > 0 && expId == m_expectType[m_depth];
    }

    const bool m_compact;
    std::vector<long long> m_expectType;
    int m_depth;
    MGenStreamType& m_outputStream;
    internal::JsonOutStream<MGenStreamType> m_jsonStream;
    RapidJsonWriterType m_rapidJsonWriter;
    const ClassRegistryType& m_classRegistry;
};

} /* namespace mgen */

#endif /* JSON_WRITER_BASE_H_ */
