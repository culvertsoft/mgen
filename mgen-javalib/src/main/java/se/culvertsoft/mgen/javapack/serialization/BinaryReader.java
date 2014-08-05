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
import static se.culvertsoft.mgen.javapack.serialization.BuiltInSerializerUtils.ensureNoMissingReqFields;
import static se.culvertsoft.mgen.javapack.serialization.BuiltInSerializerUtils.throwUnexpectTag;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import se.culvertsoft.mgen.api.model.ArrayType;
import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.api.model.ListOrArrayType;
import se.culvertsoft.mgen.api.model.ListType;
import se.culvertsoft.mgen.api.model.MapType;
import se.culvertsoft.mgen.api.model.RuntimeClassType;
import se.culvertsoft.mgen.api.model.RuntimeEnumType;
import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.api.model.TypeEnum;
import se.culvertsoft.mgen.javapack.classes.ClassRegistryBase;
import se.culvertsoft.mgen.javapack.classes.ClassRegistryEntry;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.exceptions.StreamCorruptedException;
import se.culvertsoft.mgen.javapack.exceptions.UnexpectedTypeException;
import se.culvertsoft.mgen.javapack.exceptions.UnknownTypeException;
import se.culvertsoft.mgen.javapack.util.StreamUtil;
import se.culvertsoft.mgen.javapack.util.Varint;

public class BinaryReader extends BuiltInReader {

	private final InputStream m_stream;
	private final byte m_readBuffer64[] = new byte[8];

	public BinaryReader(
			final InputStream stream,
			final ClassRegistryBase classRegistry) {
		super(classRegistry);
		m_stream = stream;
	}

	@Override
	public final MGenBase readObject() throws IOException {
		return readMGenObject(true, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends MGenBase> T readObject(final Class<T> typ)
			throws IOException {

		final ClassRegistryEntry entry = m_clsReg.getByClass(typ);

		if (entry == null)
			throw new UnknownTypeException("Could not read object of type "
					+ typ + ", since it is know known by the class registry");

		return (T) readMGenObject(true, entry.typ());
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
		ensureTypeTag(field, TAG_LIST, readTypeTag());
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
		ensureTypeTag(f, TAG_CLASS, readTypeTag());
		return readMGenObject(false, (RuntimeClassType) f.typ());
	}

	@Override
	public Enum<?> readEnumField(final Field f, final Object context)
			throws IOException {
		final byte tag = readTypeTag();
		ensureTypeTag(f, TAG_STRING, tag);
		return readEnum(false, (RuntimeEnumType) f.typ());
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

	private static final short[] NO_IDS = new short[0];

	private void skip(final byte typeTag) throws IOException {
		switch (typeTag) {
		case TAG_BOOL:
			readBoolean(false);
			break;
		case TAG_INT8:
			readInt8(false);
			break;
		case TAG_INT16:
			readInt16(false);
			break;
		case TAG_INT32:
			readInt32(false);
			break;
		case TAG_INT64:
			readInt64(false);
			break;
		case TAG_FLOAT32:
			readFloat32(false);
			break;
		case TAG_FLOAT64:
			readFloat64(false);
			break;
		case TAG_STRING:
			skipString(false);
			break;
		case TAG_LIST: // List and array have the same tags
			skipList(false);
			break;
		case TAG_MAP:
			skipMap(false);
			break;
		case TAG_CLASS:
			skipCustom();
			break;
		default:
			throw new StreamCorruptedException(
					"Cannot skip item of unknown typeTag: " + typeTag);
		}
	}

	private void skipMap(final boolean doReadTag) throws IOException {
		if (doReadTag)
			ensureTypeTag(null, TAG_MAP, readTypeTag());
		final int sz = readSize();
		if (sz > 0) {

			final byte keyTag = readTypeTag();
			final byte valueTag = readTypeTag();

			for (int i = 0; i < sz; i++) {
				skip(keyTag);
				skip(valueTag);
			}

		}
	}

	private void skipList(final boolean doReadTag) throws IOException {
		if (doReadTag)
			ensureTypeTag(null, TAG_LIST, readTypeTag());
		final int sz = readSize();
		if (sz > 0) {
			final byte tag = readTypeTag();
			for (int i = 0; i < sz; i++)
				skip(tag);
		}
	}

	private void skipString(final boolean doReadTag) throws IOException {
		if (doReadTag)
			ensureTypeTag(null, TAG_STRING, readTypeTag());
		final int nBytes = readSize();
		for (int i = 0; i < nBytes; i++)
			m_stream.read();
	}

	private void skipCustom() throws IOException {

		final int nIdsOrFields = readSize();

		if (nIdsOrFields == 0)
			return;

		final int nFields;

		if ((nIdsOrFields & 0x01) != 0) {
			skipTypeIds(nIdsOrFields >> 2);
			nFields = readSize();
		} else { // type ids omitted
			nFields = nIdsOrFields >> 2;
		}

		skipFields(nFields);
	}

	private void skipTypeIds(int n) throws IOException {
		for (int i = 0; i < n; i++)
			readMgenTypeId();
	}

	private String readString(final boolean readTag) throws IOException {

		if (readTag)
			ensureTypeTag(null, TAG_STRING, readTypeTag());

		final int nBytes = readSize();
		if (nBytes > 0) {
			return m_stringDecoder.decode(m_stream, nBytes);
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
			final RuntimeClassType constraint) throws IOException {

		if (readTypeTag)
			ensureTypeTag(null, TAG_CLASS, readTypeTag());

		final int nIdsOrFields = readSize();

		if (nIdsOrFields == 0)
			return null;

		final short[] ids;
		final int nFields;

		if ((nIdsOrFields & 0x01) != 0) {
			ids = readTypeIds(nIdsOrFields >> 2);
			nFields = readSize();
		} else { // type ids omitted
			ids = null;
			nFields = nIdsOrFields >> 2;
		}

		final MGenBase object = instantiate(ids, constraint);

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
		return (byte) m_stream.read();
	}

	private boolean readBoolean(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_BOOL, readTypeTag());
		return m_stream.read() != 0;
	}

	private byte readInt8(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_INT8, readTypeTag());
		return (byte) m_stream.read();
	}

	private short readInt16(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_INT16, readTypeTag());
		int ch1 = m_stream.read();
		int ch2 = m_stream.read();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return (short) ((ch1 << 8) + (ch2 << 0));
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

		return Float.intBitsToFloat(read32());
	}

	private int read32() throws IOException {
		int ch1 = m_stream.read();
		int ch2 = m_stream.read();
		int ch3 = m_stream.read();
		int ch4 = m_stream.read();
		if ((ch1 | ch2 | ch3 | ch4) < 0)
			throw new EOFException();
		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}

	private double readFloat64(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_FLOAT64, readTypeTag());
		return Double.longBitsToDouble(read64());
	}

