#ifndef MGEN_JSON_WRITER_H_
#define MGEN_JSON_WRITER_H_

#include "mgen/serialization/JsonWriterBase.h"
#include "mgen/ext/rapidjson/writer.h"

namespace mgen {

/**
 * MGen writer which writes 'compact' JSON. That is JSON without 
 * indentation or newlines. Also see: JsonPrettyWriter.h.
 */
template<typename MGenStreamType, typename ClassRegistryType>
class JsonWriter: public JsonWriterBase<MGenStreamType, ClassRegistryType,
        rapidjson::Writer<internal::JsonOutStream<MGenStreamType> > > {
    typedef JsonWriterBase<MGenStreamType, ClassRegistryType,
            rapidjson::Writer<internal::JsonOutStream<MGenStreamType> > > super;
public:

    /**
     * Creates a new JsonWriter around the provided data 
     * output stream and class registry. A third optional parameter can be 
     * specified to omit writing type ids before objects where they can be
     * inferred by readers (makes the data written a little cleaner, 
     * particularly useful for configuration files).
     */
    JsonWriter(
            MGenStreamType& outputStream,
            const ClassRegistryType& classRegistry,
            const bool compact = super::default_compact) :
                    super(outputStream, classRegistry, compact) {
    }

};

template<typename MGenStreamType, typename ClassRegistryType>
inline JsonWriter<MGenStreamType, ClassRegistryType> make_JsonWriter(
		MGenStreamType& stream,
		const ClassRegistryType& classRegistry,
		const bool compact = false) {
	return JsonWriter<MGenStreamType, ClassRegistryType>(stream, classRegistry, compact);
}

} /* namespace mgen */

#endif /* MGEN_JSON_WRITER_H_ */
