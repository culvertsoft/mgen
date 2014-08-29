/*
 * SerializationException.h
 *
 *  Created on: 4 mar 2014
 *      Author: GiGurra
 */

#ifndef MGEN_SERIALIZATIONEXCEPTION_H_
#define MGEN_SERIALIZATIONEXCEPTION_H_

#include "mgen/exceptions/Exception.h"

namespace mgen {

/**
 * A base class for exceptions that occur in the serialization
 * logic of MGen.
 */
class SerializationException: public Exception {
public:
    SerializationException(const std::string& msg = "") :
            Exception(msg) {
    }
};

} /* namespace mgen */

#endif /* MGEN_SERIALIZATIONEXCEPTION_H_ */
