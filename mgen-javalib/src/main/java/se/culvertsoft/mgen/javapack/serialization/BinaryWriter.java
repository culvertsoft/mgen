package se.culvertsoft.mgen.javapack.serialization;

import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_BOOL;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_CLASS;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_FLOAT32;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_FLOAT64;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_INT16;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_INT32;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_INT64;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_INT8;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_LIST;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_MAP;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_STRING;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
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
import se.culvertsoft.mgen.javapack.util.FastByteBuffer;
import se.culvertsoft.mgen.javapack.util.Varint;

/**
 * A class for writing data streams in the MGen binary wire format.
 */
public class BinaryWriter extends BuiltInWriter {

	/**
	 * All built-in MGen writers and wire-formats support what we call compact
	 * and standard modes. In standard mode MGen objects are always prepended by
	 * a series of 16 bit ids which uniquely identify the class of the object
	 * being written. Compact mode turns off writing of these IDs where they can
	 * be inferred by the data model/reader (e.g. the field is of a specific
	 * object type and the object being written is of exactly this type - not a
	 * sub type).
	 */
	public static final boolean DEFAULT_COMPACT = false;

	/**
	 * BinaryWriter objects maintain internal buffers for fast writing, which is
	 * flushed after each finished object write. This is the default size of
	 * that buffer.
	 */
	public static final int FLUSH_SIZE = 256;

	private final FastByteBuffer m_buffer;
	private OutputStream m_streamOut;
	private final boolean m_compact;
	private long m_expectType;

	/**
	 * Creates a new binary writer.
	 * 
	 * @param stream
	 *            The data output stream to write to
	 * 
	 * @param classRegistry
	 *            The class registry to use
	 * 
	 * @param compact
	 *            If objects should be written in compact or standard mode. See
	 *            DEFAULT_COMPACT.
	 */
	public BinaryWriter(
			final OutputStream stream,
			final ClassRegistryBase classRegistry,
			final boolean compact) {
		super(classRegistry);
		m_buffer = new FastByteBuffer(FLUSH_SIZE * 2);
		m_streamOut = stream;
		m_compact = compact;
		m_expectType = -1;
	}

	/**
	 * Creates a new binary writer.
	 * 
	 * @param stream
	 *            The data output stream to write to
	 * 
	 * @param classRegistry
	 *            The class registry to use
	 */
	public BinaryWriter(final OutputStream stream, final ClassRegistryBase classRegistry) {
		this(stream, classRegistry, DEFAULT_COMPACT);
	}

