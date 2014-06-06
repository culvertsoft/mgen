/*
 * SerializationException.h
 *
 *  Created on: 4 mar 2014
 *      Author: GiGurra
 */

#ifndef SERIALIZATIONEXCEPTION_H_
#define SERIALIZATIONEXCEPTION_H_

#include "mgen/exceptions/Exception.h"

namespace mgen {

class SerializationException: public Exception {
public:
    SerializationException(const std::string& msg = "") :
            Exception(msg) {
    }
};

} /* namespace mgen */

#endif /* SERIALIZATIONEXCEPTION_H_ */
