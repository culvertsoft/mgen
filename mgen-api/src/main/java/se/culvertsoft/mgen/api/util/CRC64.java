package se.culvertsoft.mgen.api.util;

import java.nio.charset.Charset;

/**
 * Utility class for calculating 64 bit hashes
 */
public class CRC64 {

	/**
	 * Calculates the 64 bit hash of a byte array
	 * 
	 * @param data
	 *            The bytes to hash
	 * 
	 * @return The calculated hash
	 */
	public static long calc(final byte[] data) {
		long out = 0;
		for (final byte b : data) {
			final int lookupidx = ((int) out ^ b) & 0xff;
			out = (out >>> 8) ^ CACHE[lookupidx];
		}
		return out;
	}

	/**
	 * Calculates the 64 bit hash of a string
	 * 
	 * @param buffer
	 *            The string to hash
	 * 
	 * @return The calculated hash
	 */
	public static long calc(final String buffer) {
		return calc(buffer.getBytes(charset));
	}

	private final static Charset charset = Charset.forName("UTF8");

	/*
	 * ECMA specifies 0x42F0E1EBA9EA3693 0xC96C5795D7870F42 0xA17870F5D4F51B49
	 * We're using the first.
	 */
	private static final long POLYNOMIAL = 0x42F0E1EBA9EA3693L;

	private static final long[] CACHE = mkCache();

	private static long[] mkCache() {

		final long[] cache = new long[256];
		for (int i = 0; i < 256; i++) {
			long v = i;
			for (int j = 0; j < 8; j++) {
				if ((v & 1) == 1) {
					v = (v >>> 1) ^ POLYNOMIAL;
				} else {
					v = (v >>> 1);
				}
			}
			cache[i] = v;
		}
		return cache;
	}

}
