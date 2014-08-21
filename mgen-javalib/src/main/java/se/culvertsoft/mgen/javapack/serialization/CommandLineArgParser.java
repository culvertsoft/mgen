package se.culvertsoft.mgen.javapack.serialization;

import java.io.IOException;

import se.culvertsoft.mgen.api.model.BoolType;
import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.api.model.StringType;
import se.culvertsoft.mgen.javapack.classes.ClassRegistryBase;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.exceptions.SerializationException;

public class CommandLineArgParser<T extends MGenBase> {

	private final Class<T> m_cls;
	private final ClassRegistryBase m_classRegistry;
	private final Field[] m_fields;
	private final JsonReader m_jsonReader;
	private final StringBuilder m_builder = new StringBuilder();
	private Field m_field = null;
	private int m_n = 0;

	public CommandLineArgParser(
			final Class<T> cls,
			final ClassRegistryBase classRegistry) {
		m_cls = cls;
		m_classRegistry = classRegistry;
		m_fields = newInstance()._fields();
		m_jsonReader = new JsonReader(m_classRegistry);
	}

	public T parse(final String[] args) throws IOException {
		m_builder.setLength(0);

		m_builder.append("{");
		m_n = 0;

		for (int i = 0; i < args.length; i++) {
			final String str = prune(args[i]);
			final String key = str.startsWith("-") ? str.substring(1) : null;
			final String value = str.startsWith("-") ? null : str;

			if (key != null) {
				if (m_field != null) {
					thr("No value provided for field " + m_field);
				}
				m_field = findField(key);
				if (m_field.typ() == BoolType.INSTANCE) {
					addField(m_field, "true");
					m_field = null;
				}
			} else {
				if (m_field != null) {
					addField(m_field, value);
					m_field = null;
				} else {
					thr("Passed value " + value
							+ " but didn't prepend a known field name");
				}
			}
		}

		m_builder.append("}");

		System.out.println("Converted cmd line args to: "
				+ m_builder.toString());

		return m_jsonReader.readObject(m_builder.toString(), m_cls);
	}

	private T newInstance() {
		try {
			return m_cls.newInstance();
		} catch (final Exception e) {
			throw new SerializationException(
					"Unable to create instance of class " + m_cls,
					e);
		}
	}

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

	private String prune(final String txt) {
		return unQuote(txt.trim()).trim();
	}

	private void addField(final Field field, final String value) {
		if (m_n > 0)
			m_builder.append(", ");
		m_builder.append(quote(field.name()) + ": ");
		if (field.typ() == StringType.INSTANCE)
			m_builder.append(quote(value));
		else
			m_builder.append(value);
		m_n++;
	}

	private Field findField(final String txt) {

		for (final Field f : m_fields) {
			if (f.name().startsWith(txt)) {
				return f;
			}
		}

		throw new SerializationException("Could not find any field by name "
				+ txt + " in type " + m_cls);
	}

	private void thr(final String error) {
		throw new SerializationException(error);
	}

	private String quote(final String txt) {
		return '"' + txt + '"';
	}

}
