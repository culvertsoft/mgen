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

class Field {
public:

    Field(
            const short id,
            const std::string& name,
            const std::vector<std::string>& flags = std::vector<std::string>()) :
                    m_id(id),
                    m_name(name),
                    m_flags(flags),
                    m_required(false) {
        for (std::size_t i = 0; i < flags.size(); i++) {
            if (flags[i] == "required") {
                m_required = true;
                break;
            }
        }
    }

    short id() const {
        return m_id;
    }

    const std::string& name() const {
        return m_name;
    }

    const std::vector<std::string>& flags() const {
        return m_flags;
    }

    bool isRequired() const {
        return m_required;
    }

private:
    short m_id;
    std::string m_name;
    std::vector<std::string> m_flags;
    bool m_required;

};

enum FieldSetDepth {
    SHALLOW,
    DEEP
};

} /* namespace mgen */

#endif /* MGEN_FIELD_H_ */
