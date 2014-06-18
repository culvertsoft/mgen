/*
 * ClassRegistryBase.h
 *
 *  Created on: Mar 2, 2014
 *      Author: johan
 */

#ifndef CLASSREGISTRYBASE_H_
#define CLASSREGISTRYBASE_H_

#include "mgen/classes/ClassRegistryEntry.h"
#include "mgen/exceptions/Exception.h"

namespace mgen {

class ClassRegistryBase {
public:

    template<typename T>
    void add() {

        ClassRegistryEntry entry(T::_TYPE_NAME(), T::_newInstance);

        m_typename2Entry[T::_TYPE_NAME()] = entry;
        m_hash16bit2Entry[T::_TYPE_HASH_16BIT] = entry;
        m_hash16bitBase642Entry[T::_TYPE_HASH_16BIT_BASE64()] = entry;

    }

    const ClassRegistryEntry * getByHash16bit(const short hash16bit) const {
        const std::map<short, ClassRegistryEntry>::const_iterator it = m_hash16bit2Entry.find(
                hash16bit);
        return it != m_hash16bit2Entry.end() ? &(it->second) : 0;
    }

    const ClassRegistryEntry * getByTypeName(const std::string& typeName) const {
        const std::map<std::string, ClassRegistryEntry>::const_iterator it = m_typename2Entry.find(
                typeName);
        return it != m_typename2Entry.end() ? &(it->second) : 0;
    }

    const ClassRegistryEntry * getByBase64Hash16bit(const std::string& hash) const {
        const std::map<std::string, ClassRegistryEntry>::const_iterator it =
                m_hash16bitBase642Entry.find(hash);
        return it != m_hash16bitBase642Entry.end() ? &(it->second) : 0;
    }

    const std::map<short, ClassRegistryEntry>& getEntriesHash16bit() const {
        return m_hash16bit2Entry;
    }

    const std::map<std::string, ClassRegistryEntry>& getEntriesTypeName() const {
        return m_typename2Entry;
    }

    const std::map<std::string, ClassRegistryEntry>& getEntriesHash16bitBase64() const {
        return m_hash16bitBase642Entry;
    }

private:
    std::map<short, ClassRegistryEntry> m_hash16bit2Entry;
    std::map<std::string, ClassRegistryEntry> m_typename2Entry;
    std::map<std::string, ClassRegistryEntry> m_hash16bitBase642Entry;

};

} /* namespace mgen */

#endif /* CLASSREGISTRYBASE_H_ */
