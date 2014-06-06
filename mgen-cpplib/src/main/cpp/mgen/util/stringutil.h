/*
 * stringutil.h
 *
 *  Created on: Mar 3, 2014
 *      Author: johan
 */

#ifndef STRINGUTIL_H_
#define STRINGUTIL_H_

#include <string>
#include <sstream>

namespace mgen {

#ifndef STRINGIFY
#define STRINGIFY2(expr) #expr
#define STRINGIFY(expr) STRINGIFY2(expr)
#endif //STRINGIFY

//Converts std::ostream to std::string
inline std::string streamToString(const std::ostream& src) {
    std::stringstream trg;
    trg << src.rdbuf();
    return trg.str();
}

//Helper macro for creating a string from an expression, C++ stream style.
#define TO_STRING(expression) streamToString((std::ostringstream() << "") << expression)

} /* namespace mgen */

#endif /* STRINGUTIL_H_ */
