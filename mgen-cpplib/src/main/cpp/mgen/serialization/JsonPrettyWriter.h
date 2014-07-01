#ifndef JSON_PRETTY_WRITER_H_
#define JSON_PRETTY_WRITER_H_

#include "JsonWriterBase.h"
#include "mgen/ext/rapidjson/prettywriter.h"

namespace mgen {

template<typename MGenStreamType, typename ClassRegistryType>
class JsonPrettyWriter: public JsonWriterBase<MGenStreamType, ClassRegistryType,
        rapidjson::PrettyWriter<internal::JsonOutStream<MGenStreamType> > > {
    typedef JsonWriterBase<MGenStreamType, ClassRegistryType,
            rapidjson::PrettyWriter<internal::JsonOutStream<MGenStreamType> > > super;
public:

    JsonPrettyWriter(
            MGenStreamType& outputStream,
            const ClassRegistryType& classRegistry,
            const bool compact = super::default_compact) :
                    super(outputStream, classRegistry, compact) {
    }

};

} /* namespace mgen */

#endif /* JSON_PRETTY_WRITER_H_ */
