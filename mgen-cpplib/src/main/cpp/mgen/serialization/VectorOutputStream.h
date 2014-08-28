/*
 * VectorOutputStream.h
 *
 *  Created on: 3 mar 2014
 *      Author: GiGurra
 */

#ifndef MGEN_VECTOROUTPUTSTREAM_H_
#define MGEN_VECTOROUTPUTSTREAM_H_

#include <vector>
#include "mgen/exceptions/IOException.h"

namespace mgen {

/**
 * Represents an in-memory data output for MGen writers. VectorOutputStream wraps
 * a provided std::vector<char> without making a copy. When data is written to this 
 * VectorOutputStream, the bytes are pushed back to the underlying wrapped vector.
 */
class VectorOutputStream {
public:

    /**
     * Wraps a provided vector in this VectorOutputStream. No copy is made,
     * so the caller is responsible for keeping the wrapped vector alive for as long
     * as this VectorOutputStream exists.
     */
    VectorOutputStream(std::vector<char>& data) :
            m_data(data) {
    }

    /**
     * Writes nBytes bytes to the underlying wrapped vector. The bytes are pushed back
     * at the end of the underlying wrapped vector.
     */
    void write(const void* src, const int nBytes) {
        m_data.insert(
                m_data.end(),
                reinterpret_cast<const char*>(src),
                reinterpret_cast<const char*>(src) + nBytes);
    }

    /**
     * Clears the underlying wrapped vector (std::vector<char>::clear()).
     */
    void reset() {
        m_data.clear();
    }

private:
    std::vector<char>& m_data;

};

} /* namespace mgen */

#endif /* MGEN_VECTOROUTPUTSTREAM_H_ */
