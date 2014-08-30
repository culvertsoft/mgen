package se.culvertsoft.mgen.api.util;

import se.culvertsoft.mgen.api.util.internal.Base64Impl_MIG;

/**
 * Utility class for Base64 calculations and conversions
 */
public class Base64 {

	/**
	 * Encodes an int16 to its base64 string representation
	 * 
	 * @param i
	 *            The int16 to convert
	 * 
	 * @return The base64 converted representation of the integer
	 */
	public static String encode(final short i) {
		return encode(encBigE(i)).substring(0, 3);
	}

	/**
	 * Converts a byte array to its base64 string representation
	 * 
	 * @param bytes
	 *            The bytes to convert
	 * 
	 * @return The converted base64 form of the bytes
	 */
	public static String encode(final byte[] bytes) {
		return Base64Impl_MIG.encodeToString(bytes, false);
	}

	/**
	 * Decodes a bas64 string into its byte array representation
	 * 
	 * @param text
	 *            The base64 string to convert
	 * 
	 * @return The converted bytes
	 */
	public static byte[] decode(final String text) {
		return Base64Impl_MIG.decodeFast(ensureMultipleOf4(text));
	}

	/**
	 * Internal helper method to ensure that the length of the input string is a
	 * multiple of 4. The input is padded with '=' until its length is a
	 * multiple of 4.
	 * 
	 * @param text
	 *            The text to pad
	 * 
	 * @return The padded text
	 */
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

	/**
	 * Internal helper method for encoding an int16 into a big endian byte array
	 * 
	 * @param s
	 *            the int16 to encode
	 * 
	 * @return the big-endian encoded bytes
	 */
	private static byte[] encBigE(final short s) {
		final byte[] out = new byte[2];
		out[0] = (byte) ((s >>> 8) & 0xFF);
		out[1] = (byte) ((s >>> 0) & 0xFF);
		return out;
	}

}
