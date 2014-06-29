package se.culvertsoft.mgen.javapack.serialization;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import se.culvertsoft.mgen.javapack.classes.ClassRegistry;

public abstract class DynamicWriter extends BuiltInWriter {

	private final StringBuilder buffer = new StringBuilder();

	public DynamicWriter(
			final OutputStream stream,
			final ClassRegistry classRegistry) {
		super(stream instanceof DataOutputStream ? (DataOutputStream) stream
				: new DataOutputStream(stream), classRegistry);
	}

	protected void write(final boolean b) throws IOException {
		buffer.append(b);
	}

	protected void write(final byte b) throws IOException {
		buffer.append(b);
	}

	protected void write(final short s) throws IOException {
		buffer.append(s);
	}

	protected void write(final int i) throws IOException {
		buffer.append(i);
	}

	protected void write(final long i) throws IOException {
		buffer.append(i);
	}

	protected void write(final float f) throws IOException {
		buffer.append(f);
	}

	protected void write(final double d) throws IOException {
		buffer.append(d);
	}

	protected void write(final String s) throws IOException {
		buffer.append(s);
	}

	protected void flush() throws IOException {
		final ByteBuffer bb = encodeString(buffer);
		buffer.setLength(0);
		m_stream.write(bb.array(), 0, bb.remaining());
	}

}
