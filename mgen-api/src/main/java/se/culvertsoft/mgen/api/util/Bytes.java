package se.culvertsoft.mgen.api.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Bytes {

	public static byte[] encBigE(final int i) {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeInt(i);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}

	public static byte[] encBigE(final short s) {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeShort(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}

	public static int decBigEInt(final byte[] bytes) throws IOException {
		return new DataInputStream(new ByteArrayInputStream(bytes)).readInt();
	}

	public static short decBigEShort(final byte[] bytes) throws IOException {
		return new DataInputStream(new ByteArrayInputStream(bytes)).readShort();
	}

}
