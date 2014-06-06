package se.culvertsoft.mgen.javapack.serialization;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import se.culvertsoft.mgen.api.model.ArrayType;
import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.api.model.ListType;
import se.culvertsoft.mgen.api.model.MapType;
import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.javapack.classes.ClassRegistry;
import se.culvertsoft.mgen.javapack.classes.MGenBase;

public class XmlWriter extends DynamicWriter {

	public XmlWriter(
			final OutputStream outputStream,
			final ClassRegistry classRegistry,
			final boolean useTypeHashes) {
		super(outputStream, classRegistry, useTypeHashes);
	}

	public XmlWriter(
			final OutputStream outputStream,
			final ClassRegistry classRegistry) {
		this(outputStream, classRegistry, true);
	}

	protected String fieldIdOf(final Field field) {
		return field.name();
	}

	@Override
	public void writeBooleanField(boolean b, Field field, final boolean isSet)
			throws IOException {
		writeBoolean(fieldIdOf(field), b);
	}

	@Override
	public void writeInt8Field(byte b, Field field, final boolean isSet)
			throws IOException {
		writeInt8(fieldIdOf(field), b);
	}

	@Override
	public void writeInt16Field(short s, Field field, final boolean isSet)
			throws IOException {
		writeInt16(fieldIdOf(field), s);
	}

	@Override
	public void writeInt32Field(int i, Field field, final boolean isSet)
			throws IOException {
		writeInt32(fieldIdOf(field), i);
	}

	@Override
	public void writeInt64Field(long l, Field field, final boolean isSet)
			throws IOException {
		writeInt64(fieldIdOf(field), l);
	}

	@Override
	public void writeFloat32Field(float f, Field field, final boolean isSet)
			throws IOException {
		writeFloat32(fieldIdOf(field), f);
	}

	@Override
	public void writeFloat64Field(double d, Field field, final boolean isSet)
			throws IOException {
		writeFloat64(fieldIdOf(field), d);
	}

	@Override
	public void writeStringField(String s, Field field, final boolean isSet)
			throws IOException {
		writeString(fieldIdOf(field), s);
	}

	@Override
	public void writeListField(
			ArrayList<Object> list,
			Field field,
			final boolean isSet) throws IOException {
		writeList(fieldIdOf(field), list, (ListType) field.typ());
	}

	@Override
	public void writeMapField(
			HashMap<Object, Object> map,
			Field field,
			final boolean isSet) throws IOException {
		writeMap(fieldIdOf(field), map, (MapType) field.typ());
	}

	@Override
	public void writeArrayField(Object array, Field field, final boolean isSet)
			throws IOException {
		writeArray(fieldIdOf(field), array, (ArrayType) field.typ());
	}

	@Override
	public void writeMGenObjectField(
			MGenBase o,
			Field field,
			final boolean isSet) throws IOException {
		writeMgenObject(fieldIdOf(field), o);
	}

	@Override
	public void beginWrite(final MGenBase o, final int nFieldsSet)
			throws IOException {
		for (final String typeId : typeIdsOf(o)) {
			writeString("__t", typeId);
		}
	}

	@Override
	public void finishWrite() throws IOException {
	}

	/*******************************************************************
	 * 
	 * 
	 * - - - - - - - - - - INTERNAL METHODS
	 * 
	 ******************************************************************/

	private void writeArray(String arrayId, Object array, ArrayType arrayType)
			throws IOException {
		switch (arrayType.elementType().typeEnum()) {
		case BOOL:
			writeBooleanArray(arrayId, (boolean[]) array, arrayType);
			break;
		case INT8:
			writeInt8Array(arrayId, (byte[]) array, arrayType);
			break;
		case INT16:
			writeInt16Array(arrayId, (short[]) array, arrayType);
			break;
		case INT32:
			writeInt32Array(arrayId, (int[]) array, arrayType);
			break;
		case INT64:
			writeInt64Array(arrayId, (long[]) array, arrayType);
			break;
		case FLOAT32:
			writeFloat32Array(arrayId, (float[]) array, arrayType);
			break;
		case FLOAT64:
			writeFloat64Array(arrayId, (double[]) array, arrayType);
			break;
		default:
			writeObjectArray(arrayId, (Object[]) array, arrayType);
			break;
		}
	}

	private void writeBooleanArray(
			final String id,
			final boolean[] array,
			ArrayType arrayType) throws IOException {
		tagBegin(id);
		for (int i = 0; i < array.length; i++)
			writeBoolean("i" + i, array[i]);
		tagEnd(id);
	}

	private void writeInt8Array(
			final String id,
			final byte[] array,
			ArrayType arrayType) throws IOException {
		tagBegin(id);
		for (int i = 0; i < array.length; i++)
			writeInt8("i" + i, array[i]);
		tagEnd(id);
	}

	private void writeInt16Array(
			final String id,
			final short[] array,
			ArrayType arrayType) throws IOException {
		tagBegin(id);
		for (int i = 0; i < array.length; i++)
			writeInt16("i" + i, array[i]);
		tagEnd(id);
	}

