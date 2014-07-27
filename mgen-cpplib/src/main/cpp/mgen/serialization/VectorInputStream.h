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
#include "mgen/exceptions/IOException.h"

namespace mgen {

class VectorInputStream {
public:

    VectorInputStream(const std::vector<char>& input, const int offset = 0) :
            m_input(input), m_offset(offset) {
    }

    void reset() {
        m_offset = 0;
    }

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
