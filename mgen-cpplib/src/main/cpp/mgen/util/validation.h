/*
 * Marker.h
 *
 *  Created on: 21 mar 2014
 *      Author: GiGurra
 */

#ifndef MARKER_H_
#define MARKER_H_

#include "mgen/classes/MGenBase.h"

namespace mgen {
namespace validation {

/********************************************************
 *
 *
 *                  FIELD VALIDATORS
 *
 *******************************************************/

inline bool validateFieldDeep(const bool v);
inline bool validateFieldDeep(const char v);
inline bool validateFieldDeep(const short v);
inline bool validateFieldDeep(const int v);
inline bool validateFieldDeep(const long long v);
inline bool validateFieldDeep(const float v);
inline bool validateFieldDeep(const double v);
inline bool validateFieldDeep(const std::string& v);
inline bool validateFieldDeep(const MGenBase& v);
template<typename T>
inline bool validateFieldDeep(const Polymorphic<T>& v);
template<typename T>
inline bool validateFieldDeep(const std::vector<T>& v);
template<typename K, typename V>
inline bool validateFieldDeep(const std::map<K, V>& v);

template<typename T>
inline bool validateFieldDeep(const Polymorphic<T>& v) {
    return !v.get() || validateFieldDeep(*v);
}

template<typename T>
inline bool validateFieldDeep(const std::vector<T>& v) {
    typedef typename std::vector<T>::const_iterator It;
    for (It it = v.begin(); it != v.end(); it++) {
        if (!validateFieldDeep(*it)) {
            return false;
        }
    }
    return true;
}

template<typename K, typename V>
inline bool validateFieldDeep(const std::map<K, V>& v) {
    typedef typename std::map<K, V>::const_iterator It;
    for (It it = v.begin(); it != v.end(); it++) {
        if (!validateFieldDeep(it->first) || !validateFieldDeep(it->second)) {
            return false;
        }
    }
    return true;
}

/********************************************************
 *
 *
 *                  FIELD SETTERS
 *
 *******************************************************/

inline void setFieldSetDeep(bool v);
inline void setFieldSetDeep(char v);
inline void setFieldSetDeep(short v);
inline void setFieldSetDeep(int v);
inline void setFieldSetDeep(long long v);
inline void setFieldSetDeep(float v);
inline void setFieldSetDeep(double v);
inline void setFieldSetDeep(std::string& v);
inline void setFieldSetDeep(MGenBase& v);
template<typename T>
inline void setFieldSetDeep(Polymorphic<T>& v);
template<typename T>
inline void setFieldSetDeep(std::vector<T>& v);
template<typename K, typename V>
inline void setFieldSetDeep(std::map<K, V>& v);

template<typename T>
inline void setFieldSetDeep(Polymorphic<T>& v) {
    if (v.get())
        setFieldSetDeep(*v);
}

template<typename T>
inline void setFieldSetDeep(std::vector<T>& v) {
    typedef typename std::vector<T>::iterator It;
    for (It it = v.begin(); it != v.end(); it++)
        setFieldSetDeep(*it);
}

template<typename K, typename V>
inline void setFieldSetDeep(std::map<K, V>& v) {
    typedef typename std::map<K, V>::iterator It;
    for (It it = v.begin(); it != v.end(); it++) {
        setFieldSetDeep(it->first);
        setFieldSetDeep(it->second);
    }
}

/*************************************************************
 *
 *
 *          IMPLEMENTATION
 *
 *************************************************************/

inline bool validateFieldDeep(const MGenBase& v) {
    return v._validate(DEEP);
}

inline bool validateFieldDeep(const bool v) {
    return true;
}

inline bool validateFieldDeep(const char v) {
    return true;
}

inline bool validateFieldDeep(const short v) {
    return true;
}

inline bool validateFieldDeep(const int v) {
    return true;
}

inline bool validateFieldDeep(const long long v) {
    return true;
}

inline bool validateFieldDeep(const float v) {
    return true;
}

inline bool validateFieldDeep(const double v) {
    return true;
}

inline bool validateFieldDeep(const std::string& v) {
    return true;
}

inline void setFieldSetDeep(bool v) {
}

inline void setFieldSetDeep(char v) {
}

inline void setFieldSetDeep(short v) {
}

inline void setFieldSetDeep(int v) {
}

inline void setFieldSetDeep(long long v) {
}

inline void setFieldSetDeep(float v) {
}

inline void setFieldSetDeep(double v) {
}

inline void setFieldSetDeep(std::string& v) {
}

inline void setFieldSetDeep(MGenBase& v) {
    v._setAllFieldsSet(true, DEEP);
}

} /* namespace validation */
} /* namespace mgen */

#endif /* MARKER_H_ */
