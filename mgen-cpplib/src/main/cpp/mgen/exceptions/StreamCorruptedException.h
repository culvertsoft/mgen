/*
 * StreamCorruptedException.h
 *
 *  Created on: 4 mar 2014
 *      Author: GiGurra
 */

#ifndef MGEN_STREAMCORRUPTEDEXCEPTION_H_
#define MGEN_STREAMCORRUPTEDEXCEPTION_H_

#include "mgen/exceptions/SerializationException.h"

namespace mgen {

class StreamCorruptedException: public SerializationException {
public:
    StreamCorruptedException(const std::string& msg = "") :
        SerializationException(msg) {
    }
};

} /* namespace mgen */

#endif /* MGEN_STREAMCORRUPTEDEXCEPTION_H_ */
