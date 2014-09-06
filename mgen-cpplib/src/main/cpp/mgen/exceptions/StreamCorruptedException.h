/*
 * StreamCorruptedException.h
 *
 *  Created on: 4 mar 2014
 *      Author: GiGurra
 */

#ifndef MGEN_STREAMCORRUPTEDEXCEPTION_H_
#define MGEN_STREAMCORRUPTEDEXCEPTION_H_

#include <mgen/exceptions/SerializationException.h>

namespace mgen {

/**
 * Signals that unexpected data was read when reading data streams,
 * for example an array length was expected but a negative number was read.
 */
class StreamCorruptedException: public SerializationException {
public:
    StreamCorruptedException(const std::string& msg = "") :
        SerializationException(msg) {
    }
};

} /* namespace mgen */

#endif /* MGEN_STREAMCORRUPTEDEXCEPTION_H_ */
