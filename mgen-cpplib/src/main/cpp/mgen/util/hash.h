/*
 * hash.h
 *
 *  Created on: 8 mar 2014
 *      Author: GiGurra
 */

#ifndef HASH_H_
#define HASH_H_

#include <string>
#include "mgen/util/crc.h"

namespace mgen {
namespace hash {

inline short calc16bit(const std::string& s);
inline int calc32bit(const std::string& s);

inline short calc16bit(const std::string& s) {
    return crc::calc16(s.data(), s.size());
}

inline int calc32bit(const std::string& s) {
    return crc::calc32(s.data(), s.size());
}

} /* namespace hash */
} /* namespace mgen */

#endif /* HASH_H_ */
