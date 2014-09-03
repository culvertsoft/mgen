#ifndef MGEN_JSON_PRETTY_WRITER_H_
#define MGEN_JSON_PRETTY_WRITER_H_

#include "mgen/serialization/JsonWriterBase.h"
#include "mgen/ext/rapidjson/prettywriter.h"

namespace mgen {

/**
 * MGen writer which writes 'pretty' JSON. That is JSON with expected
 * indentation and newlines. The standard MGen JsonWriter writes
 * compact json.
 */
template<typename MGenStreamType, typename ClassRegistryType>
class JsonPrettyWriter: public JsonWriterBase<MGenStreamType, ClassRegistryType,
        rapidjson::PrettyWriter<internal::JsonOutStream<MGenStreamType> > > {
    typedef JsonWriterBase<MGenStreamType, ClassRegistryType,
            rapidjson::PrettyWriter<internal::JsonOutStream<MGenStreamType> > > super;
public:

    /**
     * Creates a new JsonPrettyWriter (JsonWriter) around the provided data 
     * output stream and class registry. A third optional parameter can be 
     * specified to omit writing type ids before objects where they can be
     * inferred by readers (makes the data written a little cleaner, 
     * particularly useful for configuration files).
     */
    JsonPrettyWriter(
            MGenStreamType& outputStream,
            const ClassRegistryType& classRegistry,
            const bool compact = super::default_compact) :
                    super(outputStream, classRegistry, compact) {
        super::m_rapidJsonWriter.SetIndent('\t', 1);
    }

};

template<typename MGenStreamType, typename ClassRegistryType>
inline JsonPrettyWriter<MGenStreamType, ClassRegistryType> make_JsonPrettyWriter(
		MGenStreamType& stream,
		const ClassRegistryType& classRegistry,
		const bool compact = false) {
	return JsonPrettyWriter<MGenStreamType, ClassRegistryType>(stream, classRegistry, compact);
}

} /* namespace mgen */

#endif /* MGEN_JSON_PRETTY_WRITER_H_ */
