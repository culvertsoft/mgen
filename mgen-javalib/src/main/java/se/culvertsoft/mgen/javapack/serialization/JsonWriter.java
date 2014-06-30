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

	public static final int DEFAULT_MAX_DEPTH = 256;

	protected final int[] m_iEntry;
	protected int m_depth = 0;

	public JsonWriter(
			final OutputStream outputStream,
			final ClassRegistry classRegistry,
			final int maxDepth) {
		super(outputStream, classRegistry);
		m_iEntry = new int[maxDepth];
	}

	public JsonWriter(
			final OutputStream outputStream,
			final ClassRegistry classRegistry) {
		this(outputStream, classRegistry, DEFAULT_MAX_DEPTH);
	}

	@Override
	public void writeObject(final MGenBase o) throws IOException {
		m_depth = 0;
		writeMGenObject(o);
		flush();
	}

	@Override
	public void writeMGenObjectField(MGenBase o, Field field)
			throws IOException {
		newEntry();
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
		endBlock("}", m_iEntry[m_depth] > 0);
	}

	@Override
	public void writeBooleanField(final boolean b, final Field field)
			throws IOException {
		writePair(field.name(), String.valueOf(b));
	}

	@Override
	public void writeInt8Field(final byte b, final Field field)
			throws IOException {
		writePair(field.name(), String.valueOf(b));
	}

	@Override
	public void writeInt16Field(final short s, final Field field)
			throws IOException {
		writePair(field.name(), String.valueOf(s));
	}

	@Override
	public void writeInt32Field(final int i, final Field field)
			throws IOException {
		writePair(field.name(), String.valueOf(i));
	}

	@Override
	public void writeInt64Field(final long l, final Field field)
			throws IOException {
		writePair(field.name(), String.valueOf(l));
	}

	@Override
	public void writeFloat32Field(final float f, final Field field)
			throws IOException {
		writePair(field.name(), String.valueOf(f));
	}

	@Override
	public void writeFloat64Field(final double d, final Field field)
			throws IOException {
		writePair(field.name(), String.valueOf(d));
	}

	@Override
	public void writeStringField(final String s, final Field field)
			throws IOException {
		writePair(field.name(), quoteEscape(s));
	}

	@Override
	public void writeListField(final ArrayList<Object> list, final Field field)
			throws IOException {
		newEntry();
		writeName(field.name());
		writeList(list, (ListType) field.typ());
	}

	@Override
	public void writeMapField(final HashMap<Object, Object> m, final Field f)
			throws IOException {
		newEntry();
		writeName(f.name());
		writeMap(m, (MapType) f.typ());
	}

	@Override
	public void writeArrayField(Object array, Field field) throws IOException {
		newEntry();
		writeName(field.name());
		writeArray(array, (ArrayType) field.typ());
	}

	/*******************************************************************
	 * 
	 * 
	 * - - - - - - - - - - INTERNAL METHODS
	 * 
	 * 
	 ******************************************************************/

	protected void writeName(final String name) throws IOException {
		write('"' + name + "\":");
	}

	protected void newEntry() throws IOException {
		if (m_iEntry[m_depth] > 0)
			write(",");
		m_iEntry[m_depth]++;
	}

	protected void endBlock(final String endString, final boolean hasContents)
			throws IOException {
		m_depth--;
		write(endString);
	}

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
				newEntry();
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
			for (final Object key : m.keySet()) {
				newEntry();
				writeName(String.valueOf(key));
				writeObject(m.get(key), typ.valueType());
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

	private void writeObjectArray(final Object[] objects, final ArrayType typ)
			throws IOException {
		beginBlock("[");
		for (Object o : objects) {
			writeObject(o, typ.elementType());
		}
		endBlock("]", objects.length != 0);
	}

	private String quoteEscape(final String text) {
		return text != null ? quote(JSONValue.escape(text)) : "null";
	}

	private String quote(final String in) {
		return '"' + in + '"';
	}

	private void writePair(final String name, final String value)
			throws IOException {
		newEntry();
		writeName(name);
		write(value);
	}

	private void writeTypeId(final MGenBase o) throws IOException {
		writePair("__t", quote(o._typeIds16BitBase64String()));
	}

	private void beginBlock(final String beginString) throws IOException {
		write(beginString);
		m_depth++;
		if (m_depth >= m_iEntry.length)
			throw new SerializationException("Max recursion depth reached");
		m_iEntry[m_depth] = 0;
	}

}
