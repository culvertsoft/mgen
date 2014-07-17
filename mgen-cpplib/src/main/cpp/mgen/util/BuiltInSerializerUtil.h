/*
 * BuiltInSerializerUtil.h
 *
 *  Created on: 24 jun 2014
 *      Author: GiGurra
 */

#ifndef MGEN_BUILTINSERIALIZERUTIL_H_
#define MGEN_BUILTINSERIALIZERUTIL_H_

#include "mgen/classes/MGenBase.h"
#include "mgen/exceptions/UnexpectedTypeException.h"
#include "mgen/util/stringutil.h"

namespace mgen {
namespace serialutil {

#define throw_unexpected_type(expect, actual) \
    throw UnexpectedTypeException(toString("Unexpected type! -> Expected type ").append(toString(expect)).append(" but got type ").append(toString(actual)))

template <typename ClassRegistryType, typename MGenType, typename IdType>
void throwByUnexpectedIds(
        const ClassRegistryType& classReg,
        const MGenType * obj,
        const std::vector<IdType>& expIds,
        const std::vector<IdType>& actualIds) {
    const mgen::ClassRegistryEntry * entry = classReg.getByIds(actualIds);
    if (entry) {
        throw_unexpected_type(MGenType::_type_name(), entry->typeName());
    } else {
        throw_unexpected_type(MGenType::_type_name(), toString("unknown: ").append(toString(actualIds)));
    }
}

template<typename ReaderType, typename ClassRegType, typename ContextType>
MGenBase * readObjInternal(
        ReaderType& reader,
        const ClassRegType& classRegistry,
        ContextType& context,
        MGenBase * object,
        const ClassRegistryEntry& entry) {

    if (!object) {
        try {
            object = entry.newInstance();
            classRegistry.readObjectFields(*object, context, reader);
        } catch (...) {
            delete object;
            throw;
        }
    } else {
        classRegistry.readObjectFields(*object, context, reader);
    }

    return object;

}

template<typename ClassRegType, typename IdType>
const ClassRegistryEntry * getCompatibleEntry(
        const ClassRegType& classReg,
        const std::vector<IdType>& ids,
        const bool isExpType,
        const long long expType) {

    if (!ids.empty()) {

        const ClassRegistryEntry * entry = classReg.getByIds(ids);

        if (isExpType) {
            if (entry) {
                if (!entry->isInstanceOfTypeId(expType)) {
                    const ClassRegistryEntry * expEntry = classReg.getByTypeId(expType);
                    throw UnexpectedTypeException(
                            std::string(
                                    "BuiltInSerializerUtil::getCompatibleEntry: Unexpected type. Expected ").append(
                                    expEntry ? expEntry->typeName() : " <unknown> ").append(
                                    " but got ").append(entry->typeName()));
                }

            } else if (isExpType) {
                throw UnexpectedTypeException(
                        "BuiltInSerializerUtil::getCompatibleEntry: Unknown type: "
                                + toString(ids));
            }
        }

        return entry;

    } else if (isExpType) {
        return classReg.getByTypeId(expType);
    } else {
        throw SerializationException(
                "BuiltInSerializerUtil::getCompatibleEntry: Missing type information");
    }

}

template<typename MGenClassRegType, typename MGenType, typename IdType>
void checkExpType(
        const MGenClassRegType& classReg,
        const MGenType * o,
        const std::vector<IdType>& expIds,
        const std::vector<IdType>& actualIds) {

    // Ids were omitted, so must assume true
    if (actualIds.empty())
        return;

    if (actualIds.size() < expIds.size())
        throwByUnexpectedIds(classReg, o, expIds, actualIds);

    for (int i = 0; i < expIds.size(); i++) {
        if (actualIds[i] != expIds[i]) {
            throwByUnexpectedIds(classReg, o, expIds, actualIds);
        }
    }

}

template<typename MGenClassRegType, typename MGenType>
void checkExpType(
        const MGenClassRegType& classReg,
        const MGenType * o,
        const std::vector<std::string>& actualIds) {
    static const std::vector<std::string>& expIds = MGenType::_type_ids_16bit_base64();
    checkExpType(classReg, o, expIds, actualIds);
}

template<typename MGenClassRegType, typename MGenType>
void checkExpType(
        const MGenClassRegType& classReg,
        const MGenType * o,
        const std::vector<short>& actualIds) {
    static const std::vector<short>& expIds = MGenType::_type_ids_16bit();
    checkExpType(classReg, o, expIds, actualIds);
}

#undef throw_unexpected_type

}
/* namespace serialutil */
} /* namespace mgen */

#endif /* MGEN_BUILTINSERIALIZERUTIL_H_ */