	/**
	 * Method to replace the internal output stream with a new one to write to
	 * 
	 * @param stream
	 *            The new output stream to write to
	 * 
	 * @return This writer
	 */
	public BinaryWriter setOutput(final OutputStream stream) {
		m_streamOut = stream;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeObject(final MGenBase o) throws IOException {
		m_expectType = -1;
		m_buffer.clear();
		writeMGenObject(o, true, null);
		flush();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beginWrite(final MGenBase object, final int nFields) throws IOException {

		if (shouldOmitIds(object)) {
			writeSize((nFields << 2) | 0x02);
		} else {
			final short[] ids = object._typeIds16Bit();
			writeSize((ids.length << 2) | 0x01);
			for (final short id : ids)
				writeInt16(id, false);
			writeSize(nFields);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finishWrite() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeBooleanField(final boolean b, final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_BOOL);
		writeBoolean(b, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeInt8Field(final byte b, final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_INT8);
		writeInt8(b, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeInt16Field(final short s, final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_INT16);
		writeInt16(s, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeInt32Field(final int i, final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_INT32);
		writeInt32(i, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeInt64Field(final long l, final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_INT64);
		writeInt64(l, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeFloat32Field(final float f, final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_FLOAT32);
		writeFloat32(f, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeFloat64Field(final double d, final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_FLOAT64);
		writeFloat64(d, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeStringField(final String s, final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_STRING);
		writeString(s, false);
	}

	@Override
	public void writeListField(final List<Object> list, final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_LIST);
		writeList(list, (ListType) field.typ(), false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeMapField(final Map<Object, Object> map, final Field field)
			throws IOException {
		writeFieldStart(field.id(), TAG_MAP);
		writeMap(map, (MapType) field.typ(), false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeArrayField(final Object array, final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_LIST);
		writeArray(array, (ArrayType) field.typ(), false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeEnumField(final Enum<?> e, final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_STRING);
		writeEnum(e, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeMGenObjectField(final MGenBase o, final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_CLASS);
		writeMGenObject(o, false, (RuntimeClassType) field.typ());
	}

	/*******************************************************************
	 * 
	 * 
	 * - - - - - - - - - - INTERNAL METHODS
	 * 
	 ******************************************************************/

	/**
	 * Internal method for writing an MGen object
	 * 
	 * @param o
	 *            The object to write
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @param typ
	 *            Meta data information on the object to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeMGenObject(final MGenBase o, final boolean tag, final RuntimeClassType typ)
			throws IOException {

		if (tag)
			writeTypeTag(TAG_CLASS);

		if (o != null) {
			m_expectType = typ != null ? typ.typeId() : 0;
			o._accept(this, FieldVisitSelection.ALL_SET_NONTRANSIENT);
		} else {
			writeByte(0);
		}

	}

	/**
	 * Internal method called before writing a field. Writes a field id and
	 * binary type tag.
	 * 
	 * @param id
	 *            The field id
	 * 
	 * @param tag
	 *            The binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeFieldStart(final short id, final byte tag) throws IOException {
		writeInt16(id, false);
		writeTypeTag(tag);
	}

	/**
	 * Internal method for writing an enum
	 * 
	 * @param e
	 *            The enum value to write
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeEnum(final Enum<?> e, final boolean tag) throws IOException {
		if (tag)
			writeTypeTag(TAG_STRING);
		writeString(String.valueOf(e), tag);
	}

	/**
	 * Internal method for writing a boolean
	 * 
	 * @param b
	 *            The value to write
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeBoolean(final boolean b, final boolean tag) throws IOException {
		if (tag)
			writeTypeTag(TAG_BOOL);
		writeByte(b ? 1 : 0);
	}

	/**
	 * Internal method for writing an int8
	 * 
	 * @param b
	 *            The value to write
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeInt8(final int b, final boolean tag) throws IOException {
		if (tag)
			writeTypeTag(TAG_INT8);
		writeByte(b);
	}

	/**
	 * Internal method for writing an int16
	 * 
	 * @param s
	 *            The value to write
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeInt16(final short s, final boolean tag) throws IOException {
		if (tag)
			writeTypeTag(TAG_INT16);
		writeRawInt16(s);
	}

	/**
	 * Internal method for writing an int32
	 * 
	 * @param i
	 *            The value to write
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeInt32(final int i, final boolean tag) throws IOException {
		if (tag)
			writeTypeTag(TAG_INT32);
		writeSignedVarint32(i);
	}

	/**
	 * Internal method for writing an int64
	 * 
	 * @param l
	 *            The value to write
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeInt64(final long l, final boolean tag) throws IOException {
		if (tag)
			writeTypeTag(TAG_INT64);
		writeSignedVarint64(l);
	}

	/**
	 * Internal method for writing a float32
	 * 
	 * @param f
	 *            The value to write
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeFloat32(final float f, final boolean tag) throws IOException {
		if (tag)
			writeTypeTag(TAG_FLOAT32);
		writeRawInt32(Float.floatToRawIntBits(f));
	}

	/**
	 * Internal method for writing a float64
	 * 
	 * @param d
	 *            The value to write
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeFloat64(final double d, final boolean tag) throws IOException {
		if (tag)
			writeTypeTag(TAG_FLOAT64);
		writeRawInt64(Double.doubleToRawLongBits(d));
	}

	/**
	 * Internal method for writing a string
	 * 
	 * @param s
	 *            The value to write
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeString(final String s, final boolean tag) throws IOException {
		if (tag)
			writeTypeTag(TAG_STRING);
		if (s != null && !s.isEmpty()) {
			m_stringEncoder.encode(s);
			writeSize(m_stringEncoder.size());
			writeBytes(m_stringEncoder.data(), m_stringEncoder.size());
		} else {
			writeSize(0);
		}
	}

	/**
	 * Internal method for writing a list
	 * 
	 * @param list
	 *            The list to write
	 * 
	 * @param typ
	 *            Meta data information about the list
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeList(final List<Object> list, final ListType typ, final boolean tag)
			throws IOException {

		if (tag)
			writeTypeTag(TAG_LIST);

		writeElements(false, list, typ.elementType());

	}

	/**
	 * Internal method for writing list elements
	 * 
	 * @param doWriteListTag
	 *            If to write a binary type tag
	 * 
	 * @param list
	 *            The list to write
	 * 
	 * @param elementType
	 *            Meta data information on the element types
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void writeElements(
			final boolean doWriteListTag,
			final Collection<Object> list,
			final Type elementType) throws IOException {

		if (doWriteListTag)
			writeTypeTag(TAG_LIST);

		if (list != null && !list.isEmpty()) {

			writeSize(list.size());

			writeTypeTag(elementType.typeTag());

			switch (elementType.typeEnum()) {
			case ENUM: {
				final Collection<Enum<?>> l = (Collection) list;
				for (final Enum<?> e : l)
					writeEnum(e, false);
				break;
			}
			case BOOL: {
				final Collection<Boolean> l = (Collection) list;
				for (final Boolean e : l)
					writeBoolean(e != null ? e : false, false);
				break;
			}
			case INT8: {
				final Collection<Byte> l = (Collection) list;
				for (final Byte e : l)
					writeInt8(e != null ? e : (byte) 0, false);
				break;
			}
			case INT16: {
				final Collection<Short> l = (Collection) list;
				for (final Short e : l)
					writeInt16(e != null ? e : (short) 0, false);
				break;
			}
			case INT32: {
				final Collection<Integer> l = (Collection) list;
				for (final Integer e : l)
					writeInt32(e != null ? e : 0, false);
				break;
			}
			case INT64: {
				final Collection<Long> l = (Collection) list;
				for (final Long e : l)
					writeInt64(e != null ? e : 0, false);
				break;
			}
			case FLOAT32: {
				final Collection<Float> l = (Collection) list;
				for (final Float e : l)
					writeFloat32(e != null ? e : 0.0f, false);
				break;
			}
			case FLOAT64: {
				final Collection<Double> l = (Collection) list;
				for (final Double e : l)
					writeFloat64(e != null ? e : 0.0, false);
				break;
			}
			case STRING: {
				final Collection<String> l = (Collection) list;
				for (final String e : l)
					writeString(e, false);
				break;
			}
			default:
				for (final Object o : list)
					writeObject(o, elementType, false);
				break;
			}

		} else {
			writeSize(0);
		}

	}

	/**
	 * Internal method for writing a map
	 * 
	 * @param map
	 *            The map to write
	 * 
	 * @param typ
	 *            Meta data type information on the map to write
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeMap(final Map<Object, Object> map, final MapType typ, final boolean tag)
			throws IOException {

		if (tag)
			writeTypeTag(TAG_MAP);

		if (map != null && !map.isEmpty()) {

			writeSize(map.size());

			final Type keyType = typ.keyType();
			final Type valueType = typ.valueType();

			writeTypeTag(keyType.typeTag());
			writeTypeTag(valueType.typeTag());

			for (Map.Entry<Object, Object> entry : map.entrySet()) {
				writeObject(entry.getKey(), keyType, false);
				writeObject(entry.getValue(), valueType, false);
			}

		} else {
			writeSize(0);
		}

	}

	/**
	 * Internal method for writing an array
	 * 
	 * @param array
	 *            The array to write
	 * 
	 * @param typ
	 *            Meta data type information on the array to write
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeArray(final Object array, final ArrayType typ, final boolean tag)
			throws IOException {

		if (tag)
			writeTypeTag(TAG_LIST);

		if (array != null) {

			final Type elementType = typ.elementType();

			switch (elementType.typeEnum()) {
			case ENUM:
				writeEnumArray((Enum<?>[]) array, false);
				break;
			case BOOL:
				writeBooleanArray((boolean[]) array, false);
				break;
			case INT8:
				writeInt8Array((byte[]) array, false);
				break;
			case INT16:
				writeInt16Array((short[]) array, false);
				break;
			case INT32:
				writeInt32Array((int[]) array, false);
				break;
			case INT64:
				writeInt64Array((long[]) array, false);
				break;
			case FLOAT32:
				writeFloat32Array((float[]) array, false);
				break;
			case FLOAT64:
				writeFloat64Array((double[]) array, false);
				break;
			case STRING:
				writeStringArray((String[]) array, false);
				break;
			default:
				writeObjectArray((Object[]) array, elementType, false);
				break;
			}

		} else {
			writeSize(0);
		}

	}

	/**
	 * High performance array writing method for enums
	 * 
	 * @param array
	 *            The array to write
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeEnumArray(final Enum<?>[] array, final boolean tag) throws IOException {

		if (tag)
			writeTypeTag(TAG_LIST);

		if (array != null && array.length != 0) {

			writeSize(array.length);

			writeTypeTag(TAG_STRING);

			for (final Enum<?> e : array)
				writeEnum(e, false);

		} else {
			writeSize(0);
		}
	}

	/**
	 * High performance array writing method for booleans
	 * 
	 * @param array
	 *            The array to write
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeBooleanArray(final boolean[] array, final boolean tag) throws IOException {

		if (tag)
			writeTypeTag(TAG_LIST);

		if (array != null && array.length != 0) {

			writeSize(array.length);

			writeTypeTag(TAG_BOOL);

			for (final boolean b : array)
				writeBoolean(b, false);

		} else {
			writeSize(0);
		}

	}

	/**
	 * High performance array writing method for int8s
	 * 
	 * @param array
	 *            The array to write
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeInt8Array(final byte[] array, final boolean tag) throws IOException {

		if (tag)
			writeTypeTag(TAG_LIST);

		if (array != null && array.length != 0) {

			writeSize(array.length);

			writeTypeTag(TAG_INT8);
			writeBytes(array);

		} else {
			writeSize(0);
		}

	}

	/**
	 * High performance array writing method for int16s
	 * 
	 * @param array
	 *            The array to write
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeInt16Array(final short[] array, final boolean tag) throws IOException {

		if (tag)
			writeTypeTag(TAG_LIST);

		if (array != null && array.length != 0) {

			writeSize(array.length);

			writeTypeTag(TAG_INT16);

			for (final short s : array)
				writeInt16(s, false);

		} else {
			writeSize(0);
		}

	}

	/**
	 * High performance array writing method for int32s
	 * 
	 * @param array
	 *            The array to write
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeInt32Array(final int[] array, final boolean tag) throws IOException {

		if (tag)
			writeTypeTag(TAG_LIST);

		if (array != null && array.length != 0) {

			writeSize(array.length);

			writeTypeTag(TAG_INT32);

			for (final int i : array)
				writeInt32(i, false);

		} else {
			writeSize(0);
		}

	}

	/**
	 * High performance array writing method for int64s
	 * 
	 * @param array
	 *            The array to write
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeInt64Array(final long[] array, final boolean tag) throws IOException {

		if (tag)
			writeTypeTag(TAG_LIST);

		if (array != null && array.length != 0) {

			writeSize(array.length);

			writeTypeTag(TAG_INT64);

			for (final long l : array)
				writeInt64(l, false);

		} else {
			writeSize(0);
		}

	}

	/**
	 * High performance array writing method for float32s
	 * 
	 * @param array
	 *            The array to write
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeFloat32Array(final float[] array, final boolean tag) throws IOException {

		if (tag)
			writeTypeTag(TAG_LIST);

		if (array != null && array.length != 0) {

			writeSize(array.length);

			writeTypeTag(TAG_FLOAT32);

			for (final float f : array)
				writeFloat32(f, false);

		} else {
			writeSize(0);
		}

	}

	/**
	 * High performance array writing method for strings
	 * 
	 * @param array
	 *            The array to write
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeStringArray(final String[] array, final boolean tag) throws IOException {

		if (tag)
			writeTypeTag(TAG_LIST);

		if (array != null && array.length != 0) {

			writeSize(array.length);

			writeTypeTag(TAG_STRING);

			for (final String s : array)
				writeString(s, false);

		} else {
			writeSize(0);
		}

	}

	/**
	 * High performance array writing method for float64s
	 * 
	 * @param array
	 *            The array to write
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeFloat64Array(final double[] array, final boolean tag) throws IOException {

		if (tag)
			writeTypeTag(TAG_LIST);

		if (array != null && array.length != 0) {

			writeSize(array.length);

			writeTypeTag(TAG_FLOAT64);

			for (final double f : array)
				writeFloat64(f, false);

		} else {
			writeSize(0);
		}

	}

	/**
	 * High performance array writing method for generic objects
	 * 
	 * @param array
	 *            The array to write
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeObjectArray(final Object[] array, final Type elementType, final boolean tag)
			throws IOException {

		if (tag)
			writeTypeTag(TAG_LIST);

		if (array != null && array.length != 0) {

			writeSize(array.length);

			writeTypeTag(elementType.typeTag());

			for (final Object o : array)
				writeObject(o, elementType, false);

		} else {
			writeSize(0);
		}
	}

	/**
	 * Internal methods for flushing the internal buffer and write its data to
	 * the underlying data output stream
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void flush() throws IOException {
		if (m_buffer.nonEmpty()) {
			m_streamOut.write(m_buffer.data(), 0, m_buffer.size());
			m_buffer.clear();
		}
	}

	/**
	 * Internal method for checking if we need to flush the internal buffer to
	 * the underlying data output stream. Then performs the flush if the
	 * condition is met.
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void checkFlush() throws IOException {
		if (m_buffer.size() >= FLUSH_SIZE)
			flush();
	}

	/**
	 * Internal method for writing a byte
	 * 
	 * @param b
	 *            The byte to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeByte(int b) throws IOException {
		m_buffer.write(b);
		checkFlush();
	}

	/**
	 * Internal method for writing an array/blob of bytes
	 * 
	 * @param data
	 *            The bytes to write
	 * 
	 * @param offset
	 *            An offset in the bytes to write
	 * 
	 * @param sz
	 *            The number of bytes to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeBytes(final byte[] data, final int offset, final int sz) throws IOException {
		if (sz >= FLUSH_SIZE) {
			flush();
			m_streamOut.write(data, offset, sz);
		} else {
			m_buffer.write(data, offset, sz);
			checkFlush();
		}
	}

	/**
	 * Internal method for writing an array/blob of bytes
	 * 
	 * @param data
	 *            The bytes to write
	 * 
	 * @param sz
	 *            The number of bytes to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeBytes(final byte[] data, final int sz) throws IOException {
		writeBytes(data, 0, sz);
	}

	/**
	 * Internal method for writing an array/blob of bytes
	 * 
	 * @param data
	 *            The bytes to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeBytes(final byte[] data) throws IOException {
		writeBytes(data, 0, data.length);
	}

	/**
	 * Internal method for writing a fixed size big-endian 16 bit integer
	 * 
	 * @param s
	 *            The int16 to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeRawInt16(short s) throws IOException {
		writeByte(s >>> 8);
		writeByte(s >>> 0);
	}

	/**
	 * Internal method for writing a fixed size big-endian 32 bit integer
	 * 
	 * @param s
	 *            The int32 to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeRawInt32(int v) throws IOException {
		writeByte(v >>> 24);
		writeByte(v >>> 16);
		writeByte(v >>> 8);
		writeByte(v >>> 0);
	}

	/**
	 * Internal method for writing a fixed size big-endian 64 bit integer
	 * 
	 * @param s
	 *            The int64 to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeRawInt64(long v) throws IOException {
		writeByte((int) (v >>> 56));
		writeByte((int) (v >>> 48));
		writeByte((int) (v >>> 40));
		writeByte((int) (v >>> 32));
		writeByte((int) (v >>> 24));
		writeByte((int) (v >>> 16));
		writeByte((int) (v >>> 8));
		writeByte((int) (v >>> 0));
	}

	/**
	 * Internal method for writing a signed 32bit varint
	 * 
	 * @param i
	 *            The value to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeSignedVarint32(final int i) throws IOException {
		Varint.writeSignedVarInt(i, m_buffer);
		checkFlush();
	}

	/**
	 * Internal method for writing a signed 64bit varint
	 * 
	 * @param i
	 *            The value to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeSignedVarint64(final long l) throws IOException {
		Varint.writeSignedVarLong(l, m_buffer);
		checkFlush();
	}

	/**
	 * Internal method for writing an unsigned 32bit varint
	 * 
	 * @param i
	 *            The value to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeUnsignedVarint32(final int i) throws IOException {
		Varint.writeUnsignedVarInt(i, m_buffer);
		checkFlush();
	}

	/**
	 * Internal method for writing a size value
	 * 
	 * @param i
	 *            The value to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeSize(final int i) throws IOException {
		writeUnsignedVarint32(i);
	}

	/**
	 * Internal method for writing a generic object
	 * 
	 * @param o
	 *            The object to write
	 * 
	 * @param typ
	 *            Type metadata information on the object the writ
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	@SuppressWarnings("unchecked")
	private void writeObject(final Object o, final Type typ, boolean tag) throws IOException {

		switch (typ.typeEnum()) {
		case ENUM:
			writeEnum((Enum<?>) o, tag);
			break;
		case BOOL:
			writeBoolean(o != null ? (Boolean) o : false, tag);
			break;
		case INT8:
			writeInt8(o != null ? (Byte) o : 0, tag);
			break;
		case INT16:
			writeInt16(o != null ? (Short) o : 0, tag);
			break;
		case INT32:
			writeInt32(o != null ? (Integer) o : 0, tag);
			break;
		case INT64:
			writeInt64(o != null ? (Long) o : 0, tag);
			break;
		case FLOAT32:
			writeFloat32(o != null ? (Float) o : 0.0f, tag);
			break;
		case FLOAT64:
			writeFloat64(o != null ? (Double) o : 0.0, tag);
			break;
		case ARRAY:
			writeArray(o, (ArrayType) typ, tag);
			break;
		case STRING:
			writeString((String) o, tag);
			break;
		case LIST:
			writeList((List<Object>) o, (ListType) typ, tag);
			break;
		case MAP:
			writeMap((Map<Object, Object>) o, (MapType) typ, tag);
			break;
		case CLASS:
			writeMGenObject((MGenBase) o, tag, (RuntimeClassType) typ);
			break;
		default:
			throw new SerializationException("Unknown type tag for writeObject");
		}

	}

	/**
	 * Internal method for writing a binary type tag
	 * 
	 * @param tag
	 *            If to write a binary type tag
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	private void writeTypeTag(byte tag) throws IOException {
		writeByte(tag);
	}

	/**
	 * Internal convenience method for testing if we need to write type id
	 * metadata for the object which is currently to be written.
	 * 
	 * @param o
	 *            The object that we should or should not write ids for
	 * 
	 * @return if we should omit writing ids
	 */
	private boolean shouldOmitIds(final MGenBase o) {
		return m_compact && o._typeId() == m_expectType;
	}

}
