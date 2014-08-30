package se.culvertsoft.mgen.javapack.serialization;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.ArrayType;
import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.api.model.ListType;
import se.culvertsoft.mgen.api.model.MapType;
import se.culvertsoft.mgen.api.model.RuntimeClassType;
import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.javapack.classes.ClassRegistryBase;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.exceptions.SerializationException;
import se.culvertsoft.mgen.javapack.metadata.FieldVisitSelection;

/**
 * A class for writing data streams in JSON format.
 */
public class JsonWriter extends TextFormatWriter {

	/**
	 * The maximum recursion depth allowed for writing objects. While writing
	 * the contents of arrays, maps, objects, lists etc the current recursion
	 * depth is increased by one. The default value is DEFAULT_MAX_DEPTH.
	 */
	public static final int DEFAULT_MAX_DEPTH = 256;

	/**
	 * The default setting for writing in standard or compact mode. In compact
	 * mode, MGen object's type metadata is only written to the stream when it
	 * cannot be inferred by the Reader. The default setting is standard mode.
	 */
	public static final boolean DEFAULT_COMPACT = false;

	private final FieldVisitSelection m_selection;

	protected final boolean m_compact;
	protected final int[] m_iEntry;
	protected int m_depth = 0;

	/**
	 * Creates a new JsonWriter
	 * 
	 * @param outputStream
	 *            The data output stream to write to
	 * 
	 * @param classRegistry
	 *            The class registry to use
	 * 
	 * @param compact
	 *            If to use standard or compact mode. The default is standard
	 *            mode.
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

	/**
	 * Creates a new JsonWriter
	 * 
	 * @param outputStream
	 *            The data output stream to write to
	 * 
	 * @param classRegistry
	 *            The class registry to use
	 * 
	 * @param compact
	 *            If to use standard or compact mode. The default is standard
	 *            mode.
	 * 
	 * @param maxDepth
	 *            The maximum recursion depth allowed for writing objects. While
	 *            writing the contents of arrays, maps, objects, lists etc the
	 *            current recursion depth is increased by one. The default value
	 *            is DEFAULT_MAX_DEPTH.
	 */
	public JsonWriter(
			final OutputStream outputStream,
			final ClassRegistryBase classRegistry,
			final boolean compact,
			final int maxDepth) {
		this(outputStream, classRegistry, compact, maxDepth, false);
	}

	/**
	 * Creates a new JsonWriter
	 * 
	 * @param outputStream
	 *            The data output stream to write to
	 * 
	 * @param classRegistry
	 *            The class registry to use
	 * 
	 * @param compact
	 *            If to use standard or compact mode. The default is standard
	 *            mode.
	 */
	public JsonWriter(
			final OutputStream outputStream,
			final ClassRegistryBase classRegistry,
			final boolean compact) {
		this(outputStream, classRegistry, compact, DEFAULT_MAX_DEPTH);
	}

