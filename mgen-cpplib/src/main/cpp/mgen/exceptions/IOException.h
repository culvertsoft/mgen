/*
 * IOException.h
 *
 *  Created on: 3 mar 2014
 *      Author: GiGurra
 */

#ifndef MGEN_IOEXCEPTION_H_
#define MGEN_IOEXCEPTION_H_

#include "mgen/exceptions/Exception.h"

namespace mgen {

class IOException: public Exception {
public:
    IOException(const std::string& msg = "") :
            Exception(msg) {
    }
};

} /* namespace mgen */

#endif /* MGEN_IOEXCEPTION_H_ */
