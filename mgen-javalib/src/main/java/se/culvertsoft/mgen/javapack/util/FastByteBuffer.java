package se.culvertsoft.mgen.javapack.util;

import java.io.OutputStream;

/**
 * Fast utility OutputStream for bytes used by the MGen BinaryWriter. This
 * buffer can be used as a replacement for a ByteArrayOutputStream to improve
 * performance. The BinaryWriter uses this by default as an internal buffer
 * before flushing written bytes to the final data output stream.
 * 
 * Using this class instead of Javas ByteArrayOutputStream more than doubled
 * binary writing performance in the BinaryWriter.
 * 
 * Not intended to be used except by the MGen BinaryWriter.
 */
public final class FastByteBuffer extends OutputStream {

	private int m_offset;
	private byte[] m_data;

	public FastByteBuffer(final int initialSize) {
		m_offset = 0;
		m_data = new byte[initialSize];
	}

	public final int freeSpace() {
		return m_data.length - m_offset;
	}

	public final boolean hasFreeSpace() {
		return m_data.length != m_offset;
	}

	public final void incCapTo(final int newSize) {
		if (newSize > m_data.length) {
			final byte[] oldArray = m_data;
			m_data = new byte[newSize];
			System.arraycopy(oldArray, 0, m_data, 0, m_offset);
		}
	}

	public final void incCap() {
		incCapTo(2 * m_data.length);
	}

	public final void write(final byte b) {
		if (!hasFreeSpace())
			incCap();
		m_data[m_offset++] = b;
	}

	public final void write(final int b) {
		if (!hasFreeSpace())
			incCap();
		m_data[m_offset++] = (byte) b;
	}

	public final void write(final byte[] bytes, final int sz) {
		write(bytes, 0, sz);
	}

	public final void write(final byte[] bytes, final int offset, final int sz) {
		if (freeSpace() < sz)
			incCapTo(m_data.length + sz);
		if (sz > 16) {
			System.arraycopy(bytes, offset, m_data, m_offset, sz);
			m_offset += sz;
		} else {
			for (int i = 0; i < sz; i++) {
				m_data[m_offset++] = bytes[i + offset];
			}
		}
	}

	public final byte[] data() {
		return m_data;
	}

	public final int size() {
		return m_offset;
	}

	public final void clear() {
		m_offset = 0;
	}

	public final boolean nonEmpty() {
		return size() != 0;
	}

}
