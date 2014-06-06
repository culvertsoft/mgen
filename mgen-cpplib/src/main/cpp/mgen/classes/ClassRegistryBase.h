/*
 * ClassRegistryBase.h
 *
 *  Created on: Mar 2, 2014
 *      Author: johan
 */

#ifndef CLASSREGISTRYBASE_H_
#define CLASSREGISTRYBASE_H_

#include "mgen/classes/ClassRegistryEntry.h"
#include "mgen/exceptions/TypeConflictException.h"

namespace mgen {

class ClassRegistryBase {
public:

    template<typename T>
    void add() {

        check_for_conflicts(
                m_hash16bit2Entry,
                (short) T::_TYPE_HASH_16BIT,
                T::_TYPE_NAME(),
                "Hash (16bit)");
        check_for_conflicts(
                m_hash32bit2Entry,
                (int) T::_TYPE_HASH_32BIT,
                T::_TYPE_NAME(),
                "Hash (32bit)");
        check_for_conflicts(m_typename2Entry, T::_TYPE_NAME(), T::_TYPE_NAME(), "Type name");

        ClassRegistryEntry entry(T::_TYPE_NAME(), T::_newInstance);

        m_hash16bit2Entry[T::_TYPE_HASH_16BIT] = entry;
        m_hash32bit2Entry[T::_TYPE_HASH_32BIT] = entry;
        m_typename2Entry[T::_TYPE_NAME()] = entry;
        m_hash16bitBase642Entry[T::_TYPE_HASH_16BIT_BASE64()] = entry;
        m_hash32bitBase642Entry[T::_TYPE_HASH_32BIT_BASE64()] = entry;

    }

    const ClassRegistryEntry * getByHash16bit(const short hash16bit) const {
        const std::map<short, ClassRegistryEntry>::const_iterator it = m_hash16bit2Entry.find(
                hash16bit);
        return it != m_hash16bit2Entry.end() ? &(it->second) : 0;
    }

    const ClassRegistryEntry * getByHash32bit(const int hash32bit) const {
        const std::map<int, ClassRegistryEntry>::const_iterator it = m_hash32bit2Entry.find(
                hash32bit);
        return it != m_hash32bit2Entry.end() ? &(it->second) : 0;
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

    const ClassRegistryEntry * getByBase64Hash32bit(const std::string& hash) const {
        const std::map<std::string, ClassRegistryEntry>::const_iterator it =
                m_hash32bitBase642Entry.find(hash);
        return it != m_hash32bitBase642Entry.end() ? &(it->second) : 0;
    }

    const std::map<short, ClassRegistryEntry>& getEntriesHash16bit() const {
        return m_hash16bit2Entry;
    }

    const std::map<int, ClassRegistryEntry>& getEntriesHash32bit() const {
        return m_hash32bit2Entry;
    }

    const std::map<std::string, ClassRegistryEntry>& getEntriesTypeName() const {
        return m_typename2Entry;
    }

    const std::map<std::string, ClassRegistryEntry>& getEntriesHash16bitBase64() const {
        return m_hash16bitBase642Entry;
    }

    const std::map<std::string, ClassRegistryEntry>& getEntriesHash32bitBase64() const {
        return m_hash32bitBase642Entry;
    }

    void throwIfConflictsWith(const ClassRegistryBase& other) const {
        throwIfConflicts(m_hash16bit2Entry, other.m_hash16bit2Entry, "16bit hash");
        throwIfConflicts(m_hash32bit2Entry, other.m_hash32bit2Entry, "32bit hash");
        throwIfConflicts(m_typename2Entry, other.m_typename2Entry, "type name");
    }

private:
    std::map<short, ClassRegistryEntry> m_hash16bit2Entry;
    std::map<int, ClassRegistryEntry> m_hash32bit2Entry;
    std::map<std::string, ClassRegistryEntry> m_typename2Entry;
    std::map<std::string, ClassRegistryEntry> m_hash16bitBase642Entry;
    std::map<std::string, ClassRegistryEntry> m_hash32bitBase642Entry;

    template<typename K, typename V>
    void throwIfConflicts(
            const std::map<K, V>& a,
            const std::map<K, V>& b,
            const std::string& conflictType) const {
        typedef typename std::map<K, V>::const_iterator iter_t;
        for (iter_t aIt = a.begin(); aIt != a.end(); aIt++) {
            const iter_t bIt = b.find(aIt->first);
            if (bIt != b.end()) {
                throw_type_conflict(conflictType, aIt->second.typeName(), bIt->second.typeName());
            }
        }
    }

    void throw_type_conflict(
            const std::string& conflictType,
            const std::string& existName,
            const std::string& newName) const {
        throw TypeConflictException(
                std::string("ClassRegistryBase::add(..): ").append(conflictType).append(
                        " conflict with '").append(existName).append("' when adding '").append(
                        newName).append("'"));
    }

    template<typename KeyType>
    void check_for_conflicts(
            const std::map<KeyType, ClassRegistryEntry>& m,
            const KeyType& key,
            const std::string& typeName,
            const std::string& errMsgConflType) const {

        typename std::map<KeyType, ClassRegistryEntry>::const_iterator it = m.find(key);
        if (it != m.end())
            throw_type_conflict(errMsgConflType, it->second.typeName(), typeName);

    }

};

} /* namespace mgen */

#endif /* CLASSREGISTRYBASE_H_ */
