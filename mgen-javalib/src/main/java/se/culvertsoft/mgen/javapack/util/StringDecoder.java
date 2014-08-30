package se.culvertsoft.mgen.javapack.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

/**
 * Utility class for decoding UTF8-streams in the built-in MGen readers. Not
 * intended to be used except from MGen Readers.
 */
public class StringDecoder {

	private final ByteBuffer m_inputBuffer;
	private final CharBuffer m_outputBuffer;
	private final byte[] m_inputArray;

	private final CharsetDecoder m_stringDecoder;

	public StringDecoder(final int bufferSize, final Charset charset) {
		m_inputBuffer = ByteBuffer.allocate(bufferSize);
		m_outputBuffer = CharBuffer.allocate(bufferSize);
		m_stringDecoder = charset
				.newDecoder()
				.onMalformedInput(CodingErrorAction.REPLACE)
				.onUnmappableCharacter(CodingErrorAction.REPLACE);
		m_inputArray = m_inputBuffer.array();
	}

	private void reset() {
		m_inputBuffer.rewind();
		m_inputBuffer.limit(m_inputBuffer.capacity());
		m_outputBuffer.rewind();
		m_outputBuffer.limit(m_outputBuffer.capacity());
		m_stringDecoder.reset();
	}

	public String decode(final InputStream stream, final int nBytes) throws IOException {

		if (nBytes <= m_inputArray.length) {

			reset();

			StreamUtil.readFully(stream, nBytes, m_inputArray);
			m_inputBuffer.limit(nBytes);

			CoderResult cr = m_stringDecoder.decode(m_inputBuffer, m_outputBuffer, true);

			if (cr.isUnderflow())
				cr = m_stringDecoder.flush(m_outputBuffer);

			if (cr.isOverflow())
				cr.throwException();

			m_outputBuffer.flip();
			return m_outputBuffer.toString();

		} else {
			final byte[] data = new byte[nBytes];
			StreamUtil.readFully(stream, data.length, data);
			final ByteBuffer in = ByteBuffer.wrap(data);
			return m_stringDecoder.decode(in).toString();
		}

	}

}
