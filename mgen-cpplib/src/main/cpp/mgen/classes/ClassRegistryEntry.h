/*
 * ClassRegistryEntry.h
 *
 *  Created on: Mar 2, 2014
 *      Author: johan
 */

#ifndef MGEN_CLASSREGISTRYENTRY_H_
#define MGEN_CLASSREGISTRYENTRY_H_

#include <mgen/classes/MGenBase.h>

namespace mgen {

typedef MGenBase * MGenBasePtr;
typedef MGenBasePtr (*MgenCtorFn)();

/**
 * A ClassRegistryEntry represents a way to get information about a
 * generated class. It also provides a generic way for creating object
 * instances of that class.
 *
 * Generally speaking, ClassRegistryEntry instances are only intended
 * to be created by generated code.
 */
class ClassRegistryEntry {
public:

    /**
     * Creates a new ClassRegistryEntry from a class: its 64 bit type ids, 
     * its qualified type name and a function pointer to a function which
     * can create a new instance of the class that this ClassRegistryEntry
     * represents.
     */
    ClassRegistryEntry(
            const std::vector<long long>& typeIds = std::vector<long long>(),
            const std::string& typeName = "",
            MgenCtorFn ctor = 0) :
            m_typeIds(typeIds), m_typeName(typeName), m_ctor(ctor) {
    }

    /**
     * Calls the function pointer associated with this ClassRegistryEntry 
     * when it was created.
     */
    MGenBase * newInstance() const {
        return m_ctor ? m_ctor() : 0;
    }

    /**
     * Gets the qualified type name of the class that this ClassRegistryEntry
     * represents.
     */
    const std::string& typeName() const {
        return m_typeName;
    }

    /**
     * A way to determine if this ClassRegistryEntry represents a sub class of the
     * provided 64 bit type id. This is useful when reading objects from streams
     * and wanting to verify that the read objects are of the correct type to
     * match the field they are being read to.
     */
    bool isInstanceOfTypeId(const long long typeId) const {
        for (int i = int(m_typeIds.size()) - 1; i >= 0; i--) {
            if (m_typeIds[i] == typeId) {
                return true;
            }
        }
        return false;
    }

private:
    std::vector<long long> m_typeIds;
    std::string m_typeName;
    MgenCtorFn m_ctor;

};

} /* namespace mgen */

#endif /* MGEN_CLASSREGISTRYENTRY_H_ */
