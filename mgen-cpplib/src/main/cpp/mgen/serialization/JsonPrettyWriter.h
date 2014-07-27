#ifndef MGEN_JSON_PRETTY_WRITER_H_
#define MGEN_JSON_PRETTY_WRITER_H_

#include "mgen/serialization/JsonWriterBase.h"
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
        super::m_rapidJsonWriter.SetIndent('\t', 1);
    }

};

} /* namespace mgen */

#endif /* MGEN_JSON_PRETTY_WRITER_H_ */
