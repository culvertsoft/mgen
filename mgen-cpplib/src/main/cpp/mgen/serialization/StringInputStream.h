/*
 * StringInputStream.h
 *
 *  Created on: 5 sep 2014
 *      Author: GiGurra
 */

#ifndef MGEN_STRINGINPUTSTREAM_H_
#define MGEN_STRINGINPUTSTREAM_H_

#include <mgen/serialization/MemInputStream.h>

namespace mgen {

/**
 * StringInputStream is an alias for MemInputStream, which can be used to wrap strings
 * as data input sources for reading MGen objects from.
 */
class StringInputStream : public MemInputStream {
    typedef MemInputStream super;

public:
    
    /**
     * See MemInputStream::MemInputStream(const std::string&)
     */
    StringInputStream(const std::string& input, const int offset = 0) :
            super(input, offset) {
    }
    
    /**
     * See MemInputStream::MemInputStream(const char *, const int, const int)
     */
    StringInputStream(const char * input, const int size, const int offset = 0) :
            super(input, size, offset) {
    }
    
    /**
     * See MemInputStream::setInput(const std::string&, const int)
     */
    StringInputStream& setInput(const std::string& input, const int offset = 0) { 
        super::setInput(input, offset);
        return *this;
    }
    
    /**
     * See MemInputStream::setInput(const char *, const int, const int)
     */
    StringInputStream& setInput(const char * input, const int size, const int offset = 0) {
        super::setInput(input, size, offset);
		return *this;
    }
    
    /**
     * See MemInputStream::reset()
     */
    StringInputStream& reset() {
        super::reset();
		return *this;
    }


};

} /* namespace mgen */

#endif /* MGEN_STRINGINPUTSTREAM_H_ */
