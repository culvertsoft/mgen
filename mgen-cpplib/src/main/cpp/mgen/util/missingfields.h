/*
 * MissingFields.h
 *
 *  Created on: 18 mar 2014
 *      Author: GiGurra
 */

#ifndef MISSINGFIELDS_H_
#define MISSINGFIELDS_H_

#include "mgen/classes/MGenBase.h"

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

} /* namespace missingfields */
} /* namespace mgen */

#endif /* MISSINGFIELDS_H_ */
