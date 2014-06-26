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

    ClassRegistryEntry(
            const std::vector<long long>& typeIds = std::vector<long long>(),
            const std::string& typeName = "",
            MgenCtorFn ctor = 0) :
            m_typeIds(typeIds), m_typeName(typeName), m_ctor(ctor) {
    }

    MGenBase * newInstance() const {
        return m_ctor ? m_ctor() : 0;
    }

    const std::string& typeName() const {
        return m_typeName;
    }

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

#endif /* CLASSREGISTRYENTRY_H_ */
