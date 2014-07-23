package se.culvertsoft.mgen.javapack.serialization;

import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_BOOL;
import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_CUSTOM;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import se.culvertsoft.mgen.api.model.ArrayType;
import se.culvertsoft.mgen.api.model.CustomType;
import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.api.model.ListType;
import se.culvertsoft.mgen.api.model.MapType;
import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.javapack.classes.ClassRegistryBase;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.exceptions.SerializationException;
import se.culvertsoft.mgen.javapack.util.FastByteBuffer;
import se.culvertsoft.mgen.javapack.util.Varint;

public class BinaryWriter extends BuiltInWriter {

	public static final boolean DEFAULT_COMPACT = false;
	public static final int FLUSH_SIZE = 256;

	private final FastByteBuffer m_buffer;
	private final OutputStream m_streamOut;
	private final boolean m_compact;
	private long m_expectType;

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

	public BinaryWriter(final OutputStream stream, final ClassRegistryBase classRegistry) {
		this(stream, classRegistry, DEFAULT_COMPACT);
	}

	@Override
	public void writeObject(final MGenBase o) throws IOException {
		m_expectType = -1;
		m_buffer.clear();
		writeMGenObject(o, true, null);
		flush();
	}

	@Override
	public void beginWrite(final MGenBase object, final int nFieldsSet, final int nFieldsTotal)
			throws IOException {

		if (shouldOmitIds(object)) {
			writeSize((nFieldsSet << 2) | 0x02);
		} else {
			final short[] ids = object._typeIds16Bit();
			writeSize((ids.length << 2) | 0x01);
			for (final short id : ids)
				writeInt16(id, false);
			writeSize(nFieldsSet);
		}

	}

	@Override
	public void finishWrite() {
	}

