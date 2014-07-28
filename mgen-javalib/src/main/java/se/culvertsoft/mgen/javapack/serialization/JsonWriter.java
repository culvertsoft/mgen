package se.culvertsoft.mgen.javapack.serialization;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.ArrayType;
import se.culvertsoft.mgen.api.model.CustomType;
import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.api.model.ListType;
import se.culvertsoft.mgen.api.model.MapType;
import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.javapack.classes.ClassRegistryBase;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.exceptions.SerializationException;
import se.culvertsoft.mgen.javapack.metadata.FieldVisitSelection;

public class JsonWriter extends TextFormatWriter {

	public static final int DEFAULT_MAX_DEPTH = 256;
	public static final boolean DEFAULT_COMPACT = false;

	private final FieldVisitSelection m_selection;

	protected final boolean m_compact;
	protected final int[] m_iEntry;
	protected int m_depth = 0;

	public JsonWriter(
			final OutputStream outputStream,
			final ClassRegistryBase classRegistry,
			final boolean compact,
			final int maxDepth,
			final boolean includeTransientFields) {
		super(outputStream, classRegistry);
		m_selection = includeTransientFields ? FieldVisitSelection.ALL_SET
				: FieldVisitSelection.ALL_SET_NONTRANSIENT;
		m_compact = compact;
		m_iEntry = new int[maxDepth];
	}

	public JsonWriter(
			final OutputStream outputStream,
			final ClassRegistryBase classRegistry,
			final boolean compact,
			final int maxDepth) {
		this(outputStream, classRegistry, compact, maxDepth, false);
	}

	public JsonWriter(
			final OutputStream outputStream,
			final ClassRegistryBase classRegistry,
			final boolean compact) {
		this(outputStream, classRegistry, compact, DEFAULT_MAX_DEPTH);
	}

	public JsonWriter(final OutputStream outputStream, final ClassRegistryBase classRegistry) {
		this(outputStream, classRegistry, DEFAULT_COMPACT);
	}

	@Override
	public void writeObject(final MGenBase o) throws IOException {
		m_depth = 0;
		writeMGenObject(o, null);
		flush();
	}

	@Override
	public void writeMGenObjectField(MGenBase o, Field field) throws IOException {
		beginWritePair(field.name());
		writeMGenObject(o, (CustomType) field.typ());
	}

	@Override
	public void beginWrite(final MGenBase o, final int nFields) throws IOException {
	}

	@Override
	public void finishWrite() throws IOException {
	}

	@Override
	public void writeBooleanField(final boolean b, final Field field) throws IOException {
		beginWritePair(field.name());
		write(b);
	}

	@Override
	public void writeInt8Field(final byte b, final Field field) throws IOException {
		beginWritePair(field.name());
		write(b);
	}

	@Override
	public void writeInt16Field(final short s, final Field field) throws IOException {
		beginWritePair(field.name());
		write(s);
	}

	@Override
	public void writeInt32Field(final int i, final Field field) throws IOException {
		beginWritePair(field.name());
		write(i);
	}

	@Override
	public void writeInt64Field(final long l, final Field field) throws IOException {
		beginWritePair(field.name());
		write(l);
	}

	@Override
	public void writeFloat32Field(final float f, final Field field) throws IOException {
		beginWritePair(field.name());
		write(f);
	}

	@Override
	public void writeFloat64Field(final double d, final Field field) throws IOException {
		beginWritePair(field.name());
		write(d);
	}

	@Override
	public void writeStringField(final String s, final Field f) throws IOException {
		beginWritePair(f.name());
		writeQuoteEscaped(s);
	}

	@Override
	public void writeListField(final ArrayList<Object> list, final Field f) throws IOException {
		beginWritePair(f.name());
		writeList(list, (ListType) f.typ());
	}

	@Override
	public void writeMapField(final HashMap<Object, Object> m, final Field f) throws IOException {
		beginWritePair(f.name());
		writeMap(m, (MapType) f.typ());
	}

