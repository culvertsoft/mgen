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
import static se.culvertsoft.mgen.javapack.serialization.BuiltInSerializerUtils.ensureNoMissingReqFields;
import static se.culvertsoft.mgen.javapack.serialization.BuiltInSerializerUtils.throwUnexpectTag;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.culvertsoft.mgen.api.model.ArrayType;
import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.api.model.ListOrArrayType;
import se.culvertsoft.mgen.api.model.ListType;
import se.culvertsoft.mgen.api.model.MapType;
import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.api.model.UnknownCustomType;
import se.culvertsoft.mgen.javapack.classes.ClassRegistry;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.exceptions.MissingRequiredFieldsException;
import se.culvertsoft.mgen.javapack.exceptions.StreamCorruptedException;
import se.culvertsoft.mgen.javapack.exceptions.UnexpectedTypeException;
import se.culvertsoft.mgen.javapack.util.Varint;

public class BinaryReader extends BuiltInReader {

	public BinaryReader(
			final InputStream stream,
			final ClassRegistry classRegistry) {
		super(stream instanceof DataInputStream ? (DataInputStream) stream
				: new DataInputStream(stream), classRegistry);
	}

	@Override
	public final MGenBase readObject() throws IOException {
		return readMGenObject(true, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends MGenBase> T readObject(final Class<T> typ)
			throws IOException {

		final MGenBase out = readMGenObject(true, getRegEntry(typ).typ());

		if (out != null && !typ.isAssignableFrom(out.getClass())) {
			throw new UnexpectedTypeException("Unexpected type. Expected "
					+ typ.getName() + " but got " + out.getClass().getName());
		}

		return (T) out;
	}

	@Override
	public boolean readBooleanField(final Field field, final Object context)
			throws IOException {
		ensureTypeTag(field, TAG_BOOL, readTypeTag());
		return readBoolean(false);
	}

	@Override
	public byte readInt8Field(final Field field, final Object context)
			throws IOException {
		ensureTypeTag(field, TAG_INT8, readTypeTag());
		return readInt8(false);
	}

	@Override
	public short readInt16Field(final Field field, final Object context)
			throws IOException {
		ensureTypeTag(field, TAG_INT16, readTypeTag());
		return readInt16(false);
	}

	@Override
	public int readInt32Field(final Field field, final Object context)
			throws IOException {
		ensureTypeTag(field, TAG_INT32, readTypeTag());
		return readInt32(false);
	}

	@Override
	public long readInt64Field(final Field field, final Object context)
			throws IOException {
		ensureTypeTag(field, TAG_INT64, readTypeTag());
		return readInt64(false);
	}

	@Override
	public float readFloat32Field(final Field field, final Object context)
			throws IOException {
		ensureTypeTag(field, TAG_FLOAT32, readTypeTag());
		return readFloat32(false);
	}

	@Override
	public double readFloat64Field(final Field field, final Object context)
			throws IOException {
		ensureTypeTag(field, TAG_FLOAT64, readTypeTag());
		return readFloat64(false);
	}

	@Override
	public String readStringField(final Field field, final Object context)
			throws IOException {
		ensureTypeTag(field, TAG_STRING, readTypeTag());
		return readString(false);
	}

	@Override
	public Object readArrayField(final Field field, final Object context)
			throws IOException {
		ensureTypeTag(field, TAG_ARRAY, readTypeTag());
		return readArray(false, (ArrayType) field.typ());
	}

	@Override
	public ArrayList<Object> readListField(
			final Field field,
			final Object context) throws IOException {
		ensureTypeTag(field, TAG_LIST, readTypeTag());
		return readList(false, (ListType) field.typ());
	}

	@Override
	public HashMap<Object, Object> readMapField(
			final Field field,
			final Object context) throws IOException {
		ensureTypeTag(field, TAG_MAP, readTypeTag());
		return readMap(false, (MapType) field.typ());
	}

	@Override
	public MGenBase readMgenObjectField(final Field f, final Object context)
			throws IOException {
		ensureTypeTag(f, TAG_CUSTOM, readTypeTag());
		return readMGenObject(false, (UnknownCustomType) f.typ());
	}

	@Override
	public void handleUnknownField(final Field field, final Object context)
			throws IOException {
		skip(readTypeTag());
	}

	/*******************************************************************
	 * 
	 * 
	 * - - - - - - - - - - INTERNAL METHODS
	 * 
	 ******************************************************************/

	private Object skip(final byte typeTag) throws IOException {
		switch (typeTag) {
		case TAG_BOOL:
			return readBoolean(false);
		case TAG_INT8:
			return readInt8(false);
		case TAG_INT16:
			return readInt16(false);
		case TAG_INT32:
			return readInt32(false);
		case TAG_INT64:
			return readInt64(false);
		case TAG_FLOAT32:
			return readFloat32(false);
		case TAG_FLOAT64:
			return readFloat64(false);
		case TAG_STRING:
			return readString(false);
		case TAG_LIST: // List and array have the same tags
			return readList(false, null);
		case TAG_MAP:
			return readMap(false, null);
			// case TAG_MGENBASE:
			// case TAG_UNKNOWN:
		case TAG_CUSTOM:
			return readMGenObject(false, null);
		default:
			throw new StreamCorruptedException("Unknown typeTag: " + typeTag);
		}
	}

	private String readString(final boolean b) throws IOException {

		final int nBytes = readSize();

		if (nBytes > 0) {
			final byte[] bytes = new byte[nBytes];
			m_stream.readFully(bytes);
			return new String(bytes, charset);
		} else {
			return "";
		}
	}

	private void readFields(final MGenBase object, final int nFields)
			throws IOException {

		for (int i = 0; i < nFields; i++)
			object._readField(readFieldId(), null, this);

		ensureNoMissingReqFields(object);
	}

	private void skipFields(final int nFields) throws IOException {
		for (int i = 0; i < nFields; i++) {
			readFieldId();
			skip(readTypeTag());
		}
	}

	private MGenBase readMGenObject(
			final boolean readTypeTag,
			final UnknownCustomType expectType) throws IOException {

		if (readTypeTag)
			ensureTypeTag(null, TAG_CUSTOM, readTypeTag());

		final int nIdsOrFields = readSize();

		if (nIdsOrFields == 0)
			return null;

		final short[] ids;
		final int nFields;

		if ((nIdsOrFields & 0x01) != 0) {
			ids = readTypeIds(nIdsOrFields >> 1);
			nFields = readSize();
		} else { // type ids omitted
			ids = null;
			nFields = nIdsOrFields >> 1;
		}

		if (ids == null && expectType == null) {
			throw new MissingRequiredFieldsException("Missing type information");
		}

		final MGenBase object = instantiate(ids, expectType);

		if (object != null) {
			readFields(object, nFields);
			ensureNoMissingReqFields(object);
			return object;
		} else {
			skipFields(nFields);
			return null;
		}

	}

	private short[] readTypeIds(final int nTypeIds) throws IOException {
		if (nTypeIds > 0) {
			final short[] typeIds = new short[nTypeIds];
			for (int i = 0; i < typeIds.length; i++)
				typeIds[i] = readMgenTypeId();
			return typeIds;

		} else {
			return NO_IDS;
		}
	}

	private byte readTypeTag() throws IOException {
		return m_stream.readByte();
	}

	private boolean readBoolean(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_BOOL, readTypeTag());
		return m_stream.readByte() != 0;
	}

	private byte readInt8(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_INT8, readTypeTag());
		return m_stream.readByte();
	}