	@Override
	public void writeBooleanField(final boolean b, final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_BOOL);
		writeBoolean(b, false);
	}

	@Override
	public void writeInt8Field(final byte b, final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_INT8);
		writeInt8(b, false);
	}

	@Override
	public void writeInt16Field(final short s, final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_INT16);
		writeInt16(s, false);
	}

	@Override
	public void writeInt32Field(final int i, final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_INT32);
		writeInt32(i, false);
	}

	@Override
	public void writeInt64Field(final long l, final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_INT64);
		writeInt64(l, false);
	}

	@Override
	public void writeFloat32Field(final float f, final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_FLOAT32);
		writeFloat32(f, false);
	}

	@Override
	public void writeFloat64Field(final double d, final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_FLOAT64);
		writeFloat64(d, false);
	}

	@Override
	public void writeStringField(final String s, final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_STRING);
		writeString(s, false);
	}

	@Override
	public void writeListField(final ArrayList<Object> list, final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_LIST);
		writeList(list, (ListType) field.typ(), false);
	}

	@Override
	public void writeMapField(final HashMap<Object, Object> map, final Field field)
			throws IOException {
		writeFieldStart(field.id(), TAG_MAP);
		writeMap(map, (MapType) field.typ(), false);
	}

	@Override
	public void writeArrayField(final Object arrayObj, final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_LIST);
		writeArray(arrayObj, (ArrayType) field.typ(), false);
	}

	@Override
	public void writeEnumField(final Enum<?> e, final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_STRING);
		writeEnum(e, false);
	}

	@Override
	public void writeMGenObjectField(final MGenBase o, final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_CUSTOM);
		writeMGenObject(o, false, (CustomType) field.typ());
	}

	/*******************************************************************
	 * 
	 * 
	 * - - - - - - - - - - INTERNAL METHODS
	 * 
	 ******************************************************************/

	private void writeMGenObject(final MGenBase o, final boolean tag, final CustomType typ)
			throws IOException {

		if (tag)
			writeTypeTag(TAG_CUSTOM);

		if (o != null) {
			m_expectType = typ != null ? typ.typeId() : 0;
			o._accept(this);
		} else {
			writeByte(0);
		}

	}

	private void writeFieldStart(final short id, final byte tag) throws IOException {
		writeInt16(id, false);
		writeTypeTag(tag);
	}

	private void writeEnum(final Enum<?> e, final boolean tag) throws IOException {
		if (tag)
			writeTypeTag(TAG_STRING);
		writeString(String.valueOf(e), tag);
	}

	private void writeBoolean(final boolean b, final boolean tag) throws IOException {
		if (tag)
			writeTypeTag(TAG_BOOL);
		writeByte(b ? 1 : 0);
	}

	private void writeInt8(final int b, final boolean tag) throws IOException {
		if (tag)
			writeTypeTag(TAG_INT8);
		writeByte(b);
	}

	private void writeInt16(final short s, final boolean tag) throws IOException {
		if (tag)
			writeTypeTag(TAG_INT16);
		writeRawInt16(s);
	}

	private void writeInt32(final int i, final boolean tag) throws IOException {
		if (tag)
			writeTypeTag(TAG_INT32);
		writeSignedVarint32(i);
	}

	private void writeInt64(final long l, final boolean tag) throws IOException {
		if (tag)
			writeTypeTag(TAG_INT64);
		writeSignedVarint64(l);
	}

	private void writeFloat32(final float f, final boolean tag) throws IOException {
		if (tag)
			writeTypeTag(TAG_FLOAT32);
		writeRawInt32(Float.floatToIntBits(f));
	}

	private void writeFloat64(final double d, final boolean tag) throws IOException {
		if (tag)
			writeTypeTag(TAG_FLOAT64);
		writeRawInt64(Double.doubleToLongBits(d));
	}

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

	private void writeList(final List<Object> list, final ListType typ, final boolean tag)
			throws IOException {

		if (tag)
			writeTypeTag(TAG_LIST);

		writeElements(false, list, typ.elementType());

	}

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

	private void writeMap(final HashMap<Object, Object> map, final MapType typ, final boolean tag)
			throws IOException {

		if (tag)
			writeTypeTag(TAG_MAP);

		if (map != null && !map.isEmpty()) {

			writeSize(map.size());

			writeElements(true, map.keySet(), typ.keyType());
			writeElements(true, map.values(), typ.valueType());

		} else {
			writeSize(0);
		}

	}

	private void writeArray(final Object arrayObj, final ArrayType typ, final boolean tag)
			throws IOException {

		if (tag)
			writeTypeTag(TAG_LIST);

		if (arrayObj != null) {

			final Type elementType = typ.elementType();

			switch (elementType.typeEnum()) {
			case ENUM:
				writeEnumArray((Enum<?>[]) arrayObj, false);
				break;
			case BOOL:
				writeBooleanArray((boolean[]) arrayObj, false);
				break;
			case INT8:
				writeInt8Array((byte[]) arrayObj, false);
				break;
			case INT16:
				writeInt16Array((short[]) arrayObj, false);
				break;
			case INT32:
				writeInt32Array((int[]) arrayObj, false);
				break;
			case INT64:
				writeInt64Array((long[]) arrayObj, false);
				break;
			case FLOAT32:
				writeFloat32Array((float[]) arrayObj, false);
				break;
			case FLOAT64:
				writeFloat64Array((double[]) arrayObj, false);
				break;
			default:
				writeObjectArray((Object[]) arrayObj, elementType, false);
				break;
			}

		} else {
			writeSize(0);
		}

	}

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

	private void flush() throws IOException {
		if (m_buffer.nonEmpty()) {
			m_streamOut.write(m_buffer.data(), 0, m_buffer.size());
			m_buffer.clear();
		}
	}

	private void checkFlush() throws IOException {
		if (m_buffer.size() >= FLUSH_SIZE)
			flush();
	}

	private void writeByte(int b) throws IOException {
		m_buffer.write(b);
		checkFlush();
	}

	private void writeBytes(final byte[] data, final int offset, final int sz) throws IOException {
		m_buffer.write(data, offset, sz);
		checkFlush();
	}

	private void writeBytes(final byte[] data, final int sz) throws IOException {
		writeBytes(data, 0, sz);
	}

	private void writeBytes(final byte[] data) throws IOException {
		writeBytes(data, 0, data.length);
	}

	private void writeRawInt16(short s) throws IOException {
		writeByte(s >>> 8);
		writeByte(s >>> 0);
	}

	private void writeRawInt32(int v) throws IOException {
		writeByte(v >>> 24);
		writeByte(v >>> 16);
		writeByte(v >>> 8);
		writeByte(v >>> 0);
	}

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

	private void writeSignedVarint32(final int i) throws IOException {
		Varint.writeSignedVarInt(i, m_buffer);
		checkFlush();
	}

	private void writeSignedVarint64(final long l) throws IOException {
		Varint.writeSignedVarLong(l, m_buffer);
		checkFlush();
	}

	private void writeUnsignedVarint32(final int i) throws IOException {
		Varint.writeUnsignedVarInt(i, m_buffer);
		checkFlush();
	}

	private void writeSize(final int i) throws IOException {
		writeUnsignedVarint32(i);
	}

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
			writeMap((HashMap<Object, Object>) o, (MapType) typ, tag);
			break;
		case CUSTOM:
		case UNKNOWN:
			writeMGenObject((MGenBase) o, tag, (CustomType) typ);
			break;
		default:
			throw new SerializationException("Unknown type tag for writeObject");
		}

	}

	private boolean shouldOmitIds(final MGenBase o) {
		return m_compact && o._typeId() == m_expectType;
	}

	private void writeTypeTag(byte tag) throws IOException {
		writeByte(tag);
	}

}
