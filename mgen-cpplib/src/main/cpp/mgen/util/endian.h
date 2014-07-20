/*
 * endian.h
 *
 *  Created on: 4 mar 2014
 *      Author: GiGurra
 */

#ifndef MGEN_ENDIAN_H_
#define MGEN_ENDIAN_H_

namespace mgen {
namespace endian {

enum MGenByteOrder {
    MGEN_LITTLE_ENDIAN,
    MGEN_BIG_ENDIAN
};

inline MGenByteOrder mgen_getSystemByteOrder();
inline bool mgen_isSystemBigEndian();
inline bool mgen_isSystemLittleEndian();

inline unsigned short mgen_htons(const unsigned short v);
inline unsigned int mgen_htonl(const unsigned int v);
inline unsigned long long mgen_htonll(const unsigned long long v);

inline unsigned short mgen_ntohs(const unsigned short v);
inline unsigned int mgen_ntohl(const unsigned int v);
inline unsigned long long mgen_ntohll(const unsigned long long v);

inline char mgen_ntoh(const char v) {
    return v;
}
inline char mgen_hton(const char v) {
    return mgen_ntoh(v);
}
inline unsigned char mgen_ntoh(const unsigned char v) {
    return v;
}
inline unsigned char mgen_hton(const unsigned char v) {
    return mgen_ntoh(v);
}

inline unsigned short mgen_ntoh(const unsigned short v) {
    return mgen_ntohs(v);
}
inline unsigned short mgen_hton(const unsigned short v) {
    return mgen_ntoh(v);
}
inline short mgen_ntoh(const short v) {
    return mgen_ntohs(v);
}
inline short mgen_hton(const short v) {
    return mgen_ntoh(v);
}

inline unsigned int mgen_ntoh(const unsigned int v) {
    return mgen_ntohl(v);
}
inline unsigned int mgen_hton(const unsigned int v) {
    return mgen_ntoh(v);
}
inline int mgen_ntoh(const int v) {
    return mgen_ntohl(v);
}
inline int mgen_hton(const int v) {
    return mgen_ntoh(v);
}

inline unsigned long long mgen_ntoh(const unsigned long long v) {
    return mgen_ntohll(v);
}
inline unsigned long long mgen_hton(const unsigned long long v) {
    return mgen_ntoh(v);
}
inline long long mgen_ntoh(const long long v) {
    return mgen_ntohll(v);
}
inline long long mgen_hton(const long long v) {
    return mgen_ntoh(v);
}

inline float mgen_ntoh(const float v) {
    union un {
        float floatVal;
        unsigned int intVal;
    } un;
    un.floatVal = v;
    un.intVal = mgen_ntoh(un.intVal);
    return un.floatVal;
}

inline float mgen_hton(const float v) {
    return mgen_ntoh(v);
}

inline double mgen_ntoh(const double v) {
    union un {
        double doubleVal;
        unsigned long long longVal;
    } un;
    un.doubleVal = v;
    un.longVal = mgen_ntoh(un.longVal);
    return un.doubleVal;
}

inline double mgen_hton(const double v) {
    return mgen_ntoh(v);
}

/******************************************************************
 *
 *
 *                  IMPLEMENTATION
 *
 *****************************************************************/

#define MGEN_BSWAP_16(x)     ((x << 8)  & 0xff00U) | \
                             ((x >> 8)  & 0x00ffU)

#define MGEN_BSWAP_32(x)     ((x << 24) & 0xff000000U) | \
                             ((x << 8)  & 0x00ff0000U) | \
                             ((x >> 8)  & 0x0000ff00U) | \
                             ((x >> 24) & 0x000000ffU)

#define MGEN_BSWAP_64(x)     ((x << 56) | \
                             ((x << 40) & 0xff000000000000ULL) | \
                             ((x << 24) & 0x00ff0000000000ULL) | \
                             ((x << 8)  & 0x0000ff00000000ULL) | \
                             ((x >> 8)  & 0x000000ff000000ULL) | \
                             ((x >> 24) & 0x00000000ff0000ULL) | \
                             ((x >> 40) & 0x0000000000ff00ULL) | \
                             ((x >> 56) & 0x00000000000ffULL))

inline MGenByteOrder mgen_findSystemByteOrder() {
    const int i = 1;
    return *reinterpret_cast<const char*>(&i) == 1 ? MGEN_LITTLE_ENDIAN : MGEN_BIG_ENDIAN;
}

inline MGenByteOrder mgen_getSystemByteOrder() {
    static const MGenByteOrder order = mgen_findSystemByteOrder();
    return order;
}

inline bool mgen_isSystemBigEndian() {
    static const bool b = mgen_getSystemByteOrder() == MGEN_BIG_ENDIAN;
    return b;
}

inline bool mgen_isSystemLittleEndian() {
    static const bool b = mgen_getSystemByteOrder() == MGEN_LITTLE_ENDIAN;
    return b;
}

inline unsigned long long mgen_htonll(const unsigned long long v) {
    if (mgen_isSystemLittleEndian()) {
        return MGEN_BSWAP_64(v);
    } else {
        return v;
    }
}

inline unsigned long long mgen_ntohll(const unsigned long long v) {
    return mgen_htonll(v);
}

inline unsigned int mgen_htonl(const unsigned int v) {
    if (mgen_isSystemLittleEndian()) {
        return MGEN_BSWAP_32(v);
    } else {
        return v;
    }
}

inline unsigned int mgen_ntohl(const unsigned int v) {
    return mgen_htonl(v);
}

inline unsigned short mgen_htons(const unsigned short v) {
    if (mgen_isSystemLittleEndian()) {
        return MGEN_BSWAP_16(v);
    } else {
        return v;
    }
}

inline unsigned short mgen_ntohs(const unsigned short v) {
    return mgen_htons(v);
}

#undef MGEN_BSWAP_16
#undef MGEN_BSWAP_32
#undef MGEN_BSWAP_64

}
}

#endif /* MGEN_ENDIAN_H_ */
