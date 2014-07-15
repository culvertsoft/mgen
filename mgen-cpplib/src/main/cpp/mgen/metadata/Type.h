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
        TAG_CUSTOM = 10,
        TAG_UNKNOWN = 10,
        TAG_MGENBASE = 10,

        TAG_BIT8 = 0x80,
        TAG_BIT7 = (0x80 >> 1),

        TAG_NULL_BIT = TAG_BIT8,
        TAG_EMPTY_BIT = TAG_BIT7,

        TAG_FALSE_BIT = TAG_BIT8,
        TAG_TRUE_BIT = TAG_BIT7,

        TAG_ZERO_BIT = TAG_BIT8
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

    static inline TAG TAG_OF(const bool) {
        return TAG_BOOL;
    }
    static inline TAG TAG_OF(const char) {
        return TAG_INT8;
    }
    static inline TAG TAG_OF(const short) {
        return TAG_INT16;
    }
    static inline TAG TAG_OF(const int) {
        return TAG_INT32;
    }
    static inline TAG TAG_OF(const long long) {
        return TAG_INT64;
    }
    static inline TAG TAG_OF(const float) {
        return TAG_FLOAT32;
    }
    static inline TAG TAG_OF(const double) {
        return TAG_FLOAT64;
    }
    static inline TAG TAG_OF(const std::string&) {
        return TAG_STRING;
    }
    template<typename T>
    static inline TAG TAG_OF(const std::vector<T>&) {
        return TAG_LIST;
    }
    template<typename K, typename V>
    static inline TAG TAG_OF(const std::map<K, V>&) {
        return TAG_MAP;
    }
    template<typename T>
    static inline TAG TAG_OF(const Polymorphic<T>&) {
        return TAG_CUSTOM;
    }
    static inline TAG TAG_OF(const MGenBase&) {
        return TAG_CUSTOM;
    }

private:
    ENUM m_enum;
    TAG m_tag;

};

} /* namespace mgen */

#endif /* TYPE_H_ */
