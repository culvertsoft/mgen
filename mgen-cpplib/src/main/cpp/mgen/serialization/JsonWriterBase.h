#ifndef JSON_WRITER_BASE_H_
#define JSON_WRITER_BASE_H_

#include "JsonOutputStream.h"
#include "mgen/util/missingfields.h"
#include "mgen/util/stringutil.h"

namespace mgen {

template<typename MGenStreamType, typename ClassRegistryType, typename RapidJsonWriterType>
class JsonWriterBase {
public:

    static const bool default_compact = false;

    JsonWriterBase(MGenStreamType& outputStream, const ClassRegistryType& classRegistry, const bool compact =
            default_compact) :
                    m_compact(compact),
                    m_expectType(-1),
                    m_outputStream(outputStream),
                    m_jsonStream(m_outputStream),
                    m_rapidJsonWriter(m_jsonStream),
                    m_classRegistry(classRegistry) {
    }

    void writeObject(const MGenBase& object) {
        m_expectType = -1;
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
    }

    void endVisit() {
        m_rapidJsonWriter.EndObject();
    }

private:

    void writePoly(const MGenBase& v) {
        m_classRegistry.visitObject(v, *this);
    }

    template<typename MGenType>
    void write(const MGenType& v, const MGenBase&) {
        m_expectType = MGenType::_type_id;
        v._accept(*this);
    }

    template<typename EnumType>
    void write(const EnumType v, const int) {
        write(get_enum_name(v));
    }

    template<typename MGenTypeOrEnum>
    void write(const MGenTypeOrEnum& v) {
        write(v, v);
    }

    template<typename MGenType>
    void write(const Polymorphic<MGenType>& v) {
        if (v.get()) {
            m_expectType = MGenType::_type_id;
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
            write(toString(it->first));
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
        return m_compact && expId == m_expectType;
    }

protected:

    const bool m_compact;
    long long m_expectType;
    MGenStreamType& m_outputStream;
    internal::JsonOutStream<MGenStreamType> m_jsonStream;
    RapidJsonWriterType m_rapidJsonWriter;
    const ClassRegistryType& m_classRegistry;

};

} /* namespace mgen */

#endif /* JSON_WRITER_BASE_H_ */
