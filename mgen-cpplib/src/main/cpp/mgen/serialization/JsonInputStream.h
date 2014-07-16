#ifndef MGEN_JSON_INPUT_STREAM
#define MGEN_JSON_INPUT_STREAM

namespace mgen {
namespace internal {

template<typename InputStreamType>
class JsonInStream {
public:

    JsonInStream(InputStreamType * stream) :
                    m_nRead(0),
                    m_buf(0x00),
                    m_peeked(false),
                    m_stream(stream) {
    }

    char Peek() const {
        if (!m_peeked) {
            m_buf = const_cast<JsonInStream&>(*this).readByte();
            m_peeked = true;
        }
        return m_buf;
    }

    char Take() {
        m_nRead++;
        if (m_peeked) {
            m_peeked = false;
            return m_buf;
        } else {
            return readByte();
        }
    }

    std::size_t Tell() {
        return m_nRead;
    }

    char * PutBegin() {
        throw SerializationException("JsonInStream::PutBegin(): BUG: Should not be called!");
    }

    void Put(const char c) {
        throw SerializationException("JsonInStream::Put(): BUG: Should not be called!");
    }

    size_t PutEnd(char * begin) {
        throw SerializationException("JsonInStream::PutEnd(): BUG: Should not be called!");
    }

private:

    char readByte() {
        char out;
        m_stream->read(&out, 1);
        return out;
    }

    int m_nRead;
    mutable char m_buf;
    mutable bool m_peeked;
    InputStreamType * m_stream;

};

} /* namespace internal */
} /* namespace mgen */

#endif /* MGEN_JSON_INPUT_STREAM */
