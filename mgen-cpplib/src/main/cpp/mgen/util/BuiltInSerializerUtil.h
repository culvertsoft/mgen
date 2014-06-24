/*
 * Marker.h
 *
 *  Created on: 21 mar 2014
 *      Author: GiGurra
 */

#ifndef MARKER_H_
#define MARKER_H_

#include "mgen/classes/MGenBase.h"
#include "mgen/util/stringutil.h"
#include "mgen/exceptions/UnexpectedTypeException.h"

namespace mgen {
namespace serialutil {

template <typename ClassRegType>
void ensureExpectedType(
		const ClassRegType& classRegistry,
		const ClassRegistryEntry& entry,
		const long long expId,
		const bool constrained) {

	if (constrained && !entry.isInstanceOfTypeId(expId)) {

		const ClassRegistryEntry * expEntry = classRegistry.getByTypeId(expId);

		throw UnexpectedTypeException(
				toString("readMgenObject::Unexpected type. Expecting ").append(
						expEntry ? expEntry->typeName() : "any").append(
						" but got ").append(entry.typeName()));

	}
}

template <typename ReaderType, typename ClassRegType, typename ContextType>
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

template <typename ReaderType, typename ClassRegType, typename ContextType>
MGenBase * readObjFromEntry(
		ReaderType& reader,
		const ClassRegType& classRegistry,
		ContextType& context,
		MGenBase * object,
		const ClassRegistryEntry * entry,
		const long long expTypeId,
		const bool constrained) {

	if (entry && entry != mgen::ClassRegistryEntry::NULL_ENTRY()) {
		ensureExpectedType(classRegistry, *entry, expTypeId, constrained);
		return readObjInternal(reader, classRegistry, context, object, *entry);
	} else {
		return object;
	}

}

}
/* namespace serialutil */
} /* namespace mgen */

#endif /* MARKER_H_ */
