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

    static const int INVALID_16BIT_ID = 0xFFFFFFFF;

    template<typename T>
    void add() {
        ClassRegistryEntry entry(T::_type_ids(), T::_type_name(), T::_newInstance);
        long long id = T::_type_id;
        m_typeId2Entry[id] = entry;
        m_typeId16BitBase642TypeId16Bit[T::_type_id_16bit_base64()] = T::_type_id_16bit;
        m_typeName2Entry[T::_type_name()] = entry;
    }

    const ClassRegistryEntry * getByTypeId(const long long typeId) const {
        const std::map<long long, ClassRegistryEntry>::const_iterator it = m_typeId2Entry.find(typeId);
        return it != m_typeId2Entry.end() ? &(it->second) : 0;
    }

    const ClassRegistryEntry * getByTypeName(const std::string& typeName) const {
        const std::map<std::string, ClassRegistryEntry>::const_iterator it = m_typeName2Entry.find(typeName);
        return it != m_typeName2Entry.end() ? &(it->second) : 0;
    }

    int getTypeId16bitFromTypeId16BitBase64(const std::string& typeId16bitBase64) const {
        const std::map<std::string, int>::const_iterator it = m_typeId16BitBase642TypeId16Bit.find(typeId16bitBase64);
        return it != m_typeId16BitBase642TypeId16Bit.end() ? it->second : INVALID_16BIT_ID;
    }

    // Generated subclass code
    const ClassRegistryEntry * getByIds(const std::vector<short>& typeIds16bit) const {
        return 0;
    }

    // Generated subclass code
    const ClassRegistryEntry * getByIds(const std::vector<std::string>& typeIds16bitBase64) const {
        return 0;
    }

private:
    std::map<long long, ClassRegistryEntry> m_typeId2Entry;
    std::map<std::string, ClassRegistryEntry> m_typeName2Entry;
    std::map<std::string, int> m_typeId16BitBase642TypeId16Bit;

};

} /* namespace mgen */

#endif /* CLASSREGISTRYBASE_H_ */
