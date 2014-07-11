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
#include <vector>

namespace mgen {

#ifndef MGEN_STRINGIFY
#define MGEN_STRINGIFY2(expr) #expr
#define MGEN_STRINGIFY(expr) MGEN_STRINGIFY2(expr)
#endif //STRINGIFY

template<typename T>
std::string toString(const T& t) {
	std::stringstream trg;
	trg << t;
	return trg.str();
}

template<typename T>
std::string toString(const std::vector<T>& v) {
	std::stringstream trg;
	trg << "[";
	for (int i = 0; i < int(v.size()); i++) {
		if (i >= 1)
			trg << ", ";
		trg << toString(v[i]);
	}
	trg << "]";
	return trg.str();
}

} /* namespace mgen */

#endif /* STRINGUTIL_H_ */
