/*
 * VectorInputStream.h
 *
 *  Created on: 3 mar 2014
 *      Author: GiGurra
 */

#ifndef MGEN_VECTORINPUTSTREAM_H_
#define MGEN_VECTORINPUTSTREAM_H_

#include <vector>
#include <cstring>
#include <mgen/exceptions/IOException.h>

namespace mgen {

/**
 * Represents an in-memory input source for MGen readers. VectorInputStream wraps
 * a provided data source without copying its contents. The input source cannot
 * be replaced after construction, however you may externally modify the input 
 * source (add more bytes to it, clear it, etc) and this VectorInputStream will
 * pick up the changes you make.
 */
class VectorInputStream {
public:

    /**
     * Wraps the provided vector. The bytes are not copied. The vector size 
     * and contents can change after construction, and this VectorInputStream 
     * will pick up that change.
     */
    VectorInputStream(const std::vector<char>& input, const int offset = 0) :
            m_input(input), m_offset(offset) {
    }

    /**
     * The input stream keeps an internal count of how many bytes
     * have been read so far from its input source. Calling this method
     * resets this count to zero. Subsequently, any read calls made
     * after calling reset() will start reading the first bytes again 
     * (at offset 0).
     */
    VectorInputStream& reset() {
        m_offset = 0;
		return *this;
    }

    /**
     * Copies nBytes from the internal buffer to the provided target buffer.
     * If less bytes are available than what is requested, an IOException
     * is thrown.
     */
    void read(void* trg, const int nBytes) {
        const int nBytesLeft = m_input.size() - m_offset;
        if (nBytes <= nBytesLeft) {
            // This memcpy check improves VS c++ performance
            // by 20% for mgen.......for some reason..
            if (nBytes > 32) {
                std::memcpy(trg, &m_input[m_offset], nBytes);
            } else {
                char * charTrg = reinterpret_cast<char*>(trg);
                for (int i = 0; i < nBytes; i++) {
                    charTrg[i] = m_input[m_offset + i];
                }
            }
            m_offset += nBytes;
        } else {
            throw IOException("VectorInputStream::read: Data source underflow");
        }
    }

private:
    const std::vector<char>& m_input;
    int m_offset;

};

} /* namespace mgen */

#endif /* MGEN_VECTORINPUTSTREAM_H_ */
