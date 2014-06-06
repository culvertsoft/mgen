/*
 * TypeConflictException.h
 *
 *  Created on: Mar 3, 2014
 *      Author: johan
 */

#ifndef TYPECONFLICTEXCEPTION_H_
#define TYPECONFLICTEXCEPTION_H_

#include "mgen/exceptions/Exception.h"

namespace mgen {

class TypeConflictException: public Exception {
public:
    TypeConflictException(const std::string& msg = "") :
            Exception(msg) {
    }
};

} /* namespace mgen */

#endif /* TYPECONFLICTEXCEPTION_H_ */