	@Override
	public void writeEnumField(Enum<?> e, Field f) throws IOException {
		beginWritePair(f.name());
		writeQuoteEscaped(String.valueOf(e));
	}

	@Override
	public void writeArrayField(Object array, Field f) throws IOException {
		beginWritePair(f.name());
		writeArray(array, (ArrayType) f.typ());
	}

	/*******************************************************************
	 * 
	 * 
	 * - - - - - - - - - - INTERNAL METHODS
	 * 
	 * 
	 ******************************************************************/

	protected void writeTypeId(final MGenBase o) throws IOException {
		beginWritePair("__t");
		writeQuoteEscaped(o._typeIds16BitBase64String());
	}

	protected final void beginWritePair(final String name) throws IOException {
		newEntry();
		writeName(name);
	}

	protected void writeName(final String name) throws IOException {
		writeQuoteEscaped(name);
		write(':');
	}

	protected void newEntry() throws IOException {
		if (m_iEntry[m_depth] > 0)
			write(',');
		m_iEntry[m_depth]++;
	}

	protected void endBlock(final String endString, final boolean hasContents) throws IOException {
		m_depth--;
		write(endString);
	}

	private void writeMGenObject(MGenBase o, CustomType expectType) throws IOException {
		if (o == null) {
			write("null");
		} else {
			beginBlock("{");
			if (needWriteTypeId(o, expectType))
				writeTypeId(o);
			o._accept(this, m_selection);
			endBlock("}", m_iEntry[m_depth] > 0);
		}
	}

	@SuppressWarnings("unchecked")
	private void writeObject(final Object o, final Type typ) throws IOException {

		if (o == null) {
			write("null");
			return;
		}

		switch (typ.typeEnum()) {
		case ENUM:
			writeQuoteEscaped(String.valueOf(o));
			break;
		case BOOL:
			write((Boolean) o);
			break;
		case INT8:
			write((Byte) o);
			break;
		case INT16:
			write((Short) o);
			break;
		case INT32:
			write((Integer) o);
			break;
		case INT64:
			write((Long) o);
			break;
		case FLOAT32:
			write((Float) o);
			break;
		case FLOAT64:
			write((Double) o);
			break;
		case STRING:
			writeQuoteEscaped((String) o);
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
		case UNKNOWN:
		case CUSTOM:
			writeMGenObject((MGenBase) o, (CustomType) typ);
			break;
		default:
			throw new SerializationException("Unknown type for writeObject");
		}

	}

	private void writeList(List<Object> objects, ListType typ) throws IOException {
		if (objects == null) {
			write("null");
		} else {
			beginBlock("[");
			for (Object o : objects) {
				newEntry();
				writeObject(o, typ.elementType());
			}
			endBlock("]", !objects.isEmpty());
		}
	}

	private void writeMap(Map<Object, Object> m, MapType typ) throws IOException {
		if (m == null) {
			write("null");
		} else {
			beginBlock("{");
			for (final Object key : m.keySet()) {
				beginWritePair(String.valueOf(key));
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
			case ENUM:
				writeArray((Enum<?>[]) o, typ);
				break;
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
			case UNKNOWN:
			case CUSTOM:
				writeObjectArray((Object[]) o, typ);
				break;
			default:
				throw new SerializationException("Don't know how to write array of type " + typ);
			}
		}
	}

	private void writeArray(Enum<?>[] o, ArrayType typ) throws IOException {

		if (o == null) {
			write("null");
			return;
		}

		beginBlock("[");
		for (int i = 0; i < o.length; i++) {
			newEntry();
			writeQuoteEscaped(String.valueOf(o[i]));
		}
		endBlock("]", o.length != 0);
	}

	private void writeArray(final boolean[] o) throws IOException {

		if (o == null) {
			write("null");
			return;
		}

		beginBlock("[");
		for (int i = 0; i < o.length; i++) {
			newEntry();
			write(o[i]);
		}
		endBlock("]", o.length != 0);
	}