	private long read64() throws IOException {
		StreamUtil.readFully(m_stream, 8, m_readBuffer64);
		return (((long) m_readBuffer64[0] << 56)
				+ ((long) (m_readBuffer64[1] & 255) << 48)
				+ ((long) (m_readBuffer64[2] & 255) << 40)
				+ ((long) (m_readBuffer64[3] & 255) << 32)
				+ ((long) (m_readBuffer64[4] & 255) << 24)
				+ ((m_readBuffer64[5] & 255) << 16)
				+ ((m_readBuffer64[6] & 255) << 8) + ((m_readBuffer64[7] & 255) << 0));
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
			final ArrayType constraint) throws IOException {

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
			if (constraint != null
					&& constraint.elementType().typeEnum() == TypeEnum.ENUM) {
				return readEnumArray(n, constraint, false);
			} else {
				return readStringArray(n, false);
			}
		case TAG_LIST:
		case TAG_MAP:
		case TAG_CLASS:
			return readObjectArray(n, elemTypeTag, constraint);
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
		StreamUtil.readFully(m_stream, n, array);
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

	private Object readEnumArray(
			final int n,
			final ArrayType constraint,
			final boolean b) throws IOException {
		final Enum<?>[] array = constraint != null ? (Enum<?>[]) constraint
				.newInstance(n) : new Enum<?>[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readEnum(
					false,
					constraint != null ? (RuntimeEnumType) constraint
							.elementType() : null);
		return array;
	}

	private String[] readStringArray(final int n, final boolean b)
			throws IOException {
		final String[] array = new String[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readString(false);
		return array;
	}

	private Object readObjectArray(
			final int n,
			final byte elemTypeTag,
			final ArrayType constraint) throws IOException {
		final Object[] array = constraint != null ? (Object[]) constraint
				.newInstance(n) : new Object[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readObject(
					elemTypeTag,
					constraint != null ? constraint.elementType() : null);
		return array;
	}

	private Object readObject(final byte typeTag, final Type constraint)
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
			if (constraint != null && constraint.typeEnum() == TypeEnum.ENUM) {
				return readEnum(false, (RuntimeEnumType) constraint);
			} else {
				return readString(false);
			}
		case TAG_LIST: // array and list have the same write tag
			if (constraint != null && constraint.typeEnum() == TypeEnum.ARRAY)
				return readArray(false, (ArrayType) constraint);
			else
				return readList(false, (ListType) constraint);
		case TAG_MAP:
			return readMap(false, (MapType) constraint);
		case TAG_CLASS:
			return readMGenObject(false, (RuntimeClassType) constraint);
		default:
			throw new StreamCorruptedException("Unknown type tag: " + typeTag);
		}
	}

	private Object readArray(final boolean readTag, final ArrayType constraint)
			throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_LIST, readTypeTag());

		final int nElements = readSize();

		if (nElements > 0) {

			final byte rcvdTag = readTypeTag();
			if (constraint != null) {
				final byte expTag = constraint.elementType().typeTag();
				ensureTypeTag(null, expTag, rcvdTag);
			}
			return readArray2(nElements, rcvdTag, constraint);

		} else {
			return constraint != null ? constraint.newInstance(0)
					: new Object[0];
		}
	}

