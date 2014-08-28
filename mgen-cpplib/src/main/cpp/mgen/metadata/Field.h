/*
 * Field.h
 *
 *  Created on: 3 mar 2014
 *      Author: GiGurra
 */

#ifndef MGEN_FIELD_H_
#define MGEN_FIELD_H_

#include <string>
#include <vector>
#include <map>
#include "mgen/classes/Polymorphic.h"

namespace mgen {

/**
 * Represents runtime metadata for a class field. This includes field id, 
 * field name and flags specified for this field before source code was
 * generated.
 */
class Field {
public:

    /**
     * Creates a Field metadata object. Parameters are field id, field name and 
     * flags specified for this field (e.g. required, polymorphic, transient, etc).
     */
    Field(
            const short id,
            const std::string& name,
            const std::vector<std::string>& flags = std::vector<std::string>()) :
                    m_id(id),
                    m_name(name),
                    m_flags(flags),
                    m_required(false),
                    m_transient(false) {
        for (std::size_t i = 0; i < flags.size(); i++) {
            if (flags[i] == "required") {
                m_required = true;
                break;
            } else if (flags[i] == "transient") {
                m_transient = true;
                break;
            }
        }
    }

    /**
     * Returns the id of this field.
     */
    short id() const {
        return m_id;
    }

    /**
     * Returns the name of this field.
     */
    const std::string& name() const {
        return m_name;
    }

    /**
     * Returns the specified flags of this field.
     */
    const std::vector<std::string>& flags() const {
        return m_flags;
    }

    /**
     * Convenience method for checking if 'required' is a 
     * specified flag for this field.
     */
    bool isRequired() const {
        return m_required;
    }

    /**
     * Convenience method for checking if 'transient' is a 
     * specified flag for this field.
     */
    bool isTransient() const {
        return m_transient;
    }

private:
    short m_id;
    std::string m_name;
    std::vector<std::string> m_flags;
    bool m_required;
    bool m_transient;

};

/**
 * Several methods require a specified depth parameter for 
 * checking various conditions. That is what this enum is for.
 */
enum FieldSetDepth {
    SHALLOW,
    DEEP
};

/**
 * Generated classes have visitation methods (MGenBase::_accept(..,..)) 
 * which require a FieldVisitSelection to be specified. This is what this
 * enum is for.
 */
enum FieldVisitSelection {
    ALL,
    ALL_SET,
    ALL_SET_NONTRANSIENT
};

} /* namespace mgen */

#endif /* MGEN_FIELD_H_ */
