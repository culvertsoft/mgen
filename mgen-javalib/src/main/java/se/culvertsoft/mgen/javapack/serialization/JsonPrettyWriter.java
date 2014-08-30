package se.culvertsoft.mgen.javapack.serialization;

import java.io.IOException;
import java.io.OutputStream;

import se.culvertsoft.mgen.javapack.classes.ClassRegistryBase;

/**
 * An MGen wire format writer for writing objects to data streams in JSON format.
 * This class does the same as JsonWriter, except that it also adds newlines and
 * indentation to the written JSON so it is more human readable.
 */
public class JsonPrettyWriter extends JsonWriter {

	/**
	 * Creates a new JsonPrettyWriter
	 * 
	 * @param outputStream
	 *            The data output stream to write to
	 * 
	 * @param classRegistry
	 *            The class registry to use
	 * 
	 * @param compact
	 *            If to use standard of compact mode. In compact mode, MGen
	 *            object's type metadata is only written to the stream when it
	 *            cannot be inferred by the Reader.
	 * 
	 * @param maxDepth
	 *            The maximum recursion depth allowed for writing objects. While
	 *            writing the contents of arrays, maps, objects, lists etc the
	 *            current recursion depth is increased by one. The default value
	 *            is DEFAULT_MAX_DEPTH.
	 * 
	 * @param includeTransientFields
	 *            If fields flagged as transient should also be written. The
	 *            default is not to write transient fields.
	 */
	public JsonPrettyWriter(
			final OutputStream outputStream,
			final ClassRegistryBase classRegistry,
			final boolean compact,
			final int maxDepth,
			final boolean includeTransientFields) {
		super(outputStream, classRegistry, compact, maxDepth, includeTransientFields);
	}

	/**
	 * Creates a new JsonPrettyWriter
	 * 
	 * @param outputStream
	 *            The data output stream to write to
	 * 
	 * @param classRegistry
	 *            The class registry to use
	 * 
	 * @param compact
	 *            If to use standard of compact mode. In compact mode, MGen
	 *            object's type metadata is only written to the stream when it
	 *            cannot be inferred by the Reader. The default setting is
	 *            standard mode.
	 * 
	 * @param maxDepth
	 *            The maximum recursion depth allowed for writing objects. While
	 *            writing the contents of arrays, maps, objects, lists etc the
	 *            current recursion depth is increased by one. The default value
	 *            is DEFAULT_MAX_DEPTH.
	 */
	public JsonPrettyWriter(
			final OutputStream outputStream,
			final ClassRegistryBase classRegistry,
			final boolean compact,
			final int maxDepth) {
		super(outputStream, classRegistry, compact, maxDepth);
	}

	/**
	 * Creates a new JsonPrettyWriter
	 * 
	 * @param outputStream
	 *            The data output stream to write to
	 * 
	 * @param classRegistry
	 *            The class registry to use
	 * 
	 * @param compact
	 *            If to use standard of compact mode. In compact mode, MGen
	 *            object's type metadata is only written to the stream when it
	 *            cannot be inferred by the Reader. The default setting is
	 *            standard mode.
	 */
	public JsonPrettyWriter(
			final OutputStream outputStream,
			final ClassRegistryBase classRegistry,
			final boolean compact) {
		this(outputStream, classRegistry, compact, DEFAULT_MAX_DEPTH);
	}

	/**
	 * Creates a new JsonPrettyWriter
	 * 
	 * @param outputStream
	 *            The data output stream to write to
	 * 
	 * @param classRegistry
	 *            The class registry to use
	 */
	public JsonPrettyWriter(final OutputStream outputStream, final ClassRegistryBase classRegistry) {
		this(outputStream, classRegistry, DEFAULT_COMPACT);
	}

	/**
	 * Internal overloaded method to produce pretty JSON.
	 * 
	 * @param name
	 *            The name of the field/map key
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	@Override
	protected void writeName(final String name) throws IOException {
		super.writeName(name);
		write(' ');
	}

	/**
	 * Internal overloaded method to produce pretty JSON.
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	@Override
	protected void newEntry() throws IOException {
		super.newEntry();
		write('\n');
		for (int i = 0; i < m_depth; i++)
			write('\t');
	}

	/**
	 * Internal overloaded method to produce pretty JSON.
	 * 
	 * @param endString
	 *            The string that finishes this block (e.g. a ']' for the end of
	 *            writing an a array)
	 * 
	 * @param hasContents
	 *            If the block was non-empty
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	@Override
	protected void endBlock(final String endString, final boolean hasContents) throws IOException {
		if (hasContents) {
			write('\n');
			for (int i = 0; i < m_depth - 1; i++)
				write('\t');
		}
		super.endBlock(endString, hasContents);
	}

}
