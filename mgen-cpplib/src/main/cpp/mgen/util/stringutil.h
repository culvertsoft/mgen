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
#include <cstdlib>

namespace mgen {

#ifndef MGEN_STRINGIFY
#define MGEN_STRINGIFY2(expr) #expr
#define MGEN_STRINGIFY(expr) MGEN_STRINGIFY2(expr)
#endif //STRINGIFY

/************************************************
 *
 *
 *          FROM T -> STRING
 *
 ************************************************/

template<typename EnumType>
inline std::string toString(const EnumType e) {
    return get_enum_name(e);
}

inline std::string toString(const bool b) {
    return b ? "true" : "false";
}

inline std::string toString(const char s) {
    std::stringstream trg;
    trg << s;
    return trg.str();
}

inline std::string toString(const short s) {
    std::stringstream trg;
    trg << s;
    return trg.str();
}

inline std::string toString(const int s) {
    std::stringstream trg;
    trg << s;
    return trg.str();
}

inline std::string toString(const long long s) {
    std::stringstream trg;
    trg << s;
    return trg.str();
}

inline std::string toString(const float s) {
    std::stringstream trg;
    trg << s;
    return trg.str();
}

inline std::string toString(const double s) {
    std::stringstream trg;
    trg << s;
    return trg.str();
}

inline std::string toString(const char * s) {
    return std::string(s);
}

inline std::string toString(const std::string& s) {
    return s;
}

template<typename T>
inline std::string toString(const std::vector<T>& v) {
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

/************************************************
 *
 *
 *          FROM STRING -> T
 *
 ************************************************/

template<typename EnumType>
inline EnumType fromString(const std::string& s) {
    return get_enum_value(EnumType(), s);
}

template<>
inline bool fromString(const std::string& s) {
    return !s.empty() && (s[0] == 't' || s[0] == 'T');
}

template<>
inline char fromString(const std::string& s) {
    return (char) std::atoi(s.c_str());
}

template<>
inline short fromString(const std::string& s) {
    return (short) std::atoi(s.c_str());
}

template<>
inline int fromString(const std::string& s) {
    return std::atoi(s.c_str());
}

template<>
inline long long fromString(const std::string& s) {
    return std::atoll(s.c_str());
}

template<>
inline float fromString(const std::string& s) {
    return (float) std::atof(s.c_str());
}

template<>
inline double fromString(const std::string& s) {
    return (double) std::atof(s.c_str());
}

template<>
inline std::string fromString(const std::string& s) {
    return s;
}

template<typename T>
T fromString(const char * s) {
    return fromString<T>(std::string(s));
}

} /* namespace mgen */

#endif /* STRINGUTIL_H_ */
