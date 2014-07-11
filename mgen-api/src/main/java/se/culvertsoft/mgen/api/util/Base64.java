package se.culvertsoft.mgen.api.util;

import se.culvertsoft.mgen.api.util.internal.Base64Impl_MIG;

public class Base64 {

	public static String encode(final short i) {
		return encode(encBigE(i)).substring(0, 3);
	}

	public static String encode(final byte[] bytes) {
		return Base64Impl_MIG.encodeToString(bytes, false);
	}

	public static byte[] decode(final String text) {
		return Base64Impl_MIG.decodeFast(ensureMultipleOf4(text));
	}

	private static String ensureMultipleOf4(final String text) {
		switch (text.length() % 4) {
		case 1:
			return text + "===";
		case 2:
			return text + "==";
		case 3:
			return text + "=";
		default:
			return text;
		}
	}

	private static byte[] encBigE(final short s) {
		final byte[] out = new byte[2];
		out[0] = (byte) ((s >>> 8) & 0xFF);
		out[1] = (byte) ((s >>> 0) & 0xFF);
		return out;
	}

}
