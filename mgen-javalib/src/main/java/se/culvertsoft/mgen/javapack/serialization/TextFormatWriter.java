package se.culvertsoft.mgen.javapack.serialization;

import java.io.IOException;
import java.io.OutputStream;

import se.culvertsoft.mgen.javapack.classes.ClassRegistryBase;

public abstract class TextFormatWriter extends BuiltInWriter {

	public static int FLUSH_SIZE = STRING_ENCODE_BUFFER_SIZE / 4;

	private final StringBuilder m_textBuffer;
	private OutputStream m_stream;
	private boolean m_writeToStream;

	protected TextFormatWriter(final OutputStream stream, final ClassRegistryBase classRegistry) {
		super(classRegistry);
		m_textBuffer = new StringBuilder(FLUSH_SIZE * 2);
		m_stream = stream;
		m_writeToStream = true;
	}

	protected TextFormatWriter setWriteToStream(final boolean writeToStream) {
		m_writeToStream = writeToStream;
		return this;
	}

	protected TextFormatWriter setOutput(final OutputStream stream) {
		m_writeToStream = true;
		m_stream = stream;
		return this;
	}

	protected void write(final boolean b) throws IOException {
		m_textBuffer.append(b);
		checkflush();
	}

	protected void write(final byte b) throws IOException {
		m_textBuffer.append(b);
		checkflush();
	}

	protected void write(final char s) throws IOException {
		m_textBuffer.append(s);
		checkflush();
	}

	protected void write(final short s) throws IOException {
		m_textBuffer.append(s);
		checkflush();
	}

	protected void write(final int i) throws IOException {
		m_textBuffer.append(i);
		checkflush();
	}

	protected void write(final long i) throws IOException {
		m_textBuffer.append(i);
		checkflush();
	}

	protected void write(final float f) throws IOException {
		m_textBuffer.append(f);
		checkflush();
	}

	protected void write(final double d) throws IOException {
		m_textBuffer.append(d);
		checkflush();
	}

	protected void write(final String s) throws IOException {
		m_textBuffer.append(s);
		checkflush();
	}

	protected void flush() throws IOException {
		if (m_writeToStream && m_textBuffer.length() > 0) {
			m_stringEncoder.encode(m_textBuffer);
			m_stream.write(m_stringEncoder.data(), 0, m_stringEncoder.size());
			m_textBuffer.setLength(0);
		}
	}

	/**
	 * Used when writing to a String instead of a stream
	 */
	protected String finish() {
		final String out = m_textBuffer.toString();
		m_textBuffer.setLength(0);
		return out;
	}

	private void checkflush() throws IOException {
		if (m_writeToStream && m_textBuffer.length() >= FLUSH_SIZE)
			flush();
	}

}
