/*
 * UnexpectedTypeException.h
 *
 *  Created on: 4 mar 2014
 *      Author: GiGurra
 */

#ifndef MGEN_UNEXPECTED_TYPE_EXCEPTION_H_
#define MGEN_UNEXPECTED_TYPE_EXCEPTION_H_

#include "mgen/exceptions/SerializationException.h"

namespace mgen {

class UnexpectedTypeException: public SerializationException {
public:
	UnexpectedTypeException(const std::string& msg = "") :
        SerializationException(msg) {
    }
};

} /* namespace mgen */

#endif /* MGEN_UNEXPECTED_TYPE_EXCEPTION_H_ */
