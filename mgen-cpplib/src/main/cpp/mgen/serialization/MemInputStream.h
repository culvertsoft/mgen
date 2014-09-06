/*
 * MemInputStream.h
 *
 *  Created on: 28 aug 2014
 *      Author: GiGurra
 */

#ifndef MGEN_MEMINPUTSTREAM_H_
#define MGEN_MEMINPUTSTREAM_H_

#include <vector>
#include <cstring>
#include <mgen/exceptions/IOException.h>

namespace mgen {

/**
 * Represents a generic in-memory input source for MGen readers. MemInputStream wraps
 * a provided data source without copying its contents. It differs from e.g. VectorInputStream
 * in that MemInputStream allows you to swap the internal data source pointer
 * after construction, but does not expect you to change the contents or size of its 
 * input buffer externally.
 */
class MemInputStream {
public:

    /**
     * Wraps the provided vector. The bytes are not copied. The vector size is 
     * expected not to change. 
     */
    MemInputStream(const std::vector<char>& input, const int offset = 0) :
            m_input(&input[0]), 
            m_size(input.size()),
            m_offset(offset) {
    }
    
    /**
     * Wraps the provided string. The bytes are not copied. The string size is 
     * expected not to change. 
     */
    MemInputStream(const std::string& input, const int offset = 0) :
            m_input(&input[0]), 
            m_size(input.size()),
            m_offset(offset) {
    }
    
    /**
     * Wraps the provided pointer. The bytes are not copied.
     */
    MemInputStream(const char * input, const int size, const int offset = 0) :
            m_input(input), 
            m_size(size),
            m_offset(offset) {
    }
    
    /**
     * Wraps the provided pointer. The bytes are not copied.
     */
    MemInputStream& setInput(const char * input, const int size, const int offset = 0) { 
        m_input = input;
        m_size = size;
        m_offset = offset;
		return *this;
    }
    
    /**
     * Wraps the provided string. The bytes are not copied.
     */
    MemInputStream& setInput(const std::string& input, const int offset = 0) { 
        return setInput(&input[0], input.size(), offset);
    }
    
    /**
     * Wraps the provided vector. The bytes are not copied.
     */
    MemInputStream& setInput(const std::vector<char>& input, const int offset = 0) {
        return setInput(&input[0], input.size(), offset);
    }
    
    /**
     * The input stream keeps an internal count of how many bytes
     * have been read so far from its input source. Calling this method
     * resets this count to zero. Subsequently, any read calls made
     * after calling reset() will start reading the first bytes again 
     * (at offset 0).
     */
    MemInputStream& reset() {
        m_offset = 0;
		return *this;
    }

    /**
     * Copies nBytes from the internal buffer to the provided target buffer.
     * If less bytes are available than what is requested, an IOException
     * is thrown.
     */
    void read(void* trg, const int nBytes) {
        const int nBytesLeft = m_size - m_offset;
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
            throw IOException("MemInputStream::read: Data source underflow");
        }
    }

private:
    const char * m_input;
    int m_size;
    int m_offset;

};

} /* namespace mgen */

#endif /* MGEN_MEMINPUTSTREAM_H_ */
