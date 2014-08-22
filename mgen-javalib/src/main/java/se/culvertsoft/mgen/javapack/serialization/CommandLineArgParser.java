package se.culvertsoft.mgen.javapack.serialization;

import java.io.IOException;

import se.culvertsoft.mgen.api.model.BoolType;
import se.culvertsoft.mgen.api.model.EnumType;
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
	private int m_n = 0;

	public CommandLineArgParser(final Class<T> cls, final ClassRegistryBase classRegistry) {
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

		System.out.println("Converted cmd line args to: " + m_builder.toString());

		return m_jsonReader.readObject(m_builder.toString(), m_cls);
	}

	private T newInstance() {
		try {
			return m_cls.newInstance();
		} catch (final Exception e) {
			throw new SerializationException("Unable to create instance of class " + m_cls, e);
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
		return removeMinus(unQuote(txt.trim()).trim());
	}

	private String removeMinus(String txt) {
		return txt.startsWith("-") ? txt.substring(1) : txt;
	}

	private void addField(final Field field, final String value) {
		if (m_n > 0)
			m_builder.append(", ");
		m_builder.append(quote(field.name()) + ": ");
		if (field.typ() == StringType.INSTANCE || field.typ() instanceof EnumType)
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

		throw new SerializationException("Could not find any field by name " + txt + " in type "
				+ m_cls);
	}

	private String quote(final String txt) {
		return '"' + txt + '"';
	}

}
