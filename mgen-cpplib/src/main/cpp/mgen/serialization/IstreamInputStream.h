#ifndef MGEN_ISTREAMINPUTSTREAM_H_
#define MGEN_ISTREAMINPUTSTREAM_H_

#include <istream>
#include <mgen/exceptions/IOException.h>

namespace mgen {

/**
 * An MGen data input wrapping a C++ std::istream
 */
class IstreamInputStream {
public:

    /**
     * Wraps a provided istream. No copy is made, so the caller is responsible 
     * for keeping the istream alive for as long as this StringInputStream exists.
     */
    IstreamInputStream(std::istream& input) :
            m_input(input) {
    }
    
    /**
     * Reads nBytes from the underlying istream.  If any of the istream's
     * error bits are set, an mgen::IOException is thrown.
     */
    void read(void* trg, const int nBytes) {
        m_input.read((char*)trg, nBytes);
        if (!m_input.good()) {
            if (m_input.eof())
                throw IOException("IstreamInputStream: eof() bit set");
            if (m_input.fail())
                throw IOException("IstreamInputStream: fail() bit set");
            if (m_input.bad())
                throw IOException("IstreamInputStream: bad() bit set");
        }
    }

private:
    std::istream& m_input;
    
    IstreamInputStream(const IstreamInputStream&);
    IstreamInputStream& operator=(const IstreamInputStream&);

};

} /* namespace mgen */

#endif /* MGEN_ISTREAMINPUTSTREAM_H_ */
