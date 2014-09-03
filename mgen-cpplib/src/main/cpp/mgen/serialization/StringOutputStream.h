#ifndef MGEN_STRINGOUTPUTSTREAM_H_
#define MGEN_STRINGOUTPUTSTREAM_H_

#include <string>

namespace mgen {

/**
 * Represents an in-memory data output for MGen writers. A StringOutputStream
 * wraps a provided std::string without making a copy, or allocates its own string 
 * (depending on choice of constructor). When data is written to this 
 * StringOutputStream it is added to the underlying string.
 */
class StringOutputStream {
public:

    /**
     * Wraps a provided string in this StringOutputStream. No copy is made,
     * so the caller is responsible for keeping the string alive for as long
     * as this StringOutputStream exists.
     */
    StringOutputStream(std::string& output) :
            m_internalString(0),
            m_writeTarget(&output) {
    }

    /**
     * Creates a StringOutputStream around an internally allocated std::string.
     */
    StringOutputStream() :
            m_writeTarget(&m_internalString) {
    }
    
    /**
     * Writes nBytes bytes to the underlying string. The bytes are pushed back
     * at the end of the underlying string.
     */
    void write(const void* src, const int nBytes) {
        str().insert(
                str().end(),
                reinterpret_cast<const char*>(src),
                reinterpret_cast<const char*>(src) + nBytes);
    }

    /**
     * Clears the underlying wrapped string (std::string::clear())
     */
    void reset() {
        str().clear();
    }
    
    /**
     * Gets the underlying string
     */
    std::string& str() {
        return *m_writeTarget;
    }

private:
    std::string m_internalString;
    std::string * m_writeTarget;
    
    StringOutputStream(const StringOutputStream&);
    StringOutputStream& operator=(const StringOutputStream&);

};

} /* namespace mgen */

#endif /* MGEN_STRINGOUTPUTSTREAM_H_ */
