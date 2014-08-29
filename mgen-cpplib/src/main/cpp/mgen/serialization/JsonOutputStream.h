#ifndef MGEN_JSON_OUTPUT_STREAM
#define MGEN_JSON_OUTPUT_STREAM

namespace mgen {
namespace internal {

/**
 * Internal helper class for connecting MGen's JsonWriter to the 
 * RapidJson JSON library.
 */
template<typename OutputStreamType>
class JsonOutStream {
public:
    JsonOutStream(OutputStreamType& stream) : m_stream(stream) {}
    void Put(const char c) { m_stream.write(&c, 1); }

private:
    OutputStreamType& m_stream;
};

} /* namespace internal */
} /* namespace mgen */

#endif /* MGEN_JSON_OUTPUT_STREAM */
