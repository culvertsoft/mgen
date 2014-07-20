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

class SerializationException: public Exception {
public:
    SerializationException(const std::string& msg = "") :
            Exception(msg) {
    }
};

} /* namespace mgen */

#endif /* MGEN_SERIALIZATIONEXCEPTION_H_ */
