/*
 * NonCopyable.h
 *
 *  Created on: 3 mar 2014
 *      Author: GiGurra
 */

#ifndef NONCOPYABLE_H_
#define NONCOPYABLE_H_

namespace mgen {

class NonCopyable {
public:
    NonCopyable() {}
	~NonCopyable() {}

private:
	NonCopyable(const NonCopyable& other);
	NonCopyable& operator=(const NonCopyable& other);
};

} /* namespace mgen */

#endif /* NONCOPYABLE_H_ */
