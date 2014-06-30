package se.culvertsoft.mgen.javapack.serialization;

import static se.culvertsoft.mgen.api.model.BinaryTypeTag.TAG_ARRAY;
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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import se.culvertsoft.mgen.api.model.ArrayType;
import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.api.model.ListType;
import se.culvertsoft.mgen.api.model.MapType;
import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.javapack.classes.ClassRegistry;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.exceptions.SerializationException;
import se.culvertsoft.mgen.javapack.util.Varint;

public class BinaryWriter extends BuiltInWriter {

	public BinaryWriter(
			final OutputStream stream,
			final ClassRegistry classRegistry) {
		super(stream instanceof DataOutputStream ? (DataOutputStream) stream
				: new DataOutputStream(stream), classRegistry);
	}

	@Override
	public void writeObject(final MGenBase o) throws IOException {
		writeMGenObject(o, true, null);
	}

	@Override
	public void beginWrite(
			final MGenBase object,
			final int nFieldsSet,
			final int nFieldsTotal) throws IOException {
		final short[] typeIds = object._typeIds16Bit();
		writeSize(typeIds.length);
		for (final short typeId : typeIds)
			writeInt16(typeId, false);
		writeSize(nFieldsSet);
	}

	@Override
	public void finishWrite() {
	}

	@Override
	public void writeBooleanField(final boolean b, final Field field)
			throws IOException {
		writeFieldStart(field.id(), TAG_BOOL);
		writeBoolean(b, false);
	}

	@Override
	public void writeInt8Field(final byte b, final Field field)
			throws IOException {
		writeFieldStart(field.id(), TAG_INT8);
		writeInt8(b, false);
	}

	@Override
	public void writeInt16Field(final short s, final Field field)
			throws IOException {
		writeFieldStart(field.id(), TAG_INT16);
		writeInt16(s, false);
	}

	@Override
	public void writeInt32Field(final int i, final Field field)
			throws IOException {
		writeFieldStart(field.id(), TAG_INT32);
		writeInt32(i, false);
	}

	@Override
	public void writeInt64Field(final long l, final Field field)
			throws IOException {
		writeFieldStart(field.id(), TAG_INT64);
		writeInt64(l, false);
	}

	@Override
	public void writeFloat32Field(final float f, final Field field)
			throws IOException {
		writeFieldStart(field.id(), TAG_FLOAT32);
		writeFloat32(f, false);
	}

	@Override
	public void writeFloat64Field(final double d, final Field field)
			throws IOException {
		writeFieldStart(field.id(), TAG_FLOAT64);
		writeFloat64(d, false);
	}

	@Override
	public void writeStringField(final String s, final Field field)
			throws IOException {
		writeFieldStart(field.id(), TAG_STRING);
		writeString(s, false);
	}

	@Override
	public void writeListField(final ArrayList<Object> list, final Field field)
			throws IOException {
		writeFieldStart(field.id(), TAG_LIST);
		writeList(list, (ListType) field.typ(), false);
	}

	@Override
	public void writeMapField(
			final HashMap<Object, Object> map,
			final Field field) throws IOException {
		writeFieldStart(field.id(), TAG_MAP);
		writeMap(map, (MapType) field.typ(), false);
	}

	@Override
	public void writeArrayField(final Object arrayObj, final Field field)
			throws IOException {
		writeFieldStart(field.id(), TAG_ARRAY);
		writeArray(arrayObj, (ArrayType) field.typ(), false);
	}

	@Override
	public void writeMGenObjectField(final MGenBase o, final Field field)
			throws IOException {
		writeFieldStart(field.id(), TAG_CUSTOM);
		writeMGenObject(o, false, field.typ());
	}

	/*******************************************************************
	 * 
	 * 
	 * - - - - - - - - - - INTERNAL METHODS
	 * 
	 ******************************************************************/

	private void writeMGenObject(
			final MGenBase o,
			final boolean tag,
			final Type typ) throws IOException {

		if (tag)
			writeTypeTag(TAG_CUSTOM);

		if (o != null) {
			o._accept(this);
		} else {
			m_stream.writeByte(0);
		}

	}

	private void writeFieldStart(final short id, final byte tag)
			throws IOException {
		writeInt16(id, false);
		writeTypeTag(tag);
	}

	private void writeBoolean(final boolean b, final boolean tag)
			throws IOException {
		if (tag)
			writeTypeTag(TAG_BOOL);
		m_stream.writeByte(b ? 1 : 0);
	}

	private void writeInt8(final int b, final boolean tag) throws IOException {
		if (tag)
			writeTypeTag(TAG_INT8);
		m_stream.writeByte(b);
	}

