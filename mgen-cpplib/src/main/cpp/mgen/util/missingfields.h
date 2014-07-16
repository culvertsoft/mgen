/*
 * MissingFields.h
 *
 *  Created on: 18 mar 2014
 *      Author: GiGurra
 */

#ifndef MGEN_MISSINGFIELDS_H_
#define MGEN_MISSINGFIELDS_H_

#include "mgen/classes/MGenBase.h"
#include "mgen/exceptions/SerializationException.h"
#include "mgen/serialization/BinaryTags.h"

namespace mgen {
namespace missingfields {

inline std::vector<Field> required(const MGenBase& object, const FieldSetDepth depth = SHALLOW);
inline std::string requiredAsString(const MGenBase& object, const FieldSetDepth depth = SHALLOW);

inline std::vector<Field> required(const MGenBase& object, const FieldSetDepth depth) {

    const std::vector<Field>& allFields = object._fieldMetadatas();
    std::vector<Field> missingReqFields;
    for (std::size_t i = 0; i < allFields.size(); i++) {
        if (allFields[i].isRequired() && !object._isFieldSet(allFields[i], depth)) {
            missingReqFields.push_back(allFields[i]);
        }
    }

    return missingReqFields;

}

inline std::string requiredAsString(const MGenBase& object, const FieldSetDepth depth) {

    std::vector<Field> missingReqFields = required(object, depth);

    std::string missingFieldsString = "[";

    for (std::size_t i = 0; i < missingReqFields.size(); i++) {
        missingFieldsString += missingReqFields[i].name();
        if (i + 1 < missingReqFields.size())
            missingFieldsString += ", ";
    }

    missingFieldsString += "]";

    return missingFieldsString;

}

template<typename MGenType>
inline void ensureNoMissingFields(const MGenType& object) {
    if (!object._validate(SHALLOW)) {
        const std::string missingFieldsString = requiredAsString(object);
        throw SerializationException(
                std::string("Missing required fields ").append(missingFieldsString).append(
                        " for object of type: ").append(object._typeName()));
    }
}

} /* namespace missingfields */
} /* namespace mgen */

#endif /* MGEN_MISSINGFIELDS_H_ */
