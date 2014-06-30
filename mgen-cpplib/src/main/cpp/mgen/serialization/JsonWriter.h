#ifndef JSON_WRITER_H_
#define JSON_WRITER_H_

#include "JsonWriterBase.h"
#include "mgen/ext/rapidjson/writer.h"

namespace mgen {

template<typename MGenStreamType, typename ClassRegistryType>
class JsonWriter: public JsonWriterBase<MGenStreamType, ClassRegistryType, rapidjson::Writer<internal::JsonOutStream<MGenStreamType> > > {
    typedef JsonWriterBase<MGenStreamType, ClassRegistryType, rapidjson::Writer<internal::JsonOutStream<MGenStreamType> > > super;
public:

    JsonWriter(MGenStreamType& outputStream, const ClassRegistryType& classRegistry) :
            super(outputStream, classRegistry) {
    }

};

} /* namespace mgen */

#endif /* JSON_WRITER_H_ */
