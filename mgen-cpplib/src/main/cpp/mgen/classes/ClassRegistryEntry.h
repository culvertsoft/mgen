/*
 * ClassRegistryEntry.h
 *
 *  Created on: Mar 2, 2014
 *      Author: johan
 */

#ifndef CLASSREGISTRYENTRY_H_
#define CLASSREGISTRYENTRY_H_

#include "mgen/classes/MGenBase.h"

namespace mgen {

typedef MGenBase * MGenBasePtr;
typedef MGenBasePtr (*MgenCtorFn)();

class ClassRegistryEntry {
public:

    ClassRegistryEntry(const std::string& typeName = "", MgenCtorFn ctor = 0) :
            m_typeName(typeName), m_ctor(ctor) {
    }

    MGenBase * newInstance() const {
        return m_ctor ? m_ctor() : 0;
    }

    const std::string& typeName() const {
        return m_typeName;
    }

    static const ClassRegistryEntry * NULL_ENTRY() {
        static ClassRegistryEntry entry;
        return &entry;
    }

private:
    std::string m_typeName;
    MgenCtorFn m_ctor;

};

} /* namespace mgen */

#endif /* CLASSREGISTRYENTRY_H_ */
