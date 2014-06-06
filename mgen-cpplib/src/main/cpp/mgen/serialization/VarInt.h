/*
 * VarInt.h
 *
 *  Created on: 4 mar 2014
 *      Author: GiGurra
 */

#ifndef VARINT_H_
#define VARINT_H_

#include "mgen/exceptions/StreamCorruptedException.h"

namespace mgen {
namespace varint {

template<typename Stream>
inline void writeByte(const char c, Stream& out);

template<typename Stream>
inline char readByte(Stream& in);

template<typename Stream>
inline void writeSigned64(long long value, Stream& out);

template<typename Stream>
inline void writeUnsigned64(long long value, Stream& out);

template<typename Stream>
inline void writeSigned32(int value, Stream& out);

template<typename Stream>
inline void writeUnsigned32(int value, Stream& out);

template<typename Stream>
inline long long readSigned64(Stream& in);

template<typename Stream>
inline long long readUnsigned64(Stream& in);

template<typename Stream>
inline int readSigned32(Stream& in);

template<typename Stream>
inline int readUnsigned32(Stream& in);

/********************************************************
 *
 *
 *
 *
 ********************************************************/

template<typename Stream>
inline void writeByte(const char c, Stream& out) {
    out.write(&c, 1);
}

template<typename Stream>
inline char readByte(Stream& in) {
    char out;
    in.read(&out, 1);
    return out;
}

template<typename Stream>
inline void writeSigned64(long long value, Stream& out) {
    writeUnsigned64((value << 1) ^ (value >> 63), out);
}

template<typename Stream>
inline void writeUnsigned64(long long value, Stream& out) {
    while ((value & 0xFFFFFFFFFFFFFF80LL) != 0LL) {
        writeByte((value & 0x7F) | 0x80, out);
        value >>= 7;
    }
    writeByte(value & 0x7F, out);
}

template<typename Stream>
inline void writeSigned32(int value, Stream& out) {
    writeUnsigned32((value << 1) ^ (value >> 31), out);
}

template<typename Stream>
inline void writeUnsigned32(int value, Stream& out) {
    while ((value & 0xFFFFFF80) != 0LL) {
        writeByte((value & 0x7F) | 0x80, out);
        value >>= 7;
    }
    writeByte(value & 0x7F, out);
}

template<typename Stream>
inline long long readSigned64(Stream& in) {
    long long raw = readUnsigned64(in);
    long long temp = (((raw << 63) >> 63) ^ raw) >> 1;
    return temp ^ (raw & (1LL << 63));
}

template<typename Stream>
inline long long readUnsigned64(Stream& in) {
    long long value = 0LL;
    int i = 0;
    long long b;
    while (((b = readByte(in)) & 0x80LL) != 0) {
        value |= (b & 0x7F) << i;
        i += 7;
        if (i >= 64)
            throw StreamCorruptedException("Varint::readUnsigned64 overflow.");
    }
    return value | (b << i);
}

template<typename Stream>
inline int readSigned32(Stream& in) {
    int raw = readUnsigned32(in);
    int temp = (((raw << 31) >> 31) ^ raw) >> 1;
    return temp ^ (raw & (1 << 31));
}

template<typename Stream>
inline int readUnsigned32(Stream& in) {
    int value = 0;
    int i = 0;
    int b;
    while (((b = readByte(in)) & 0x80) != 0) {
        value |= (b & 0x7F) << i;
        i += 7;
        if (i >= 36)
            throw SerializationException("Varint::readUnsigned32 overflow.");
    }
    return value | (b << i);
}

}
} /* namespace mgen */

#endif /* VARINT_H_ */
