/*
 * ClassRegistryBase.h
 *
 *  Created on: Mar 2, 2014
 *      Author: johan
 */

#ifndef MGEN_CLASSREGISTRYBASE_H_
#define MGEN_CLASSREGISTRYBASE_H_

#include <sstream>

#include "mgen/classes/ClassRegistryEntry.h"
#include "mgen/exceptions/Exception.h"

namespace mgen {

/**
 * Base class for all MGen class registries. When generating classes for an MGen project,
 * a class registry is also generated. The class registry provides class lookups from 
 * names and type ids.
 */
class ClassRegistryBase {
public:

    typedef std::map<long long, ClassRegistryEntry> EntryMap;
    static const int INVALID_16BIT_ID = 0xFFFFFFFF;
    
    /**
     * Gets a class registry entry by 64 bit type id.
     */
    const ClassRegistryEntry * getByTypeId(const long long typeId) const {
        const std::map<long long, ClassRegistryEntry>::const_iterator it = m_typeId2Entry.find(typeId);
        return it != m_typeId2Entry.end() ? &(it->second) : 0;
    }

    /**
     * Gets a class registry entry by qualified class name (name.space.ClassName).
     */
    const ClassRegistryEntry * getByTypeName(const std::string& typeName) const {
        const std::map<std::string, ClassRegistryEntry>::const_iterator it = m_typeName2Entry.find(typeName);
        return it != m_typeName2Entry.end() ? &(it->second) : 0;
    }
    
    /**
     * Gets a class registry entry by 16 bit type ids. This method is entirely implemented
     * in generated class registries.
     */
    const ClassRegistryEntry * getByIds(const std::vector<short>& base64ids_vector) const {
        return 0;
    }
    
    /**
     * Gets a class registry entry by 16 bit type ids in base64 format.
     */
    const ClassRegistryEntry * getByIds(const std::vector<std::string>& base64ids_vector) const {
        const std::string& base64ids = combine(base64ids_vector);
        for (int sz = int(base64ids.size()); sz >= 3; sz-=3) {
            const std::map<std::string, ClassRegistryEntry>::const_iterator it = 
                m_typeIds16bitBase642Entry.find(base64ids.substr(0, sz));
            if (it != m_typeIds16bitBase642Entry.end()) {
                return &it->second;
            }
        }
        return 0;
    }

    const EntryMap& entries() const {
        return m_typeId2Entry;
    }
    
protected:

    /**
     * This function is called exclusively from constructors of generated
     * class registries. Adds the provided class (template parameter) by
     * 64 bit id, name and 16bit base64 id sequence.
     */
    template<typename T>
    void add() {
        ClassRegistryEntry entry(T::_type_ids(), T::_type_name(), T::_newInstance);
        long long id = T::_type_id;
        m_typeId2Entry[id] = entry;
        m_typeIds16bitBase642Entry[T::_type_ids_16bit_base64_string()] = entry;
        m_typeName2Entry[T::_type_name()] = entry;
    }

private:
    std::map<long long, ClassRegistryEntry> m_typeId2Entry;
    std::map<std::string, ClassRegistryEntry> m_typeName2Entry;
	std::map<std::string, ClassRegistryEntry> m_typeIds16bitBase642Entry;
    
    std::string combine(const std::vector<std::string>& strings) const {        
        std::stringstream ss;
        for (std::vector<std::string>::const_iterator it = strings.begin(); it != strings.end(); it++) {
            ss << *it;
        }
        return ss.str();
    }

};

} /* namespace mgen */

#endif /* MGEN_CLASSREGISTRYBASE_H_ */
