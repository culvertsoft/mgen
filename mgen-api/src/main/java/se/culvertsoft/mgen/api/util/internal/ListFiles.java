package se.culvertsoft.mgen.api.util.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.TreeSet;

import se.culvertsoft.mgen.api.exceptions.MGenException;

public class ListFiles {

	private static void list(
			final String dir,
			final String ending,
			final TreeSet<String> out,
			final boolean recurse) throws FileNotFoundException {
		final File pluginFolder = new File(dir);
		if (!pluginFolder.exists())
			throw new MGenException("Missing directory: " + dir);
		if (!pluginFolder.isDirectory())
			throw new MGenException("Not a directory: " + dir);
		final File[] curFiles = pluginFolder.listFiles();
		for (File file : curFiles) {
			if (file.isDirectory()) {
				if (recurse)
					list(file.getAbsolutePath(), ending, out, recurse);
			} else if (file.getName().endsWith(ending)) {
				out.add(file.getAbsolutePath());
			}
		}
	}

	public static
			TreeSet<String>
			list(final String dir, final String ending, final boolean recurse)
					throws FileNotFoundException {
		final TreeSet<String> out = new TreeSet<String>();
		list(dir, ending, out, recurse);
		return out;
	}

}