	private short readInt16(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_INT16, readTypeTag());
		return m_stream.readShort();
	}

	private int readInt32(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_INT32, readTypeTag());
		return Varint.readSignedVarInt(m_stream);
	}

	private long readInt64(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_INT64, readTypeTag());
		return Varint.readSignedVarLong(m_stream);
	}

	private float readFloat32(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_FLOAT32, readTypeTag());
		return m_stream.readFloat();
	}

	private double readFloat64(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_FLOAT64, readTypeTag());
		return m_stream.readDouble();
	}

	private int readSize() throws IOException {
		final int out = Varint.readUnsignedVarInt(m_stream);
		if (out < 0)
			throw new StreamCorruptedException("readSize() < 0");
		return out;
	}

	private short readMgenTypeId() throws IOException {
		return readInt16(false);
	}

	private short readFieldId() throws IOException {
		return readInt16(false);
	}

	private void ensureTypeTag(
			final Field field,
			final byte expTag,
			final byte readTag) throws IOException {
		if (expTag != readTag) {
			throwUnexpectTag("(for field " + field + ")", expTag, readTag);
		}
	}

	private Object readArray2(
			final int n,
			final byte elemTypeTag,
			final ArrayType arrayType) throws IOException {

		switch (elemTypeTag) {
		case TAG_BOOL:
			return readBooleanArray(n, false);
		case TAG_INT8:
			return readInt8Array(n, false);
		case TAG_INT16:
			return readInt16Array(n, false);
		case TAG_INT32:
			return readInt32Array(n, false);
		case TAG_INT64:
			return readInt64Array(n, false);
		case TAG_FLOAT32:
			return readFloat32Array(n, false);
		case TAG_FLOAT64:
			return readFloat64Array(n, false);
		case TAG_STRING:
		case TAG_LIST:
		case TAG_MAP:
		case TAG_CUSTOM:
			return readObjectArray(n, elemTypeTag, arrayType);
		default:
			throw new StreamCorruptedException("Unknown elemTypeTag: "
					+ elemTypeTag);
		}
	}

	private Object readBooleanArray(final int n, final boolean b)
			throws IOException {
		final boolean[] array = new boolean[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readBoolean(false);
		return array;
	}

	private Object readInt8Array(final int n, final boolean b)
			throws IOException {
		final byte[] array = new byte[n];
		m_stream.readFully(array);
		return array;
	}

	private Object readInt16Array(final int n, final boolean b)
			throws IOException {
		final short[] array = new short[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readInt16(false);
		return array;
	}

	private Object readInt32Array(final int n, final boolean b)
			throws IOException {
		final int[] array = new int[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readInt32(false);
		return array;
	}

	private Object readInt64Array(final int n, final boolean b)
			throws IOException {
		final long[] array = new long[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readInt64(false);
		return array;
	}

	private Object readFloat32Array(final int n, final boolean b)
			throws IOException {
		final float[] array = new float[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readFloat32(false);
		return array;
	}

	private Object readFloat64Array(final int n, final boolean b)
			throws IOException {
		final double[] array = new double[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readFloat64(false);
		return array;
	}

	private Object readObjectArray(
			final int n,
			final byte elemTypeTag,
			final ArrayType arrayType) throws IOException {
		final Object[] array = arrayType != null ? (Object[]) arrayType
				.newInstance(n) : new Object[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readObject(elemTypeTag, arrayType.elementType());
		return array;
	}

	private Object readObject(final byte typeTag, final Type expectType)
			throws IOException {
		switch (typeTag) {
		case TAG_BOOL:
			return readBoolean(false);
		case TAG_INT8:
			return readInt8(false);
		case TAG_INT16:
			return readInt16(false);
		case TAG_INT32:
			return readInt32(false);
		case TAG_INT64:
			return readInt64(false);
		case TAG_FLOAT32:
			return readFloat32(false);
		case TAG_FLOAT64:
			return readFloat64(false);
		case TAG_STRING:
			return readString(false);
		case TAG_LIST: // array and list have the same write tag
			return readList(false, (ListOrArrayType) expectType);
		case TAG_MAP:
			return readMap(false, (MapType) expectType);
		case TAG_CUSTOM:
			return readMGenObject(false, (UnknownCustomType) expectType);
		default:
			throw new StreamCorruptedException("Unknown type tag: " + typeTag);
		}
	}

	private Object readArray(final boolean readTag, final ArrayType expectType)
			throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_ARRAY, readTypeTag());

		final int nElements = readSize();

		if (nElements > 0) {

			final byte rcvdTag = readTypeTag();
			if (expectType != null) {
				final byte expTag = expectType.elementType().binaryTypeTag();
				ensureTypeTag(null, expTag, rcvdTag);
			}
			return readArray2(nElements, rcvdTag, expectType);

		} else {
			return expectType.newInstance(0);
		}
	}

	private ArrayList<Object> readList(
			final boolean readTag,
			final ListOrArrayType listOrArrayType) throws IOException {

		if (readTag)
			ensureTypeTag(null, TAG_LIST, readTypeTag());

		return readElements(listOrArrayType != null ? listOrArrayType
				.elementType() : null);

	}

	private HashMap<Object, Object> readMap(
			final boolean readTag,
			final MapType mapType) throws IOException {

		if (readTag)
			ensureTypeTag(null, TAG_MAP, readTypeTag());

		final int nElements = readSize();
		final HashMap<Object, Object> out = new HashMap<Object, Object>(
				nElements);

		if (nElements > 0) {

			final List<Object> keys = readElements(mapType != null ? mapType
					.keyType() : null);
			final List<Object> values = readElements(mapType != null ? mapType
					.valueType() : null);

			if (keys.size() != values.size() || keys.size() != nElements)
				throw new StreamCorruptedException("nKeys != nValues in map");

			for (int i = 0; i < keys.size(); i++)
				out.put(keys.get(i), values.get(i));

		}

		return out;
	}

	private ArrayList<Object> readElements(final Type constraint)
			throws IOException {

		final int nElements = readSize();

		final byte readElemTag = readTypeTag();

		if (constraint != null && constraint.binaryTypeTag() != readElemTag)
			throwUnexpectTag("", constraint.binaryTypeTag(), readElemTag);

		final ArrayList<Object> out = new ArrayList<Object>(nElements);

		for (int i = 0; i < nElements; i++)
			out.add(readObject(readTypeTag(), constraint));

		return out;
	}

}
