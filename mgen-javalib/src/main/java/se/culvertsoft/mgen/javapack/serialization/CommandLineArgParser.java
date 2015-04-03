package se.culvertsoft.mgen.javapack.serialization;

import java.io.IOException;

import se.culvertsoft.mgen.api.model.*;
import se.culvertsoft.mgen.javapack.classes.ClassRegistryBase;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.exceptions.SerializationException;

/**
 * Warning: EXPERIMENTAL. The API of this class may change significantly.
 * 
 * A command line parser generator. It takes an MGen object class and generates
 * a statically typed command line argument parser from it.
 * 
 * It works by converting standard formatted command line arguments to Json, and
 * then handing off the parsing to the mgen JsonReader.
 */
public class CommandLineArgParser<T extends MGenBase> {

	private final Class<T> m_cls;
	private final ClassRegistryBase m_classRegistry;
	private final Field[] m_fields;
	private final JsonReader m_jsonReader;
	private final StringBuilder m_builder = new StringBuilder();
	private int m_n = 0;

	/**
	 * Creates a new command line argument parser.
	 * 
	 * @param cls
	 *            The MGen object class to create a parser for
	 * 
	 * @param classRegistry
	 *            The class registry in which the class provided is registered
	 */
	public CommandLineArgParser(final Class<T> cls, final ClassRegistryBase classRegistry) {
		m_cls = cls;
		m_classRegistry = classRegistry;
		m_fields = newInstance()._fields();
		m_jsonReader = new JsonReader(m_classRegistry);
	}

	/**
	 * Parses command line arguments into an MGen object of previously (in
	 * constructor) specified type. It works by converting standard formatted
	 * command line arguments to Json, and then handing off the parsing to the
	 * mgen JsonReader.
	 * 
	 * @param args
	 *            The command line arguments
	 * 
	 * @return The parsed object
	 * 
	 * @throws IOException
	 *             Compatibility layer with the underlying JsonReader, which may
	 *             throw IOExceptions when the underlying data input stream
	 *             behaves unexpectedly (e.g. reaches EOF before expected).
	 */
	public T parse(final String[] args) throws IOException {
		m_builder.setLength(0);

		m_builder.append("{");
		m_n = 0;

		for (int i = 0; i < args.length; i++) {
			final Field field = findField(prune(args[i]));
			final String value = i + 1 < args.length ? prune(args[i + 1]) : null;

			if (field.typ() == BoolType.INSTANCE) {
				addField(field, "true");
			} else {
				if (value == null)
					throw new SerializationException("No value provided for field " + field);
				addField(field, value);
				i++;
			}
		}

		m_builder.append("}");

		return m_jsonReader.readObject(m_builder.toString(), m_cls);
	}

	/**
	 * Convenience method for instantiating MGen object classes without checked
	 * exceptions.
	 * 
	 * @return The instantiated MGen object
	 */
	private T newInstance() {
		try {
			return m_cls.newInstance();
		} catch (final Exception e) {
			throw new SerializationException("Unable to create instance of class " + m_cls, e);
		}
	}

	/**
	 * Convenience method for removing quotes around a string.
	 * 
	 * @param txtIn
	 *            The text to remove quotes from.
	 * 
	 * @return The unquoted string.
	 */
	private String unQuote(final String txtIn) {
		String src = txtIn.trim();
		if (src.startsWith("\"")) {
			src = src.substring(1);
		}
		if (src.endsWith("\"")) {
			src = src.substring(0, src.length());
		}
		return src;
	}

	/**
	 * Internal helper method for pruning strings. Removes any leading '-'s and
	 * quotes.
	 * 
	 * @param txt
	 *            The string to prune
	 * 
	 * @return The pruned string
	 */
	private String prune(final String txt) {
		return removeMinuses(unQuote(removeMinuses(txt.trim())).trim());
	}

	/**
	 * Internal helper method for removing leading '-'s from a string
	 * 
	 * @param txt
	 *            The string to prune
	 * 
	 * @return The pruned string
	 */
	private String removeMinuses(String txt) {
		while (txt.startsWith("-")) {
			txt = txt.substring(1);
		}
		return txt;
	}

	/**
	 * Internal helper method called during construction when converting command
	 * line arguments to JSON.
	 * 
	 * @param field
	 *            The field to add
	 * 
	 * @param value
	 *            The value of the field
	 */
	private void addField(final Field field, final String value) {
		if (m_n > 0)
			m_builder.append(", ");
		m_builder.append(quote(field.name()) + ": ");
		switch (field.typ().typeEnum()) {
			case STRING:
			case ENUM:
				m_builder.append(quote(value));
				break;
			default:
				m_builder.append(value);
				break;
		}
		m_n++;
	}

	/**
	 * Internal helper function for finding a field in a class.
	 * 
	 * @param txt
	 *            The field name or short name (first letter of the field name)
	 * 
	 * @return The field
	 * 
	 * @throws SerializationException
	 *             If the field was not found
	 */
	private Field findField(final String txt) {

		for (final Field f : m_fields) {
			if (f.name().startsWith(txt)) {
				return f;
			}
		}

		throw new SerializationException("Could not find any field by name " + txt + " in type "
				+ m_cls);
	}

	/**
	 * Internal helper method for quoting a string
	 * 
	 * @param txt
	 *            The string to quote
	 * 
	 * @return The quoted string
	 */
	private String quote(final String txt) {
		return '"' + txt + '"';
	}

}