	/**
	 * Creates a new JsonWriter
	 * 
	 * @param outputStream
	 *            The data output stream to write to
	 * 
	 * @param classRegistry
	 *            The class registry to use
	 */
	public JsonWriter(final OutputStream outputStream, final ClassRegistryBase classRegistry) {
		this(outputStream, classRegistry, DEFAULT_COMPACT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JsonWriter setOutput(final OutputStream stream) {
		super.setOutput(stream);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeObject(final MGenBase o) throws IOException {
		m_depth = 0;
		writeMGenObject(o, null);
		flush();
	}

	/**
	 * Convenience method for writing an object directly to a JSON string
	 * 
	 * @param o
	 *            The object to write
	 * 
	 * @return The object written to a string, in JSON format
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	public String writeObjectToString(final MGenBase o) throws IOException {
		m_depth = 0;
		setWriteToStream(false);
		writeMGenObject(o, null);
		return finish();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeMGenObjectField(MGenBase o, Field field) throws IOException {
		beginWritePair(field.name());
		writeMGenObject(o, (RuntimeClassType) field.typ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beginWrite(final MGenBase o, final int nFields) throws IOException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finishWrite() throws IOException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeBooleanField(final boolean b, final Field field) throws IOException {
		beginWritePair(field.name());
		write(b);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeInt8Field(final byte b, final Field field) throws IOException {
		beginWritePair(field.name());
		write(b);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeInt16Field(final short s, final Field field) throws IOException {
		beginWritePair(field.name());
		write(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeInt32Field(final int i, final Field field) throws IOException {
		beginWritePair(field.name());
		write(i);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeInt64Field(final long l, final Field field) throws IOException {
		beginWritePair(field.name());
		write(l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeFloat32Field(final float f, final Field field) throws IOException {
		beginWritePair(field.name());
		write(f);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeFloat64Field(final double d, final Field field) throws IOException {
		beginWritePair(field.name());
		write(d);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeStringField(final String s, final Field f) throws IOException {
		beginWritePair(f.name());
		writeQuoteEscaped(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeListField(final ArrayList<Object> list, final Field f) throws IOException {
		beginWritePair(f.name());
		writeList(list, (ListType) f.typ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeMapField(final HashMap<Object, Object> m, final Field f) throws IOException {
		beginWritePair(f.name());
		writeMap(m, (MapType) f.typ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeEnumField(Enum<?> e, Field f) throws IOException {
		beginWritePair(f.name());
		writeQuoteEscaped(String.valueOf(e));
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * Internal method for 16bit writing type ids of an object in base64 form.
	 * 
	 * @param o
	 *            The object to write type ids of
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	protected void writeTypeIds(final MGenBase o) throws IOException {
		beginWritePair("__t");
		writeQuoteEscaped(o._typeIds16BitBase64String());
	}

	/**
	 * Internal method for writing the beginning of a key-value entry
	 * 
	 * @param name
	 *            The key/name of the key-value entry
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	protected final void beginWritePair(final String name) throws IOException {
		newEntry();
		writeName(name);
	}

	/**
	 * Internal method for writing the key/name of a key-value entry
	 * 
	 * @param name
	 *            The key/name of the key-value entry
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	protected void writeName(final String name) throws IOException {
		writeQuoteEscaped(name);
		write(':');
	}

	/**
	 * Internal method for beginning writing a new key-value entry
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	protected void newEntry() throws IOException {
		if (m_iEntry[m_depth] > 0)
			write(',');
		m_iEntry[m_depth]++;
	}

	/**
	 * Internal method for ending the writing of a block (array, list, object,
	 * map etc)
	 * 
	 * @param endString
	 *            The finishing string (such as ']' for arrays)
	 * 
	 * @param hasContents
	 *            If the block was non-empty
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	protected void endBlock(final String endString, final boolean hasContents) throws IOException {
		m_depth--;
		write(endString);
	}

	/**
	 * Internal method for writing an MGen object
	 * 
	 * @param o
	 *            The MGen object to write
	 * 
	 * @param expectType
	 *            Type meta data about the object the write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeMGenObject(MGenBase o, RuntimeClassType expectType) throws IOException {
		if (o == null) {
			write("null");
		} else {
			beginBlock("{");
			if (needWriteTypeId(o, expectType))
				writeTypeIds(o);
			o._accept(this, m_selection);
			endBlock("}", m_iEntry[m_depth] > 0);
		}
	}

	/**
	 * Internal method for writing a generic object
	 * 
	 * @param o
	 *            The object to write
	 * 
	 * @param typ
	 *            Type meta data about the object to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
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
		case CLASS:
			writeMGenObject((MGenBase) o, (RuntimeClassType) typ);
			break;
		default:
			throw new SerializationException("Unknown type for writeObject");
		}

	}

	/**
	 * Internal method for writing a list
	 * 
	 * @param list
	 *            The list to write
	 * 
	 * @param typ
	 *            Type meta data about what to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeList(List<Object> list, ListType typ) throws IOException {
		if (list == null) {
			write("null");
		} else {
			beginBlock("[");
			for (Object o : list) {
				newEntry();
				writeObject(o, typ.elementType());
			}
			endBlock("]", !list.isEmpty());
		}
	}

	/**
	 * Internal method for writing a map
	 * 
	 * @param map
	 *            The map to write
	 * 
	 * @param typ
	 *            Type meta data about what to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeMap(Map<Object, Object> map, MapType typ) throws IOException {
		if (map == null) {
			write("null");
		} else {
			beginBlock("{");
			for (final Object key : map.keySet()) {
				beginWritePair(String.valueOf(key));
				writeObject(map.get(key), typ.valueType());
			}
			endBlock("}", !map.isEmpty());
		}
	}

	/**
	 * Internal method for writing an array
	 * 
	 * @param array
	 *            The array to write
	 * 
	 * @param typ
	 *            Type meta data about what to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeArray(Object array, ArrayType typ) throws IOException {
		if (array == null) {
			write("null");
		} else {
			switch (typ.elementType().typeEnum()) {
			case ENUM:
				writeArray((Enum<?>[]) array, typ);
				break;
			case BOOL:
				writeArray((boolean[]) array);
				break;
			case INT8:
				writeArray((byte[]) array);
				break;
			case INT16:
				writeArray((short[]) array);
				break;
			case INT32:
				writeArray((int[]) array);
				break;
			case INT64:
				writeArray((long[]) array);
				break;
			case FLOAT32:
				writeArray((float[]) array);
				break;
			case FLOAT64:
				writeArray((double[]) array);
				break;
			case STRING:
				writeArray((String[]) array);
				break;
			case ARRAY:
			case LIST:
			case MAP:
			case CLASS:
				writeObjectArray((Object[]) array, typ);
				break;
			default:
				throw new SerializationException("Don't know how to write array of type " + typ);
			}
		}
	}

	/**
	 * Private method for writing an enum array
	 * 
	 * @param array
	 *            The array to write
	 * 
	 * @param typ
	 *            Type meta data about what to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeArray(Enum<?>[] array, ArrayType typ) throws IOException {

		if (array == null) {
			write("null");
			return;
		}

		beginBlock("[");
		for (int i = 0; i < array.length; i++) {
			newEntry();
			writeQuoteEscaped(String.valueOf(array[i]));
		}
		endBlock("]", array.length != 0);
	}

	/**
	 * Private method for writing a boolean array
	 * 
	 * @param array
	 *            The array to write
	 * 
	 * @param typ
	 *            Type meta data about what to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
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

	/**
	 * Private method for writing an int8 array
	 * 
	 * @param array
	 *            The array to write
	 * 
	 * @param typ
	 *            Type meta data about what to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
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

	/**
	 * Private method for writing an int16 array
	 * 
	 * @param array
	 *            The array to write
	 * 
	 * @param typ
	 *            Type meta data about what to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
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

	/**
	 * Private method for writing an int32 array
	 * 
	 * @param array
	 *            The array to write
	 * 
	 * @param typ
	 *            Type meta data about what to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
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

	/**
	 * Private method for writing an int64 array
	 * 
	 * @param array
	 *            The array to write
	 * 
	 * @param typ
	 *            Type meta data about what to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
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

	/**
	 * Private method for writing a float32 array
	 * 
	 * @param array
	 *            The array to write
	 * 
	 * @param typ
	 *            Type meta data about what to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
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

	/**
	 * Private method for writing a float64 array
	 * 
	 * @param array
	 *            The array to write
	 * 
	 * @param typ
	 *            Type meta data about what to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
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

	/**
	 * Private method for writing a string array
	 * 
	 * @param array
	 *            The array to write
	 * 
	 * @param typ
	 *            Type meta data about what to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
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

	/**
	 * Private method for writing a generic object array
	 * 
	 * @param array
	 *            The array to write
	 * 
	 * @param typ
	 *            Type meta data about what to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
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

	/**
	 * Internal helper method for starting a new block, such as an array, list,
	 * map or object.
	 * 
	 * @param beginString
	 *            The starting string of the block (such as '[' for beginning an
	 *            array)
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void beginBlock(final String beginString) throws IOException {
		write(beginString);
		m_depth++;
		if (m_depth >= m_iEntry.length)
			throw new SerializationException("Max recursion depth reached");
		m_iEntry[m_depth] = 0;
	}

	/**
	 * Internal helper method for checking if we need to write type ids for the
	 * MGen object to be written
	 * 
	 * @param o
	 *            The MGen object to be written
	 * 
	 * @param expectType
	 *            Type meta data about the object to be written
	 * 
	 * @return If we need to write type meta data about the object to be written
	 */
	private boolean needWriteTypeId(final MGenBase o, final RuntimeClassType expectType) {
		if (expectType == null || !m_compact)
			return true;
		return o._typeId() != expectType.typeId();
	}

	/**
	 * Internal method for writing an escaped string, following the JSON string
	 * escape rules.
	 * 
	 * Copied and modified from json-simple. Unfortunately it's not accessible
	 * publicly in json-simple.
	 * 
	 * @param s
	 *            The string to write escaped
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
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
