package se.culvertsoft.mgen.javapack.serialization;

import java.io.IOException;
import java.io.OutputStream;

import se.culvertsoft.mgen.javapack.classes.ClassRegistry;

public class JsonPrettyWriter extends JsonWriter {

	public JsonPrettyWriter(
			final OutputStream outputStream,
			final ClassRegistry classRegistry,
			final boolean compact,
			final int maxDepth) {
		super(outputStream, classRegistry, compact, maxDepth);
	}

	public JsonPrettyWriter(
			final OutputStream outputStream,
			final ClassRegistry classRegistry,
			final boolean compact) {
		this(outputStream, classRegistry, compact, DEFAULT_MAX_DEPTH);
	}

	public JsonPrettyWriter(
			final OutputStream outputStream,
			final ClassRegistry classRegistry) {
		this(outputStream, classRegistry, DEFAULT_COMPACT);
	}

	@Override
	protected void writeName(final String name) throws IOException {
		super.writeName(name);
		write(" ");
	}

	@Override
	protected void newEntry() throws IOException {
		super.newEntry();
		write("\n");
		for (int i = 0; i < m_depth; i++)
			write("\t");
	}

	@Override
	protected void endBlock(final String endString, final boolean hasContents)
			throws IOException {
		if (hasContents) {
			write("\n");
			for (int i = 0; i < m_depth - 1; i++)
				write("\t");
		}
		super.endBlock(endString, hasContents);
	}

}