	private ArrayList<Object> readList(
			final boolean tag,
			final ListOrArrayType constraint) throws IOException {

		if (tag)
			ensureTypeTag(null, TAG_LIST, readTypeTag());

		return readElements(
				false,
				constraint != null ? constraint.elementType() : null);

	}

	private HashMap<Object, Object> readMap(
			final boolean doReadTag,
			final MapType constraint) throws IOException {

		if (doReadTag)
			ensureTypeTag(null, TAG_MAP, readTypeTag());

		final int nElements = readSize();
		final HashMap<Object, Object> out = new HashMap<Object, Object>(
				nElements);

		if (nElements > 0) {

			final byte keyTag = readTypeTag();
			final byte valueTag = readTypeTag();

			final Type keyType = constraint != null ? constraint.keyType()
					: null;
			final Type valueType = constraint != null ? constraint.valueType()
					: null;

			if (constraint != null) {
				ensureTypeTag(null, keyType.typeTag(), keyTag);
				ensureTypeTag(null, valueType.typeTag(), valueTag);
			}

			for (int i = 0; i < nElements; i++) {
				final Object key = readObject(keyTag, keyType);
				final Object value = readObject(valueTag, valueType);
				out.put(key, value);
			}

		}

		return out;
	}

	private ArrayList<Object> readElements(
			final boolean doReadTag,
			final Type constraint) throws IOException {

		if (doReadTag)
			ensureTypeTag(null, TAG_LIST, readTypeTag());

		final int nElements = readSize();

		final ArrayList<Object> out = new ArrayList<Object>(nElements);

		if (nElements > 0) {

			final byte readElemTag = readTypeTag();

			if (constraint != null && constraint.typeTag() != readElemTag)
				throwUnexpectTag("", constraint.typeTag(), readElemTag);

			switch (readElemTag) {
			case TAG_BOOL:
				for (int i = 0; i < nElements; i++)
					out.add(readBoolean(false));
				break;
			case TAG_INT8:
				for (int i = 0; i < nElements; i++)
					out.add(readInt8(false));
				break;
			case TAG_INT16:
				for (int i = 0; i < nElements; i++)
					out.add(readInt16(false));
				break;
			case TAG_INT32:
				for (int i = 0; i < nElements; i++)
					out.add(readInt32(false));
				break;
			case TAG_INT64:
				for (int i = 0; i < nElements; i++)
					out.add(readInt64(false));
				break;
			case TAG_FLOAT32:
				for (int i = 0; i < nElements; i++)
					out.add(readFloat32(false));
				break;
			case TAG_FLOAT64:
				for (int i = 0; i < nElements; i++)
					out.add(readFloat64(false));
				break;
			case TAG_STRING:
				if (constraint != null
						&& constraint.typeEnum() == TypeEnum.ENUM) {
					final RuntimeEnumType t = (RuntimeEnumType) constraint;
					for (int i = 0; i < nElements; i++)
						out.add(readEnum(false, t));
				} else {
					for (int i = 0; i < nElements; i++)
						out.add(readString(false));
				}
				break;
			default:
				for (int i = 0; i < nElements; i++)
					out.add(readObject(readElemTag, constraint));
				break;

			}

		}

		return out;
	}

	protected MGenBase instantiate(
			final short[] ids,
			final RuntimeClassType constraint) {

		if (ids == null && constraint == null)
			return null;

		final ClassRegistryEntry entry = ids != null ? m_clsReg
				.getByTypeIds16Bit(ids) : m_clsReg.getById(constraint.typeId());

		if (constraint != null) {
			if (entry == null) {
				throw new UnexpectedTypeException("Unknown type: "
						+ Arrays.toString(ids));
			} else if (!entry.isInstanceOfTypeId(constraint.typeId())) {
				throw new UnexpectedTypeException("Unexpected type. Expected "
						+ constraint.fullName() + " but got " + entry.clsName());
			}
		}

		return entry != null ? entry.construct() : null;

	}

	private Enum<?> readEnum(
			final boolean readTag,
			final RuntimeEnumType constraint) throws IOException {
		final String writtenName = readString(readTag);
		return constraint != null ? constraint.get(writtenName) : null;
	}

}
