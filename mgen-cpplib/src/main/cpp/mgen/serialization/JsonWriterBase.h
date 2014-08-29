#ifndef MGEN_JSON_WRITER_BASE_H_
#define MGEN_JSON_WRITER_BASE_H_

#include "mgen/serialization/JsonOutputStream.h"
#include "mgen/util/missingfields.h"
#include "mgen/util/stringutil.h"

namespace mgen {

/**
 * Base class for all MGen Json writers. Not intended to be constructed directly. 
 * See JsonWriter.h and JsonPrettyWriter.h instead.
 */
template<typename MGenStreamType, typename ClassRegistryType, typename RapidJsonWriterType>
class JsonWriterBase {
public:

    static const bool default_compact = false;

    /**
     * Creates a new JsonWriterBase around the provided data 
     * output stream and class registry. A third optional parameter can be 
     * specified to omit writing type ids before objects where they can be
     * inferred by readers (makes the data written a little cleaner, 
     * particularly useful for configuration files).
     */
    JsonWriterBase(
            MGenStreamType& outputStream, 
            const ClassRegistryType& classRegistry, 
            const bool compact = default_compact) :
                m_compact(compact),
                m_expectType(-1),
                m_outputStream(outputStream),
                m_jsonStream(m_outputStream),
                m_rapidJsonWriter(m_jsonStream),
                m_classRegistry(classRegistry) {
    }

    /**
     * Primary interface method for writing MGen objects to this writer. The class of the
     * object must be registered in class registry provided to this JsonWriterBase when it was
     * constructed (this is always the case unless you have multiple data models in parallel).
     */
    void writeObject(const MGenBase& object) {
        m_expectType = -1;
        writePoly(object);
    }

    /**
     * When this writer is visiting an object it should write, this method will be called
     * before starting to visit any fields. For the case of (this) JsonWriterBase, it will
     * case it to write a beginning brace of the object o follow, and potentially object
     * type metadata (in a field named '__t').
     */
    template<typename MGenType>
    void beginVisit(const MGenType& object, const int nFieldsToVisit) {

        missingfields::ensureNoMissingFields(object);

        m_rapidJsonWriter.StartObject();

        if (!shouldOmitIds(MGenType::_type_id)) {
            m_rapidJsonWriter.String("__t");
            m_rapidJsonWriter.String(MGenType::_type_ids_16bit_base64_string().c_str());
        }
    }

    /**
     * When this writer is visiting an object it should write, this method will be called
     * for each selected object field to be written.
     */
    template<typename T>
    void visit(const T& v, const Field& field) {
        m_rapidJsonWriter.String(field.name().c_str());
        write(v);
    }

    /**
     * Method called when all selected fields of an object have been visited. 
     * Writes the ending brace of this object.
     */
    void endVisit() {
        m_rapidJsonWriter.EndObject();
    }

private:

    /**
     * Internal method for writing a polymorphic MGen object.
     */
    void writePoly(const MGenBase& v) {
        m_classRegistry.visitObject(v, *this, mgen::ALL_SET_NONTRANSIENT);
    }

    /**
     * Internal method for writing an MGen object of statically known class.
     */
    template<typename MGenType>
    void write(const MGenType& v, const MGenBase&) {
        m_expectType = MGenType::_type_id;
        v._accept(*this, ALL_SET_NONTRANSIENT);
    }

    /**
     * Internal method for writing an enum.
     */
    template<typename EnumType>
    void write(const EnumType v, const int) {
        write(get_enum_name(v));
    }

    /**
     * Internal method for writing an enum or MGen object.
     */
    template<typename MGenTypeOrEnum>
    void write(const MGenTypeOrEnum& v) {
        write(v, v);
    }

    /**
     * Internal method for writing a polymorphic MGen object.
     */
    template<typename MGenType>
    void write(const Polymorphic<MGenType>& v) {
        if (v.get()) {
            m_expectType = MGenType::_type_id;
            writePoly(*v);
        } else {
            m_rapidJsonWriter.Null();
        }
    }

    /**
     * Internal method for writing a list.
     */
    template<typename T>
    void write(const std::vector<T>& v) {
        m_rapidJsonWriter.StartArray();
        for (std::size_t i = 0; i < v.size(); i++)
            write(v[i]);
        m_rapidJsonWriter.EndArray();
    }

    /**
     * Internal method for writing a map.
     */
    template<typename K, typename V>
    void write(const std::map<K, V>& v) {
        m_rapidJsonWriter.StartObject();
        for (typename std::map<K, V>::const_iterator it = v.begin(); it != v.end(); it++) {
            write(toString(it->first));
            write(it->second);
        }
        m_rapidJsonWriter.EndObject();
    }

    /**
     * Internal method for writing a bool.
     */
    void write(const bool v) { m_rapidJsonWriter.Bool(v); }

    /**
     * Internal method for writing an int8.
     */
    void write(const char v) { m_rapidJsonWriter.Int(v); }
    
    /**
     * Internal method for writing an int16.
     */
    void write(const short v) { m_rapidJsonWriter.Int(v); }
    
    /**
     * Internal method for writing an int32.
     */
    void write(const int v) { m_rapidJsonWriter.Int(v); }
    
    /**
     * Internal method for writing an int64.
     */
    void write(const long long v) { m_rapidJsonWriter.Int64(v); }
    
    /**
     * Internal method for writing a float32.
     */
    void write(const float v) { m_rapidJsonWriter.Double(v); }
    
    /**
     * Internal method for writing a float64.
     */
    void write(const double v) { m_rapidJsonWriter.Double(v); }
    
    /**
     * Internal method for writing a string.
     */
    void write(const std::string& v) {
        m_rapidJsonWriter.String(v.c_str());
    }

    /**
     * Internal convenience method for testing if we need to write type id
     * metadata for the object which is currently to be written.
     */
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

#endif /* MGEN_JSON_WRITER_BASE_H_ */
