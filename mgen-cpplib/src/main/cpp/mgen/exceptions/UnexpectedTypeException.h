/*
 * UnexpectedTypeException.h
 *
 *  Created on: 4 mar 2014
 *      Author: GiGurra
 */

#ifndef UNEXPECTED_TYPE_EXCEPTION_H_
#define UNEXPECTED_TYPE_EXCEPTION_H_

#include "mgen/exceptions/SerializationException.h"

namespace mgen {

class UnexpectedTypeException: public SerializationException {
public:
	UnexpectedTypeException(const std::string& msg = "") :
        SerializationException(msg) {
    }
};

} /* namespace mgen */

#endif /* UNEXPECTED_TYPE_EXCEPTION_H_ */
