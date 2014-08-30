package se.culvertsoft.mgen.javapack.serialization;

import java.io.IOException;
import java.io.OutputStream;

import se.culvertsoft.mgen.javapack.classes.ClassRegistryBase;

/**
 * Base class for all MGen wire format writers that use text formats, such as
 * JSON and XML. This class should not be used directly, but instead you should
 * use writers such as JsonWriter or JsonPrettyWriter.
 * 
 * TextFormatWriter instances can be configured to write to an output stream OR
 * directly produce strings of the serialized objects.
 */
public abstract class TextFormatWriter extends BuiltInWriter {

	/**
	 * The TextFormatWriter uses an internal buffer for written string data,
	 * where written objects are kept until reaching a limiting size or an
	 * object has been completely written. This is the default size of that
	 * buffer.
	 */
	public static int FLUSH_SIZE = STRING_ENCODE_BUFFER_SIZE / 4;

	private final StringBuilder m_textBuffer;
	private OutputStream m_stream;
	private boolean m_writeToStream;

	/**
	 * Internal method for creating a TextFormatWriter
	 * 
	 * @param stream
	 *            The data output stream to write to
	 * 
	 * @param classRegistry
	 *            The ClassRegistry to use
	 */
	protected TextFormatWriter(final OutputStream stream, final ClassRegistryBase classRegistry) {
		super(classRegistry);
		m_textBuffer = new StringBuilder(FLUSH_SIZE * 2);
		m_stream = stream;
		m_writeToStream = true;
	}

	/**
	 * Configures whether objects should be written to a data output stream or
	 * instead to a string.
	 * 
	 * @param writeToStream
	 *            Whether objects should be written to a data output stream or
	 *            instead to a string.
	 * 
	 * @return This TextFormatWriter
	 */
	protected TextFormatWriter setWriteToStream(final boolean writeToStream) {
		m_writeToStream = writeToStream;
		return this;
	}

	/**
	 * Replaces the output stream with a new one. Configures this
	 * TextFormatWriter to write to an output stream, and not directly to a
	 * string.
	 * 
	 * @param stream
	 *            The new data output stream to write to
	 * 
	 * @return This TextFormatWriter
	 */
	protected TextFormatWriter setOutput(final OutputStream stream) {
		m_writeToStream = true;
		m_stream = stream;
		return this;
	}

	/**
	 * Internal method for writing a boolean
	 * 
	 * @param b
	 *            The value to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	protected void write(final boolean b) throws IOException {
		m_textBuffer.append(b);
		checkflush();
	}

	/**
	 * Internal method for writing an int8
	 * 
	 * @param b
	 *            The value to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	protected void write(final byte b) throws IOException {
		m_textBuffer.append(b);
		checkflush();
	}

	/**
	 * Internal method for writing a character
	 * 
	 * @param c
	 *            The value to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	protected void write(final char c) throws IOException {
		m_textBuffer.append(c);
		checkflush();
	}

	/**
	 * Internal method for writing an int16
	 * 
	 * @param s
	 *            The value to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	protected void write(final short s) throws IOException {
		m_textBuffer.append(s);
		checkflush();
	}

	/**
	 * Internal method for writing an int32
	 * 
	 * @param i
	 *            The value to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	protected void write(final int i) throws IOException {
		m_textBuffer.append(i);
		checkflush();
	}

	/**
	 * Internal method for writing an int64
	 * 
	 * @param l
	 *            The value to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	protected void write(final long l) throws IOException {
		m_textBuffer.append(l);
		checkflush();
	}

	/**
	 * Internal method for writing a float32
	 * 
	 * @param f
	 *            The value to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	protected void write(final float f) throws IOException {
		m_textBuffer.append(f);
		checkflush();
	}

	/**
	 * Internal method for writing a float64
	 * 
	 * @param d
	 *            The value to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	protected void write(final double d) throws IOException {
		m_textBuffer.append(d);
		checkflush();
	}

	/**
	 * Internal method for writing a string
	 * 
	 * @param s
	 *            The value to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	protected void write(final String s) throws IOException {
		m_textBuffer.append(s);
		checkflush();
	}

	/**
	 * If this TextFormatWriter is configured to write to an underlying data
	 * output stream, this method flushes the internal string builder/buffer and
	 * writes data to the stream.
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	protected void flush() throws IOException {
		if (m_writeToStream && m_textBuffer.length() > 0) {
			m_stringEncoder.encode(m_textBuffer);
			m_stream.write(m_stringEncoder.data(), 0, m_stringEncoder.size());
			m_textBuffer.setLength(0);
		}
	}

	/**
	 * If this TextFormatWriter is configured to write directly to a String,
	 * this method empties the internal string buffer/builder into a String and
	 * returns it.
	 * 
	 * @return The serialized string
	 */
	protected String finish() {
		final String out = m_textBuffer.toString();
		m_textBuffer.setLength(0);
		return out;
	}

	/**
	 * Internal method for checking if the internal string buffer/builder has
	 * reached the flush limit and needs to be flushed to the underlying data
	 * output stream. Only does something if this TextFormatWriter is actually
	 * considered to write to the underlying data output stream, and not
	 * directly to a string.
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void checkflush() throws IOException {
		if (m_writeToStream && m_textBuffer.length() >= FLUSH_SIZE)
			flush();
	}

}
