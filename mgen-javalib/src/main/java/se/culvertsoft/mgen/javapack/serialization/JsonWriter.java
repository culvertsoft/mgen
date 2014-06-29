package se.culvertsoft.mgen.javapack.serialization;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONValue;

import se.culvertsoft.mgen.api.model.ArrayType;
import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.api.model.ListType;
import se.culvertsoft.mgen.api.model.MapType;
import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.javapack.classes.ClassRegistry;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.exceptions.SerializationException;

public class JsonWriter extends DynamicWriter {

	private final boolean m_prettyPrint;
	private int m_indentLevel = 0;

	public JsonWriter(
			final OutputStream outputStream,
			final ClassRegistry classRegistry,
			final boolean prettyPrint) {
		super(outputStream, classRegistry);
		m_prettyPrint = prettyPrint;
	}

	public JsonWriter(
			final OutputStream outputStream,
			final ClassRegistry classRegistry) {
		this(outputStream, classRegistry, false);
	}

	@Override
	public void writeObject(final MGenBase o) throws IOException {
		writeMGenObject(o);
		flush();
	}

	@Override
	public void writeMGenObjectField(MGenBase o, Field field)
			throws IOException {
		newEntry(true);
		writeName(field.name());
		writeMGenObject(o);
	}

	@Override
	public void beginWrite(
			final MGenBase o,
			final int nFieldsSet,
			final int nFieldsTotal) throws IOException {
		beginBlock("{");
		writeTypeId(o);
	}

	@Override
	public void finishWrite() throws IOException {
		endBlock("}", true);
	}

	@Override
	public void writeBooleanField(final boolean b, final Field field)
			throws IOException {
		writePair(field.name(), String.valueOf(b), true);
	}

	@Override
	public void writeInt8Field(final byte b, final Field field)
			throws IOException {
		writePair(field.name(), String.valueOf(b), true);
	}

	@Override
	public void writeInt16Field(final short s, final Field field)
			throws IOException {
		writePair(field.name(), String.valueOf(s), true);
	}

	@Override
	public void writeInt32Field(final int i, final Field field)
			throws IOException {
		writePair(field.name(), String.valueOf(i), true);
	}

	@Override
	public void writeInt64Field(final long l, final Field field)
			throws IOException {
		writePair(field.name(), String.valueOf(l), true);
	}

	@Override
	public void writeFloat32Field(final float f, final Field field)
			throws IOException {
		writePair(field.name(), String.valueOf(f), true);
	}

	@Override
	public void writeFloat64Field(final double d, final Field field)
			throws IOException {
		writePair(field.name(), String.valueOf(d), true);
	}

	@Override
	public void writeStringField(final String s, final Field field)
			throws IOException {
		writePair(field.name(), quoteEscape(s), true);
	}

	@Override
	public void writeListField(final ArrayList<Object> list, final Field field)
			throws IOException {
		newEntry(true);
		writeName(field.name());
		writeList(list, (ListType) field.typ());
	}

	@Override
	public
			void
			writeMapField(final HashMap<Object, Object> m, final Field field)
					throws IOException {
		newEntry(true);
		writeName(field.name());
		writeMap(m, (MapType) field.typ());
	}

	@Override
	public void writeArrayField(Object array, Field field) throws IOException {
		newEntry(true);
		writeName(field.name());
		writeArray(array, (ArrayType) field.typ());
	}

	/*******************************************************************
	 * 
	 * 
	 * - - - - - - - - - - INTERNAL METHODS
	 * 
	 * @throws IOException
	 * 
	 ******************************************************************/

	private void writeMGenObject(MGenBase o) throws IOException {
		if (o == null) {
			write("null");
		} else {
			o._accept(this);
		}
	}

	@SuppressWarnings("unchecked")
	private void writeObject(final Object o, final Type typ) throws IOException {

		if (o == null) {
			write("null");
			return;
		}

		switch (typ.typeEnum()) {
		case BOOL:
		case INT8:
		case INT16:
		case INT32:
		case INT64:
		case FLOAT32:
		case FLOAT64:
			write(String.valueOf(o));
			break;
		case STRING:
			write(quoteEscape((String) o));
			break;
		case ARRAY:
			writeArray(o, (ArrayType) typ);
			break;
		case LIST:
			writeList((List<Object>) o, (ListType) typ);
			break;
		case MAP:
			writeMap((Map<Object, Object>) o, (MapType) typ);
			break;
		case MGEN_BASE:
		case UNKNOWN:
		case CUSTOM:
			writeMGenObject((MGenBase) o);
			break;
		default:
			throw new SerializationException("Unknown type for writeObject");
		}

	}

	private void writeList(List<Object> o, ListType typ) throws IOException {
		if (o == null) {
			write("null");
		} else {
			beginBlock("[");
			for (int i = 0; i < o.size(); i++) {
				newEntry(i > 0);
				writeObject(o.get(i), typ.elementType());
			}
			endBlock("]", !o.isEmpty());
		}
	}

