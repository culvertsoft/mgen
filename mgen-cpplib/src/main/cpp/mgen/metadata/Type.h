/*
 * Type.h
 *
 *  Created on: 3 mar 2014
 *      Author: GiGurra
 */

#ifndef TYPE_H_
#define TYPE_H_

#include <string>
#include <vector>
#include <map>
#include "mgen/classes/Polymorphic.h"

namespace mgen {

class MGenBase;

class Type {
public:

    enum ENUM {
        ENUM_ENUM,
        ENUM_BOOL,
        ENUM_INT8,
        ENUM_INT16,
        ENUM_INT32,
        ENUM_INT64,
        ENUM_FLOAT32,
        ENUM_FLOAT64,
        ENUM_STRING,
        ENUM_ARRAY,
        ENUM_LIST,
        ENUM_MAP,
        ENUM_CUSTOM,
        ENUM_UNKNOWN,
        ENUM_MGEN_BASE
    };

    enum TAG {
        TAG_BOOL = 0,
        TAG_INT8 = 1,
        TAG_INT16 = 2,
        TAG_INT32 = 3,
        TAG_INT64 = 4,
        TAG_FLOAT32 = 5,
        TAG_FLOAT64 = 6,
        TAG_STRING = 7,
        TAG_ARRAY = 8,
        TAG_LIST = 8,
        TAG_MAP = 9,
        TAG_CUSTOM = 10
    };

    Type(const ENUM enm, const TAG tag) :
            m_enum(enm), m_tag(tag) {
    }

    ENUM enm() const {
        return m_enum;
    }

    TAG tag() const {
        return m_tag;
    }

private:
    ENUM m_enum;
    TAG m_tag;

};

inline Type::TAG TAG_OF(const bool *) {
    return Type::TAG_BOOL;
}

inline Type::TAG TAG_OF(const char *) {
    return Type::TAG_INT8;
}

inline Type::TAG TAG_OF(const short *) {
    return Type::TAG_INT16;
}

inline Type::TAG TAG_OF(const int *) {
    return Type::TAG_INT32;
}

inline Type::TAG TAG_OF(const long long *) {
    return Type::TAG_INT64;
}

inline Type::TAG TAG_OF(const float *) {
    return Type::TAG_FLOAT32;
}

inline Type::TAG TAG_OF(const double *) {
    return Type::TAG_FLOAT64;
}

inline Type::TAG TAG_OF(const std::string *) {
    return Type::TAG_STRING;
}

template<typename T>
inline Type::TAG TAG_OF(const std::vector<T> *) {
    return Type::TAG_LIST;
}

template<typename K, typename V>
inline Type::TAG TAG_OF(const std::map<K, V> *) {
    return Type::TAG_MAP;
}

template<typename T>
inline Type::TAG TAG_OF(const Polymorphic<T> *) {
    return Type::TAG_CUSTOM;
}

inline Type::TAG TAG_OF(const MGenBase *) {
    return Type::TAG_CUSTOM;
}

} /* namespace mgen */

#endif /* TYPE_H_ */
