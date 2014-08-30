package se.culvertsoft.mgen.javapack.serialization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.BoolType;
import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.exceptions.SerializationException;

/**
 * Warning: EXPERIMENTAL. The API of this class may change significantly.
 * 
 * See CommandLineArgParser - which is an experimental command line parser
 * generator. It takes an MGen object class and generates a statically typed
 * command line argument parser from it.
 * 
 * This class, CommandLineArgHelp generates the help string associated with
 * those command line arguments.
 */
public class CommandLineArgHelp {

	private final Class<? extends MGenBase> m_cls;
	private final MGenBase m_instance;
	private final Map<String, Field> fullNameFields = new LinkedHashMap<>();
	private final HashSet<Field> m_shortCuts = new HashSet<>();
	private final HashSet<Character> m_shortcutChars = new HashSet<>();
	private final List<Field> m_required = new ArrayList<>();
	private final List<Field> m_optional = new ArrayList<>();
	private final Field[] m_fields;
	private final StringBuilder m_builder;

	/**
	 * Creates a command line arguments help string for the provided MGen object
	 * class.
	 */
	public CommandLineArgHelp(final Class<? extends MGenBase> cls) {
		m_cls = cls;
		m_instance = newInstance(cls);
		m_fields = m_instance._fields();
		m_builder = new StringBuilder();
		sortFields();
		buildHelpString();
	}

	/**
	 * Internal helper method sorting fields of the class into required and
	 * optional.
	 */
	private void sortFields() {

		for (final Field field : m_fields) {

			final char firstChar = field.name().charAt(0);

			fullNameFields.put(field.name(), field);
			if (!m_shortcutChars.contains(firstChar)) {
				m_shortcutChars.add(firstChar);
				m_shortCuts.add(field);
			}

			if (field.isRequired())
				m_required.add(field);
			else
				m_optional.add(field);

		}
	}

	/**
	 * Internal helper method for creating the help string.
	 */
	private void buildHelpString() {

		try {

			m_builder.setLength(0);

			m_builder.append("arguments summary:");
			if (m_fields.length == 0) {
				m_builder.append(" <no arguments available>\n");
				return;
			}
			buildShortArgs(m_required, "", "");
			buildShortArgs(m_optional, "[", "]");
			m_builder.append("\n\n");

			if (!m_required.isEmpty()) {
				m_builder.append("required arguments:\n");
				buildArgDescr(m_required);
			}

			if (!m_optional.isEmpty()) {
				m_builder.append("optional arguments:\n");
				buildArgDescr(m_optional);
			}

		} catch (final Exception e) {
			throw new SerializationException(
					"Could not generate command line arguments help text for " + m_cls,
					e);
		}
	}

	/**
	 * Internal helper method for building the first line (the summary) of the
	 * help text.
	 * 
	 * @param fields
	 *            The fields to write
	 * 
	 * @param begin
	 *            A string to be written before each argument
	 * 
	 * @param end
	 *            A string to be written after each argument
	 */
	private void buildShortArgs(final List<Field> fields, final String begin, final String end) {
		for (final Field field : fields) {
			m_builder.append(" ");
			m_builder.append(begin);
			m_builder.append(key(field));
			m_builder.append(isBool(field) ? " " + field.name().toUpperCase() : "");
			m_builder.append(end);
		}
	}

	/**
	 * Internal helper method for building the more detailed argument
	 * description of the help text.
	 * 
	 * @param set
	 *            The fields to write
	 */
	private void buildArgDescr(final Collection<Field> set) {
		for (final Field field : set) {
			m_builder.append("  ");
			if (hasShortcut(field))
				m_builder.append(shortKey(field) + ", ");
			m_builder.append(fullKey(field) + " ");
			if (isBool(field))
				m_builder.append("(" + field.typ() + ")");
			m_builder.append("\n");
		}
		m_builder.append("\n");
	}

	/**
	 * Returns the help string that was built.
	 * 
	 * @return The help string that was built.
	 */
	@Override
	public String toString() {
		return m_builder.toString();
	}

	/**
	 * Internal helper method to check if a field has a shortcut (single letter
	 * argument key).
	 * 
	 * @param f
	 *            The field to check
	 * 
	 * @return If a field has a shortcut (single letter argument key)
	 */
	boolean hasShortcut(final Field f) {
		return m_shortCuts.contains(f);
	}

	/**
	 * Internal convenience method to check if a field is of boolean type.
	 * 
	 * @param f
	 *            The field to check
	 * 
	 * @return If a field is of boolean type
	 */
	boolean isBool(final Field field) {
		return field.typ() != BoolType.INSTANCE;
	}

	/**
	 * Internal convenience method for getting the full argument key for a field
	 * 
	 * @param field
	 *            The field to get the key for
	 * 
	 * @return The long argument key (e.g. --myArgument)
	 */
	String fullKey(final Field field) {
		return "--" + field.name();
	}

	/**
	 * Internal convenience method for getting the short argument key for a
	 * field. See: 'fullKey(Field)'.
	 * 
	 * @param field
	 *            The field to get the key for
	 * 
	 * @return The short argument key (e.g. --m)
	 */
	String shortKey(final Field field) {
		return "-" + field.name().substring(0, 1);
	}

	/**
	 * Gets the most compact argument key representation of a field
	 * 
	 * @param field
	 *            The field to get the key for
	 * 
	 * @return The most compact argument key representation of the field
	 */
	String key(final Field field) {
		return m_shortCuts.contains(field) ? shortKey(field) : fullKey(field);
	}

	/**
	 * Convenience method for instantiating MGen object classes without checked
	 * exceptions.
	 * 
	 * @param cls
	 *            The class to instantiate an object of
	 * 
	 * @return The instantiated MGen object
	 */
	private static MGenBase newInstance(final Class<? extends MGenBase> cls) {
		try {
			return cls.newInstance();
		} catch (final Exception e) {
			throw new SerializationException("Could not instantiate object of type " + cls, e);
		}
	}

}
