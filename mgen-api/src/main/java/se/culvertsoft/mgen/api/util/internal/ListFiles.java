package se.culvertsoft.mgen.api.util.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.TreeSet;

import se.culvertsoft.mgen.api.exceptions.MGenException;

public class ListFiles {

	private static void recursively(final String dir, final String ending,
			final TreeSet<String> out) throws FileNotFoundException {
		final File pluginFolder = new File(dir);
		if (!pluginFolder.exists())
			throw new MGenException("Missing directory: " + dir);
		if (!pluginFolder.isDirectory())
			throw new MGenException("Not a directory: " + dir);
		final File[] curFiles = pluginFolder.listFiles();
		for (File file : curFiles) {
			if (file.isDirectory()) {
				recursively(file.getAbsolutePath(), ending, out);
			} else if (file.getName().endsWith(ending)) {
				out.add(file.getAbsolutePath());
			}
		}
	}

	public static TreeSet<String> recursively(final String dir,
			final String ending) throws FileNotFoundException {
		final TreeSet<String> out = new TreeSet<String>();
		recursively(dir, ending, out);
		return out;
	}

}