	private void writeArray(final byte[] o) throws IOException {

		if (o == null) {
			write("null");
			return;
		}

		beginBlock("[");
		for (int i = 0; i < o.length; i++) {
			newEntry();
			write(o[i]);
		}
		endBlock("]", o.length != 0);
	}

	private void writeArray(final short[] o) throws IOException {

		if (o == null) {
			write("null");
			return;
		}

		beginBlock("[");
		for (int i = 0; i < o.length; i++) {
			newEntry();
			write(o[i]);
		}
		endBlock("]", o.length != 0);
	}

	private void writeArray(final int[] o) throws IOException {

		if (o == null) {
			write("null");
			return;
		}

		beginBlock("[");
		for (int i = 0; i < o.length; i++) {
			newEntry();
			write(o[i]);
		}
		endBlock("]", o.length != 0);
	}

	private void writeArray(final long[] o) throws IOException {

		if (o == null) {
			write("null");
			return;
		}

		beginBlock("[");
		for (int i = 0; i < o.length; i++) {
			newEntry();
			write(o[i]);
		}
		endBlock("]", o.length != 0);
	}

	private void writeArray(final float[] o) throws IOException {

		if (o == null) {
			write("null");
			return;
		}

		beginBlock("[");
		for (int i = 0; i < o.length; i++) {
			newEntry();
			write(o[i]);
		}
		endBlock("]", o.length != 0);
	}

	private void writeArray(final double[] o) throws IOException {

		if (o == null) {
			write("null");
			return;
		}

		beginBlock("[");
		for (int i = 0; i < o.length; i++) {
			newEntry();
			write(o[i]);
		}
		endBlock("]", o.length != 0);
	}

	private void writeArray(final String[] o) throws IOException {

		if (o == null) {
			write("null");
			return;
		}

		beginBlock("[");
		for (int i = 0; i < o.length; i++) {
			newEntry();
			writeQuoteEscaped(o[i]);
		}
		endBlock("]", o.length != 0);
	}

	private void writeObjectArray(final Object[] objects, final ArrayType typ) throws IOException {

		if (objects == null) {
			write("null");
			return;
		}

		beginBlock("[");
		for (int i = 0; i < objects.length; i++) {
			final Object o = objects[i];
			newEntry();
			writeObject(o, typ.elementType());
		}
		endBlock("]", objects.length != 0);
	}

	private void beginBlock(final String beginString) throws IOException {
		write(beginString);
		m_depth++;
		if (m_depth >= m_iEntry.length)
			throw new SerializationException("Max recursion depth reached");
		m_iEntry[m_depth] = 0;
	}

	private boolean needWriteTypeId(final MGenBase o, final CustomType expectType) {
		if (expectType == null || !m_compact)
			return true;
		return o._typeId() != expectType.typeId();
	}

	/**
	 * Copied and modified from json-simple. Unfortunately it's not accessible
	 * publicly in json-simple.
	 */
	private void writeQuoteEscaped(final String s) throws IOException {

		if (s == null) {
			write("null");
			return;
		}

		write('"');

		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				write('\\');
				write('"');
				break;
			case '\\':
				write('\\');
				write('\\');
				break;
			case '\b':
				write('\\');
				write('b');
				break;
			case '\f':
				write('\\');
				write('f');
				break;
			case '\n':
				write('\\');
				write('n');
				break;
			case '\r':
				write('\\');
				write('r');
				break;
			case '\t':
				write('\\');
				write('t');
				break;
			case '/':
				write('\\');
				write('/');
				break;
			default:
				// Reference: http://www.unicode.org/versions/Unicode5.1.0/
				if ((ch >= '\u0000' && ch <= '\u001F') || (ch >= '\u007F' && ch <= '\u009F')
						|| (ch >= '\u2000' && ch <= '\u20FF')) {
					String ss = Integer.toHexString(ch);
					write("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						write('0');
					}
					write(ss.toUpperCase());
				} else {
					write(ch);
				}
			}
		}

		write('"');

	}

}
