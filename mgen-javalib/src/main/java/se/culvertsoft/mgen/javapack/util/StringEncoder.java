package se.culvertsoft.mgen.javapack.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

/**
 * Utility class for encoding UTF8-streams in the built-in MGen writers. Not
 * intended to be used except from MGen Writers.
 */
public class StringEncoder {

	private ByteBuffer m_buffer;

	private final CharsetEncoder m_stringEncoder;

	public StringEncoder(final int bufferSize, final Charset charset) {
		m_buffer = ByteBuffer.allocate(bufferSize);
		m_stringEncoder = charset
				.newEncoder()
				.onMalformedInput(CodingErrorAction.REPLACE)
				.onUnmappableCharacter(CodingErrorAction.REPLACE);
	}

	public byte[] data() {
		return m_buffer.array();
	}

	public int size() {
		return m_buffer.remaining();
	}

	private void reset() {
		m_buffer.rewind();
		m_buffer.limit(m_buffer.capacity());
		m_stringEncoder.reset();
	}

	public void encode(final CharSequence s) throws IOException {

		if (s.length() <= 0)
			return;

		final CharBuffer in = CharBuffer.wrap(s);

		if (s.length() * 4 > m_buffer.capacity())
			m_buffer = ByteBuffer.allocate(s.length() * 4);

		reset();

		CoderResult cr = m_stringEncoder.encode(in, m_buffer, true);

		if (cr.isUnderflow())
			cr = m_stringEncoder.flush(m_buffer);

		if (cr.isOverflow())
			cr.throwException();

		m_buffer.flip();

	}

}
