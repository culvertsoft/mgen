/*
 * endian.h
 *
 *  Created on: 4 mar 2014
 *      Author: GiGurra
 */

#ifndef ENDIAN_H_
#define ENDIAN_H_

namespace mgen {
namespace endian {

enum ByteOrder {
    MGEN_LITTLE_ENDIAN,
    MGEN_BIG_ENDIAN
};

inline ByteOrder getSystemByteOrder();
inline bool isSystemBigEndian();
inline bool isSystemLittleEndian();

inline unsigned short htons(const unsigned short v);
inline unsigned int htonl(const unsigned int v);
inline unsigned long long htonll(const unsigned long long v);

inline unsigned short ntohs(const unsigned short v);
inline unsigned int ntohl(const unsigned int v);
inline unsigned long long ntohll(const unsigned long long v);

inline char ntoh(const char v) {
    return v;
}
inline char hton(const char v) {
    return ntoh(v);
}
inline unsigned char ntoh(const unsigned char v) {
    return v;
}
inline unsigned char hton(const unsigned char v) {
    return ntoh(v);
}

inline unsigned short ntoh(const unsigned short v) {
    return ntohs(v);
}
inline unsigned short hton(const unsigned short v) {
    return ntoh(v);
}
inline short ntoh(const short v) {
    return ntohs(v);
}
inline short hton(const short v) {
    return ntoh(v);
}

inline unsigned int ntoh(const unsigned int v) {
    return ntohl(v);
}
inline unsigned int hton(const unsigned int v) {
    return ntoh(v);
}
inline int ntoh(const int v) {
    return ntohl(v);
}
inline int hton(const int v) {
    return ntoh(v);
}

inline unsigned long long ntoh(const unsigned long long v) {
    return ntohll(v);
}
inline unsigned long long hton(const unsigned long long v) {
    return ntoh(v);
}
inline long long ntoh(const long long v) {
    return ntohll(v);
}
inline long long hton(const long long v) {
    return ntoh(v);
}

inline float ntoh(const float v) {
    union un {
        float floatVal;
        unsigned int intVal;
    } un;
    un.floatVal = v;
    un.intVal = ntoh(un.intVal);
    return un.floatVal;
}

inline float hton(const float v) {
    return ntoh(v);
}

inline double ntoh(const double v) {
    union un {
        double doubleVal;
        unsigned long long longVal;
    } un;
    un.doubleVal = v;
    un.longVal = ntoh(un.longVal);
    return un.doubleVal;
}

inline double hton(const double v) {
    return ntoh(v);
}

/******************************************************************
 *
 *
 *                  IMPLEMENTATION
 *
 *****************************************************************/

#define BSWAP_16(x)     ((x << 8)  & 0xff00U) | \
                        ((x >> 8)  & 0x00ffU)

#define BSWAP_32(x)     ((x << 24) & 0xff000000U) | \
                        ((x << 8)  & 0x00ff0000U) | \
                        ((x >> 8)  & 0x0000ff00U) | \
                        ((x >> 24) & 0x000000ffU)

#define BSWAP_64(x)     ((x << 56) | \
                        ((x << 40) & 0xff000000000000ULL) | \
                        ((x << 24) & 0x00ff0000000000ULL) | \
                        ((x << 8)  & 0x0000ff00000000ULL) | \
                        ((x >> 8)  & 0x000000ff000000ULL) | \
                        ((x >> 24) & 0x00000000ff0000ULL) | \
                        ((x >> 40) & 0x0000000000ff00ULL) | \
                        ((x >> 56) & 0x00000000000ffULL))

inline ByteOrder findSystemByteOrder() {
    const int i = 1;
    return *reinterpret_cast<const char*>(&i) == 1 ? MGEN_LITTLE_ENDIAN : MGEN_BIG_ENDIAN;
}

inline ByteOrder getSystemByteOrder() {
    static const ByteOrder order = findSystemByteOrder();
    return order;
}

inline bool isSystemBigEndian() {
    static const bool b = getSystemByteOrder() == MGEN_BIG_ENDIAN;
    return b;
}

inline bool isSystemLittleEndian() {
    static const bool b = getSystemByteOrder() == MGEN_LITTLE_ENDIAN;
    return b;
}

inline unsigned long long htonll(const unsigned long long v) {
    if (isSystemLittleEndian()) {
        return BSWAP_64(v);
    } else {
        return v;
    }
}

inline unsigned long long ntohll(const unsigned long long v) {
    return htonll(v);
}

inline unsigned int htonl(const unsigned int v) {
    if (isSystemLittleEndian()) {
        return BSWAP_32(v);
    } else {
        return v;
    }
}

inline unsigned int ntohl(const unsigned int v) {
    return htonl(v);
}

inline unsigned short htons(const unsigned short v) {
    if (isSystemLittleEndian()) {
        return BSWAP_16(v);
    } else {
        return v;
    }
}

inline unsigned short ntohs(const unsigned short v) {
    return htons(v);
}

#undef BSWAP_16
#undef BSWAP_32
#undef BSWAP_64

}
}

#endif /* ENDIAN_H_ */
