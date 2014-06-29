package se.culvertsoft.mgen.javapack.serialization;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import se.culvertsoft.mgen.javapack.classes.ClassRegistry;

public abstract class DynamicWriter extends BuiltInWriter {

	public DynamicWriter(final OutputStream stream,
			final ClassRegistry classRegistry) {
		super(stream instanceof DataOutputStream ? (DataOutputStream) stream
				: new DataOutputStream(stream), classRegistry);
	}

	protected void write(final boolean i) throws IOException {
		write(String.valueOf(i));
	}

	protected void write(final byte i) throws IOException {
		write(String.valueOf(i));
	}

	protected void write(final short i) throws IOException {
		write(String.valueOf(i));
	}

	protected void write(final int i) throws IOException {
		write(String.valueOf(i));
	}

	protected void write(final long i) throws IOException {
		write(String.valueOf(i));
	}

	protected void write(final float f) throws IOException {
		write(String.valueOf(f));
	}

	protected void write(final double f) throws IOException {
		write(String.valueOf(f));
	}

	protected void write(final String s) throws IOException {
		final ByteBuffer bb = charset.encode(s);
		m_stream.write(bb.array(), 0, bb.remaining());
	}

}