	private void writeMap(Map<Object, Object> m, MapType typ)
			throws IOException {
		if (m == null) {
			write("null");
		} else {
			beginBlock("{");
			int i = 0;
			for (final Object key : m.keySet()) {
				final Object value = m.get(key);
				newEntry(i > 0);
				writeName(String.valueOf(key));
				writeObject(value, typ.valueType());
				i++;
			}
			endBlock("}", !m.isEmpty());
		}
	}

	private void writeArray(Object o, ArrayType typ) throws IOException {
		if (o == null) {
			write("null");
		} else {
			switch (typ.elementType().typeEnum()) {
			case BOOL:
				writeArray((boolean[]) o);
				break;
			case INT8:
				writeArray((byte[]) o);
				break;
			case INT16:
				writeArray((short[]) o);
				break;
			case INT32:
				writeArray((int[]) o);
				break;
			case INT64:
				writeArray((long[]) o);
				break;
			case FLOAT32:
				writeArray((float[]) o);
				break;
			case FLOAT64:
				writeArray((double[]) o);
				break;
			case STRING:
				writeArray((String[]) o);
				break;
			case ARRAY:
			case LIST:
			case MAP:
			case MGEN_BASE:
			case UNKNOWN:
			case CUSTOM:
				writeObjectArray((Object[]) o, typ);
				break;
			default:
				break;
			}
		}
	}

	private void writeArray(final boolean[] o) throws IOException {
		beginBlock("[");
		for (int i = 0; i < o.length; i++) {
			if (i > 0)
				write(",");
			write(o[i]);
		}
		endBlock("]", false);
	}

	private void writeArray(final byte[] o) throws IOException {
		beginBlock("[");
		for (int i = 0; i < o.length; i++) {
			if (i > 0)
				write(",");
			write(o[i]);
		}
		endBlock("]", false);
	}

	private void writeArray(final short[] o) throws IOException {
		beginBlock("[");
		for (int i = 0; i < o.length; i++) {
			if (i > 0)
				write(",");
			write(o[i]);
		}
		endBlock("]", false);
	}

	private void writeArray(final int[] o) throws IOException {
		beginBlock("[");
		for (int i = 0; i < o.length; i++) {
			if (i > 0)
				write(",");
			write(o[i]);
		}
		endBlock("]", false);
	}

	private void writeArray(final long[] o) throws IOException {
		beginBlock("[");
		for (int i = 0; i < o.length; i++) {
			if (i > 0)
				write(",");
			write(o[i]);
		}
		endBlock("]", false);
	}

	private void writeArray(final float[] o) throws IOException {
		beginBlock("[");
		for (int i = 0; i < o.length; i++) {
			if (i > 0)
				write(",");
			write(o[i]);
		}
		endBlock("]", false);
	}

	private void writeArray(final double[] o) throws IOException {
		beginBlock("[");
		for (int i = 0; i < o.length; i++) {
			if (i > 0)
				write(",");
			write(o[i]);
		}
		endBlock("]", false);
	}

	private void writeArray(final String[] o) throws IOException {
		beginBlock("[");
		for (int i = 0; i < o.length; i++) {
			if (i > 0)
				write(",");
			write(quoteEscape(o[i]));
		}
		endBlock("]", false);
	}

	private void writeObjectArray(final Object[] o, final ArrayType typ)
			throws IOException {
		beginBlock("[");
		for (int i = 0; i < o.length; i++) {
			newEntry(i > 0);
			writeObject(o[i], typ.elementType());
		}
		endBlock("]", o.length != 0);
	}

	private String quoteEscape(final String text) {
		return text != null ? quote(JSONValue.escape(text)) : "null";
	}

	private String quote(final String in) {
		return '"' + in + '"';
	}

	private void writeName(final String name) throws IOException {
		write('"' + name + "\":");
		if (m_prettyPrint)
			write(" ");
	}

	private void writePair(
			final String name,
			final String value,
			final boolean writeComma) throws IOException {
		newEntry(writeComma);
		writeName(name);
		write(value);
	}

	private void writeTypeId(final MGenBase o) throws IOException {
		writePair("__t", quote(o._typeIds16BitBase64String()), false);
	}

	private void newEntry(final boolean writeComma) throws IOException {
		if (writeComma)
			write(",");
		if (m_prettyPrint) {
			write("\n");
			for (int i = 0; i < m_indentLevel; i++) {
				write("    ");
			}
		}
	}

	private void beginBlock(final String beginString) throws IOException {
		write(beginString);
		m_indentLevel++;
	}

	private void endBlock(final String endString, final boolean onNewLine)
			throws IOException {
		m_indentLevel--;
		if (onNewLine)
			newEntry(false);
		write(endString);
	}

}
