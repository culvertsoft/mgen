/*
 * MGenException.h
 *
 *  Created on: Mar 3, 2014
 *      Author: johan
 */

#ifndef MGEN_MGENEXCEPTION_H_
#define MGEN_MGENEXCEPTION_H_

#include <stdexcept>

namespace mgen {

class Exception: public std::runtime_error {
public:
    Exception(const std::string& msg = "") :
        std::runtime_error(msg) {
    }
};

} /* namespace mgen */

#endif /* MGEN_MGENEXCEPTION_H_ */
