#ifndef MGEN_OSTREAMOUTPUTSTREAM_H_
#define MGEN_OSTREAMOUTPUTSTREAM_H_

#include <ostream>
#include <mgen/exceptions/IOException.h>

namespace mgen {

/**
 * An MGen data output wrapping a C++ std::ostream. Although std istreams and std::ostreams
 * work fine with MGen, they do not throw any exceptions. Using this wrapper will cause exceptions
 * to be thrown, instead of having to check the error bits of the istream or ostream.
 */
class OstreamOutputStream {
public:

    /**
     * Wraps a provided ostream. No copy is made, so the caller is responsible 
     * for keeping the ostream alive for as long as this OstreamOutputStream exists.
     */
    OstreamOutputStream(std::ostream& output) :
            m_output(output) {
    }
    
    /**
     * Writes nBytes bytes to the underlying ostream. If any of the ostream's
     * error bits are set, an mgen::IOException is thrown.
     */
    void write(const void* src, const int nBytes) {
        m_output.write((const char*)src, nBytes);
        if (!m_output.good()) {
            if (m_output.eof())
                throw IOException("OstreamOutputStream: eof() bit set");
            if (m_output.fail())
                throw IOException("OstreamOutputStream: fail() bit set");
            if (m_output.bad())
                throw IOException("OstreamOutputStream: bad() bit set");
        }
    }

private:
    std::ostream& m_output;
    
    OstreamOutputStream(const OstreamOutputStream&);
    OstreamOutputStream& operator=(const OstreamOutputStream&);

};

} /* namespace mgen */

#endif /* MGEN_OSTREAMOUTPUTSTREAM_H_ */
