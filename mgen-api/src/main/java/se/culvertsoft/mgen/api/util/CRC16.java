package se.culvertsoft.mgen.api.util;

import java.nio.charset.Charset;

/**
 * Utility class for calculating 16 bit hashes from a string or byte array.
 */
public class CRC16 {

	private final static Charset charset = Charset.forName("UTF8");

	/**
	 * Calculates the 16 it crc of the provided input array
	 * 
	 * @param buffer
	 *            The bytes to calculate the hash of
	 * 
	 * @return The calculated hash
	 */
	public static short calc(final byte[] buffer) {
		int crc = 0xFFFF;
		for (int j = 0; j < buffer.length; j++) {
			crc = ((crc >>> 8) | (crc << 8)) & 0xffff;
			crc ^= (buffer[j] & 0xff);// byte to int, trunc sign
			crc ^= ((crc & 0xff) >> 4);
			crc ^= (crc << 12) & 0xffff;
			crc ^= ((crc & 0xFF) << 5) & 0xffff;
		}
		crc &= 0xffff;
		return (short) crc;
	}

	/**
	 * Calculates the 16 it crc of the provided string
	 * 
	 * @param buffer
	 *            The string to calculate the hash of
	 * 
	 * @return The calculated hash
	 */
	public static short calc(final String buffer) {
		return calc(buffer.getBytes(charset));
	}

}
