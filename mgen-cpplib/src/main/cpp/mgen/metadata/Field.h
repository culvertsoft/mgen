/*
 * Field.h
 *
 *  Created on: 3 mar 2014
 *      Author: GiGurra
 */

#ifndef FIELD_H_
#define FIELD_H_

#include "mgen/metadata/Type.h"

namespace mgen {

class Field {
public:

    Field(
            const short hash16bit,
            const std::string& name,
            const Type type,
            const std::vector<std::string>& flags) :
                    m_hash16bit(hash16bit),
                    m_name(name),
                    m_type(type),
                    m_flags(flags),
                    m_required(false) {
        for (std::size_t i = 0; i < flags.size(); i++) {
            if (flags[i] == "required") {
                m_required = true;
                break;
            }
        }
    }

    short hash16bit() const {
        return m_hash16bit;
    }

    const std::string& name() const {
        return m_name;
    }

    const Type& type() const {
        return m_type;
    }

    const std::vector<std::string>& flags() const {
        return m_flags;
    }

    bool isRequired() const {
        return m_required;
    }

private:
    short m_hash16bit;
    std::string m_name;
    Type m_type;
    std::vector<std::string> m_flags;
    bool m_required;

};

} /* namespace mgen */

#endif /* FIELD_H_ */
