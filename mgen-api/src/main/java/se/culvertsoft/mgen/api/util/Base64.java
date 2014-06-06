package se.culvertsoft.mgen.api.util;

import java.io.IOException;

import se.culvertsoft.mgen.api.util.internal.Base64Impl_TEMP;

public class Base64 {

	public static String encode(final int i) {
		return encode(Bytes.encBigE(i)).substring(0, 6);
	}

	public static String encode(final short i) {
		return encode(Bytes.encBigE(i)).substring(0, 3);
	}

	public static String encode(final byte[] bytes) {
		return Base64Impl_TEMP.byteArrayToBase64(bytes);
	}

	public static String encodeAlternate(final byte[] bytes) {
		return Base64Impl_TEMP.byteArrayToAltBase64(bytes);
	}

	public static String ensureMultipleOf4(String text) {
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

	public static short decodeShort(final String text) throws IOException {
		return Bytes.decBigEShort(decode(text));
	}

	public static int decodeInt(final String text) throws IOException {
		return Bytes.decBigEInt(decode(text));
	}

	public static byte[] decode(String text) {
		return Base64Impl_TEMP.base64ToByteArray(ensureMultipleOf4(text));
	}

	public static byte[] decodeAlternate(final String text) {
		return Base64Impl_TEMP.altBase64ToByteArray(ensureMultipleOf4(text));
	}

}
