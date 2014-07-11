package se.culvertsoft.mgen.api.util;

import java.nio.charset.Charset;

public class CRC16 {

	private final static Charset charset = Charset.forName("UTF8");

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

	public static short calc(final String buffer) {
		return calc(buffer.getBytes(charset));
	}

}
