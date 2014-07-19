package se.culvertsoft.mgen.javapack.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

public class StringDecoder {

	private final int m_bufLen;
	private final ByteBuffer m_inputBuffer;
	private final CharBuffer m_buffer;
	private final byte[] arr;

	private final CharsetDecoder m_stringDecoder;

	/**
	 * @param maxStaticDecodeBufSz
	 *            If the incoming string is longer than this, a temporary new
	 *            heap allocated buffer is used
	 */
	public StringDecoder(final int maxStaticDecodeBufSz, final Charset charset) {
		m_bufLen = maxStaticDecodeBufSz;
		m_inputBuffer = ByteBuffer.allocate(maxStaticDecodeBufSz);
		m_buffer = CharBuffer.allocate(maxStaticDecodeBufSz);
		m_stringDecoder = charset
				.newDecoder()
				.onMalformedInput(CodingErrorAction.REPLACE)
				.onUnmappableCharacter(CodingErrorAction.REPLACE);
		arr = m_inputBuffer.array();
	}

	private void reset() {
		m_inputBuffer.rewind();
		m_inputBuffer.limit(m_inputBuffer.capacity());
		m_buffer.rewind();
		m_buffer.limit(m_buffer.capacity());
		m_stringDecoder.reset();
	}

	public String decode(final DataInputStream stream, final int nBytes) throws IOException {

		if (nBytes <= m_bufLen) {

			reset();

			stream.readFully(arr, 0, nBytes);
			m_inputBuffer.limit(nBytes);

			final CoderResult cr = m_stringDecoder.decode(m_inputBuffer, m_buffer, true);

			if (cr.isUnderflow())
				m_stringDecoder.flush(m_buffer);

			if (cr.isOverflow())
				cr.throwException();

			m_buffer.flip();
			return m_buffer.toString();

		} else {
			final byte[] data = new byte[nBytes];
			stream.readFully(data);
			final ByteBuffer in = ByteBuffer.wrap(data);
			return m_stringDecoder.decode(in).toString();
		}

	}

}
