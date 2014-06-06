package se.culvertsoft.mgen.api.util.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ListClasses {

	public static List<String> namesInJar(final String pathToJar)
			throws IOException {

		final ArrayList<String> out = new ArrayList<String>();
		final JarFile jarFile = new JarFile(pathToJar);

		try {
			final Enumeration<?> e = jarFile.entries();

			while (e.hasMoreElements()) {
				final JarEntry je = (JarEntry) e.nextElement();
				if (je.isDirectory() || !je.getName().endsWith(".class"))
					continue;
				final String className = je
						.getName()
						.substring(0, je.getName().length() - 6)
						.replace('/', '.');
				out.add(className);
			}

		} finally {
			jarFile.close();
		}

		return out;

	}

}
