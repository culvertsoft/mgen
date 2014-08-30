package se.culvertsoft.mgen.javapack.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class that implements a single function, readFully, which will read
 * and fill the bytes of an array, or throw an exception if it fails.
 */
public class StreamUtil {

	/**
	 * Reads len bytes into out. If enough bytes cannot be read, an IOException
	 * is thrown.
	 * 
	 * @param stream
	 *            The data input stream to read from.
	 * 
	 * @param len
	 *            The number of bytes to read
	 * 
	 * @param out
	 *            The array that receives the bytes
	 * 
	 * @throws IOException
	 *             If the underlying stream throws an IOException or EOF was
	 *             reached.
	 */
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
