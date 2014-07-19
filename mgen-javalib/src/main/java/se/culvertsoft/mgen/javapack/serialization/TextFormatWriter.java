package se.culvertsoft.mgen.javapack.serialization;

import java.io.IOException;
import java.io.OutputStream;

import se.culvertsoft.mgen.javapack.classes.ClassRegistryBase;

public abstract class TextFormatWriter extends BuiltInWriter {

	public static int FLUSH_SIZE = STRING_ENCODE_BUFFER_SIZE / 4;

	private final OutputStream m_stream;
	private final StringBuilder m_buffer;

	public TextFormatWriter(final OutputStream stream, final ClassRegistryBase classRegistry) {
		super(classRegistry);
		m_stream = stream;
		m_buffer = new StringBuilder(FLUSH_SIZE * 2);
	}

	protected void write(final boolean b) throws IOException {
		m_buffer.append(b);
		if (m_buffer.length() >= FLUSH_SIZE)
			flush();
	}

	protected void write(final byte b) throws IOException {
		m_buffer.append(b);
		if (m_buffer.length() >= FLUSH_SIZE)
			flush();
	}

	protected void write(final char s) throws IOException {
		m_buffer.append(s);
		if (m_buffer.length() >= FLUSH_SIZE)
			flush();
	}

	protected void write(final short s) throws IOException {
		m_buffer.append(s);
		if (m_buffer.length() >= FLUSH_SIZE)
			flush();
	}

	protected void write(final int i) throws IOException {
		m_buffer.append(i);
		if (m_buffer.length() >= FLUSH_SIZE)
			flush();
	}

	protected void write(final long i) throws IOException {
		m_buffer.append(i);
		if (m_buffer.length() >= FLUSH_SIZE)
			flush();
	}

	protected void write(final float f) throws IOException {
		m_buffer.append(f);
		if (m_buffer.length() >= FLUSH_SIZE)
			flush();
	}

	protected void write(final double d) throws IOException {
		m_buffer.append(d);
		if (m_buffer.length() >= FLUSH_SIZE)
			flush();
	}

	protected void write(final String s) throws IOException {
		for (int i = 0; i < s.length(); i++)
			write(s.charAt(i));
	}

	protected void flush() throws IOException {
		if (m_buffer.length() > 0) {
			m_stringEncoder.encode(m_buffer);
			m_stream.write(m_stringEncoder.data(), 0, m_stringEncoder.size());
			m_buffer.setLength(0);
		}
	}

}
