package se.culvertsoft.mgen.api.util;

import java.nio.charset.Charset;
import java.util.HashMap;

public class Hasher {

	private final static Charset charset = Charset.forName("UTF8");
	private final HashMap<String, Integer> m_32bit = new HashMap<String, Integer>();
	private final HashMap<String, Short> m_16bit = new HashMap<String, Short>();

	public int _32bit(final String txt) {
		final Integer cached = m_32bit.get(txt);
		if (cached != null)
			return cached;

		final int h = static_32bit(txt);
		m_32bit.put(txt, h);
		return h;
	}

	public short _16bit(final String txt) {
		final Short cached = m_16bit.get(txt);
		if (cached != null)
			return cached;

		final short h = static_16bit(txt);
		m_16bit.put(txt, h);
		return h;
	}

	public static int static_32bit(final String txt) {
		return CRC32.calc(txt.getBytes(charset));
	}

	public static short static_16bit(final String txt) {
		return CRC16.calc(txt.getBytes(charset));
	}

}
