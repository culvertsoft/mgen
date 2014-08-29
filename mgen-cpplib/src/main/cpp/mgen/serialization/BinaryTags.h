#ifndef MGEN_BINARY_TAGS_H_
#define MGEN_BINARY_TAGS_H_

#include "mgen/classes/MGenBase.h"
#include "mgen/util/stringutil.h"

namespace mgen {

/**
 * Enum used by the MGen binary wire format to signal what kind of value 
 * is about to be written/read. These are stamped ahead of each value.
 * For repeated types like maps and lists, component tags are only prepended 
 * once ahead of the collection, and only if the collection is non-empty.
 */
enum BINARY_TAG {
    BINARY_TAG_BOOL = 0,
    BINARY_TAG_INT8 = 1,
    BINARY_TAG_INT16 = 2,
    BINARY_TAG_INT32 = 3,
    BINARY_TAG_INT64 = 4,
    BINARY_TAG_FLOAT32 = 5,
    BINARY_TAG_FLOAT64 = 6,
    BINARY_TAG_STRING = 7,
    BINARY_TAG_ARRAY = 8,
    BINARY_TAG_LIST = 8,
    BINARY_TAG_MAP = 9,
    BINARY_TAG_CUSTOM = 10
};

/**
 * A 'BINARY_TAG_OF(..)' function exists in the mgen namespace for
 * each supported data type, for the binary writers and readers to use.  
 * These are convenience functions to make writer/reader code easier.
 */
inline BINARY_TAG BINARY_TAG_OF(const bool *) {
    return BINARY_TAG_BOOL;
}

/**
 * A 'BINARY_TAG_OF(..)' function exists in the mgen namespace for
 * each supported data type, for the binary writers and readers to use.  
 * These are convenience functions to make writer/reader code easier.
 */
inline BINARY_TAG BINARY_TAG_OF(const char *) {
    return BINARY_TAG_INT8;
}

/**
 * A 'BINARY_TAG_OF(..)' function exists in the mgen namespace for
 * each supported data type, for the binary writers and readers to use.  
 * These are convenience functions to make writer/reader code easier.
 */
inline BINARY_TAG BINARY_TAG_OF(const short *) {
    return BINARY_TAG_INT16;
}

/**
 * A 'BINARY_TAG_OF(..)' function exists in the mgen namespace for
 * each supported data type, for the binary writers and readers to use.  
 * These are convenience functions to make writer/reader code easier.
 */
inline BINARY_TAG BINARY_TAG_OF(const int *) {
    return BINARY_TAG_INT32;
}

/**
 * A 'BINARY_TAG_OF(..)' function exists in the mgen namespace for
 * each supported data type, for the binary writers and readers to use.  
 * These are convenience functions to make writer/reader code easier.
 */
inline BINARY_TAG BINARY_TAG_OF(const long long *) {
    return BINARY_TAG_INT64;
}

/**
 * A 'BINARY_TAG_OF(..)' function exists in the mgen namespace for
 * each supported data type, for the binary writers and readers to use.  
 * These are convenience functions to make writer/reader code easier.
 */
inline BINARY_TAG BINARY_TAG_OF(const float *) {
    return BINARY_TAG_FLOAT32;
}

/**
 * A 'BINARY_TAG_OF(..)' function exists in the mgen namespace for
 * each supported data type, for the binary writers and readers to use.  
 * These are convenience functions to make writer/reader code easier.
 */
inline BINARY_TAG BINARY_TAG_OF(const double *) {
    return BINARY_TAG_FLOAT64;
}

/**
 * A 'BINARY_TAG_OF(..)' function exists in the mgen namespace for
 * each supported data type, for the binary writers and readers to use.  
 * These are convenience functions to make writer/reader code easier.
 */
inline BINARY_TAG BINARY_TAG_OF(const std::string *) {
    return BINARY_TAG_STRING;
}

/**
 * A 'BINARY_TAG_OF(..)' function exists in the mgen namespace for
 * each supported data type, for the binary writers and readers to use.  
 * These are convenience functions to make writer/reader code easier.
 */
template<typename T>
inline BINARY_TAG BINARY_TAG_OF(const std::vector<T> *) {
    return BINARY_TAG_LIST;
}

/**
 * A 'BINARY_TAG_OF(..)' function exists in the mgen namespace for
 * each supported data type, for the binary writers and readers to use.  
 * These are convenience functions to make writer/reader code easier.
 */
template<typename K, typename V>
inline BINARY_TAG BINARY_TAG_OF(const std::map<K, V> *) {
    return BINARY_TAG_MAP;
}

/**
 * A 'BINARY_TAG_OF(..)' function exists in the mgen namespace for
 * each supported data type, for the binary writers and readers to use.  
 * These are convenience functions to make writer/reader code easier.
 */
template<typename T>
inline BINARY_TAG BINARY_TAG_OF(const Polymorphic<T> *) {
    return BINARY_TAG_CUSTOM;
}

/**
 * A 'BINARY_TAG_OF(..)' function exists in the mgen namespace for
 * each supported data type, for the binary writers and readers to use.  
 * These are convenience functions to make writer/reader code easier.
 */
template<typename UserDefinedType>
inline BINARY_TAG BINARY_TAG_OF(const UserDefinedType * v) {
    return __is_enum(UserDefinedType) ? BINARY_TAG_STRING : BINARY_TAG_CUSTOM;
}

#define MGEN_CASE_ENUM_STR(name) case name: return MGEN_STRINGIFY(name);
/**
 * Convenience method for writers/readers to create decent error messages.
 */
inline std::string get_enum_name(const BINARY_TAG e) {
    switch (e) {
    MGEN_CASE_ENUM_STR(BINARY_TAG_BOOL)
    MGEN_CASE_ENUM_STR(BINARY_TAG_INT8)
    MGEN_CASE_ENUM_STR(BINARY_TAG_INT16)
    MGEN_CASE_ENUM_STR(BINARY_TAG_INT32)
    MGEN_CASE_ENUM_STR(BINARY_TAG_INT64)
    MGEN_CASE_ENUM_STR(BINARY_TAG_FLOAT32)
    MGEN_CASE_ENUM_STR(BINARY_TAG_FLOAT64)
    MGEN_CASE_ENUM_STR(BINARY_TAG_STRING)
    MGEN_CASE_ENUM_STR(BINARY_TAG_LIST)
    MGEN_CASE_ENUM_STR(BINARY_TAG_MAP)
    MGEN_CASE_ENUM_STR(BINARY_TAG_CUSTOM)
    default:
        return std::string("BINARY_TAG_UNKNOWN: ").append(toString(int(e)));
    }
}
#undef MGEN_CASE_ENUM_STR

} /* namespace mgen */

#endif /* MGEN_BINARY_TAGS_H_ */

