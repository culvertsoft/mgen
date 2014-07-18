package se.culvertsoft.mgen.javapack.util;

import java.io.ByteArrayOutputStream;

import se.culvertsoft.mgen.javapack.classes.ClassRegistryBase;
import se.culvertsoft.mgen.javapack.classes.EmptyClassRegistry;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.exceptions.SerializationException;
import se.culvertsoft.mgen.javapack.serialization.BuiltInWriter;
import se.culvertsoft.mgen.javapack.serialization.JsonPrettyWriter;

public class Stringifyer {

	public static final ClassRegistryBase REGISTRY = new EmptyClassRegistry();

	static class BosExposed extends ByteArrayOutputStream {
		public byte[] getBackingBuffer() {
			return buf;
		}
	}

	public static String toString(final MGenBase object) {

		final BosExposed stream = new BosExposed();
		final JsonPrettyWriter writer = new JsonPrettyWriter(stream, REGISTRY, true);
		writer.setShouldValidate(false);

		try {
			writer.writeObject(object);
			return new String(stream.getBackingBuffer(), 0, stream.size(), BuiltInWriter.charset);
		} catch (final Exception e) {
			throw new SerializationException(e);
		}
	}
}
