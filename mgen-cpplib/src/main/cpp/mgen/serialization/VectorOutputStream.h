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

class VectorOutputStream {
public:

    VectorOutputStream(std::vector<char>& data) :
            m_data(data) {
    }

    void write(const void* src, const int nBytes) {
        m_data.insert(
                m_data.end(),
                reinterpret_cast<const char*>(src),
                reinterpret_cast<const char*>(src) + nBytes);
    }

    void reset() {
        m_data.clear();
    }

private:
    std::vector<char>& m_data;

};

} /* namespace mgen */

#endif /* MGEN_VECTOROUTPUTSTREAM_H_ */
