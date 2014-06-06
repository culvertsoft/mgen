package se.culvertsoft.mgen.api.util.internal;

import java.io.ByteArrayOutputStream;

public class ByteArrayOutputStreamExposed extends ByteArrayOutputStream {

	public ByteArrayOutputStreamExposed() {
		super();
	}

	public ByteArrayOutputStreamExposed(final int n) {
		super(n);
	}

	public byte[] backingArray() {
		return buf;
	}

}