	private void writeInt32Array(
			final String id,
			final int[] array,
			ArrayType arrayType) throws IOException {
		tagBegin(id);
		for (int i = 0; i < array.length; i++)
			writeInt32("i" + i, array[i]);
		tagEnd(id);
	}

	private void writeInt64Array(
			final String id,
			final long[] array,
			ArrayType arrayType) throws IOException {
		tagBegin(id);
		for (int i = 0; i < array.length; i++)
			writeInt64("i" + i, array[i]);
		tagEnd(id);
	}

	private void writeFloat32Array(
			final String id,
			final float[] array,
			ArrayType arrayType) throws IOException {
		tagBegin(id);
		for (int i = 0; i < array.length; i++)
			writeFloat32("i" + i, array[i]);
		tagEnd(id);
	}

	private void writeFloat64Array(
			final String id,
			final double[] array,
			ArrayType arrayType) throws IOException {
		tagBegin(id);
		for (int i = 0; i < array.length; i++)
			writeFloat64("i" + i, array[i]);
		tagEnd(id);
	}

	private void writeObjectArray(
			final String id,
			final Object[] array,
			ArrayType arrayType) throws IOException {
		tagBegin(id);
		for (int i = 0; i < array.length; i++)
			writeObject("i" + i, array[i], arrayType.elementType());
		tagEnd(id);
	}

	private void writeList(
			String listId,
			ArrayList<Object> list,
			ListType listType) throws IOException {
		tagBegin(listId);
		for (int i = 0; i < list.size(); i++) {
			final Object o = list.get(i);
			final String id = "i" + i;
			writeObject(id, o, listType.elementType());
		}
		tagEnd(listId);
	}

	private void writeMap(
			String mapId,
			HashMap<Object, Object> map,
			MapType mapType) throws IOException {
		tagBegin(mapId);
		for (final Object key : map.keySet()) {
			final String keyString = key != null ? key.toString() : "null";
			writeObject(keyString, map.get(key), mapType.valueType());
		}
		tagEnd(mapId);
	}

	@SuppressWarnings("unchecked")
	private void writeObject(String id, Object o, Type type) throws IOException {
		if (o != null) {
			switch (type.typeEnum()) {
			case BOOL:
				writeBoolean(id, o != null ? (Boolean) o : false);
				break;
			case INT8:
				writeInt8(id, o != null ? (Byte) o : 0);
				break;
			case INT16:
				writeInt16(id, o != null ? (Short) o : 0);
				break;
			case INT32:
				writeInt32(id, o != null ? (Integer) o : 0);
				break;
			case INT64:
				writeInt64(id, o != null ? (Long) o : 0L);
				break;
			case FLOAT32:
				writeFloat32(id, o != null ? (Float) o : 0.0f);
				break;
			case FLOAT64:
				writeFloat64(id, o != null ? (Double) o : 0.0);
				break;
			case STRING:
				writeString(id, (String) o);
				break;
			case LIST:
				writeList(id, (ArrayList<Object>) o, (ListType) type);
				break;
			case ARRAY:
				writeArray(id, o, (ArrayType) type);
				break;
			case MAP:
				writeMap(id, (HashMap<Object, Object>) o, (MapType) type);
				break;
			case CUSTOM:
			case MGEN_BASE:
			case UNKNOWN:
				writeMgenObject(id, (MGenBase) o);
				break;
			}
		} else {
			writeString(id, "null");
		}
	}

	private void writeBoolean(String id, boolean b) throws IOException {
		tagBegin(id);
		write(b);
		tagEnd(id);
	}

	private void writeInt8(String id, byte b) throws IOException {
		tagBegin(id);
		write(b);
		tagEnd(id);
	}

	private void writeInt16(String id, short b) throws IOException {
		tagBegin(id);
		write(b);
		tagEnd(id);
	}

	private void writeInt32(String id, int b) throws IOException {
		tagBegin(id);
		write(b);
		tagEnd(id);
	}

	private void writeInt64(String id, long b) throws IOException {
		tagBegin(id);
		write(b);
		tagEnd(id);
	}

	private void writeFloat32(String id, float b) throws IOException {
		tagBegin(id);
		write(b);
		tagEnd(id);
	}

	private void writeFloat64(String id, double b) throws IOException {
		tagBegin(id);
		write(b);
		tagEnd(id);
	}

	private void writeString(String id, String s) throws IOException {
		tagBegin(id);
		write(s);
		tagEnd(id);
	}

	public void writeMgenObject(String id, MGenBase mgenBase)
			throws IOException {
		tagBegin(id);
		if (mgenBase != null) {
			mgenBase._accept(this);
		} else {
			write("null");
		}
		tagEnd(id);
	}

	protected void tagBegin(final String text) throws IOException {
		write("<" + text + ">");
	}

	protected void tagEnd(final String text) throws IOException {
		write("</" + text + ">");
	}

	@Override
	public void writeMGenObject(final MGenBase o) throws IOException {
		if (o == null) {
			write("null");
		} else {
			validateThrow(o);
			o._accept(this);
		}
	}

}