	private void writeInt16(final short s, final boolean tag)
			throws IOException {
		if (tag)
			writeTypeTag(TAG_INT16);
		m_stream.writeShort(s);
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

	private void writeFloat32(final float f, final boolean tag)
			throws IOException {
		if (tag)
			writeTypeTag(TAG_FLOAT32);
		m_stream.writeFloat(f);
	}

	private void writeFloat64(final double d, final boolean tag)
			throws IOException {
		if (tag)
			writeTypeTag(TAG_FLOAT64);
		m_stream.writeDouble(d);
	}

	private void writeString(final String s, final boolean tag)
			throws IOException {
		if (tag)
			writeTypeTag(TAG_STRING);
		if (s != null && !s.isEmpty()) {
			final ByteBuffer bb = encodeString(s);
			writeSize(bb.remaining());
			m_stream.write(bb.array(), 0, bb.remaining());
		} else {
			writeSize(0);
		}
	}

	private void writeList(
			final List<Object> list,
			final ListType typ,
			final boolean tag) throws IOException {

		if (tag)
			writeTypeTag(TAG_LIST);

		writeElements(list, typ.elementType());

	}

	private void writeElements(
			final Collection<Object> list,
			final Type elementType) throws IOException {

		if (list != null && !list.isEmpty()) {

			writeSize(list.size());

			writeTypeTag(elementType.binaryTypeTag());

			for (final Object o : list)
				writeObject(o, elementType, false);

		} else {
			writeSize(0);
		}
	}

	private void writeMap(
			final HashMap<Object, Object> map,
			final MapType typ,
			final boolean tag) throws IOException {

		if (tag)
			writeTypeTag(TAG_MAP);

		if (map != null && !map.isEmpty()) {

			writeSize(map.size());

			writeElements(map.keySet(), typ.keyType());
			writeElements(map.values(), typ.valueType());

		} else {
			writeSize(0);
		}

	}

	private void writeArray(
			final Object arrayObj,
			final ArrayType typ,
			final boolean tag) throws IOException {

		if (tag)
			writeTypeTag(TAG_ARRAY);

		if (arrayObj != null) {

			final Type elementType = typ.elementType();

			switch (elementType.typeEnum()) {
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

	private void writeBooleanArray(final boolean[] array, final boolean tag)
			throws IOException {

		if (tag)
			writeTypeTag(TAG_ARRAY);

		if (array != null && array.length != 0) {

			writeSize(array.length);

			writeTypeTag(TAG_BOOL);

			for (final boolean b : array)
				writeBoolean(b, false);

		} else {
			writeSize(0);
		}

	}

	private void writeInt8Array(final byte[] array, final boolean tag)
			throws IOException {

		if (tag)
			writeTypeTag(TAG_ARRAY);

		if (array != null && array.length != 0) {

			writeSize(array.length);

			writeTypeTag(TAG_INT8);
			m_stream.write(array);

		} else {
			writeSize(0);
		}

	}

	private void writeInt16Array(final short[] array, final boolean tag)
			throws IOException {

		if (tag)
			writeTypeTag(TAG_ARRAY);

		if (array != null && array.length != 0) {

			writeSize(array.length);

			writeTypeTag(TAG_INT16);

			for (final short s : array)
				writeInt16(s, false);

		} else {
			writeSize(0);
		}

	}

	private void writeInt32Array(final int[] array, final boolean tag)
			throws IOException {

		if (tag)
			writeTypeTag(TAG_ARRAY);

		if (array != null && array.length != 0) {

			writeSize(array.length);

			writeTypeTag(TAG_INT32);

			for (final int i : array)
				writeInt32(i, false);

		} else {
			writeSize(0);
		}

	}

	private void writeInt64Array(final long[] array, final boolean tag)
			throws IOException {

		if (tag)
			writeTypeTag(TAG_ARRAY);

		if (array != null && array.length != 0) {

			writeSize(array.length);

			writeTypeTag(TAG_INT64);

			for (final long l : array)
				writeInt64(l, false);

		} else {
			writeSize(0);
		}

	}

	private void writeFloat32Array(final float[] array, final boolean tag)
			throws IOException {

		if (tag)
			writeTypeTag(TAG_ARRAY);

		if (array != null && array.length != 0) {

			writeSize(array.length);

			writeTypeTag(TAG_FLOAT32);

			for (final float f : array)
				writeFloat32(f, false);

		} else {
			writeSize(0);
		}

	}

	private void writeFloat64Array(final double[] array, final boolean tag)
			throws IOException {

		if (tag)
			writeTypeTag(TAG_ARRAY);

		if (array != null && array.length != 0) {

			writeSize(array.length);

			writeTypeTag(TAG_FLOAT64);

			for (final double f : array)
				writeFloat64(f, false);

		} else {
			writeSize(0);
		}

	}

	private void writeObjectArray(
			final Object[] array,
			final Type elementType,
			final boolean tag) throws IOException {

		if (tag)
			writeTypeTag(TAG_ARRAY);

		if (array != null && array.length != 0) {

			writeSize(array.length);

			writeTypeTag(elementType.binaryTypeTag());

			for (final Object o : array)
				writeObject(o, elementType, false);

		} else {
			writeSize(0);
		}
	}

	private void writeSignedVarint32(final int i) throws IOException {
		Varint.writeSignedVarInt(i, m_stream);
	}

	private void writeSignedVarint64(final long l) throws IOException {
		Varint.writeSignedVarLong(l, m_stream);
	}

	private void writeUnsignedVarint32(final int i) throws IOException {
		Varint.writeUnsignedVarInt(i, m_stream);
	}

	private void writeSize(final int i) throws IOException {
		writeUnsignedVarint32(i);
	}

	@SuppressWarnings("unchecked")
	private void writeObject(final Object o, final Type typ, boolean tag)
			throws IOException {
		switch (typ.typeEnum()) {
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
		case MGEN_BASE:
		case UNKNOWN:
			writeMGenObject((MGenBase) o, tag, typ);
			break;
		default:
			throw new SerializationException("Unknown type tag for writeObject");
		}

	}

	private void writeTypeTag(byte tag) throws IOException {
		m_stream.writeByte(tag);
	}

}
