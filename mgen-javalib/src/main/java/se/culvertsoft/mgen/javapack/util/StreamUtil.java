package se.culvertsoft.mgen.javapack.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {

	public static void readFully(final InputStream stream, final int len, final byte[] out)
			throws IOException {
		int n = 0;
		while (n < len) {
			int count = stream.read(out, n, len - n);
			if (count < 0)
				throw new EOFException();
			n += count;
		}
	}
}
