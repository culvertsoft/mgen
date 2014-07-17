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
import static se.culvertsoft.mgen.javapack.serialization.BuiltInSerializerUtils.ensureNoMissingReqFields;
import static se.culvertsoft.mgen.javapack.serialization.BuiltInSerializerUtils.throwUnexpectTag;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.culvertsoft.mgen.api.model.ArrayType;
import se.culvertsoft.mgen.api.model.CustomType;
import se.culvertsoft.mgen.api.model.EnumType;
import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.api.model.ListOrArrayType;
import se.culvertsoft.mgen.api.model.ListType;
import se.culvertsoft.mgen.api.model.MapType;
import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.api.model.TypeEnum;
import se.culvertsoft.mgen.javapack.classes.ClassRegistryBase;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.exceptions.MissingRequiredFieldsException;
import se.culvertsoft.mgen.javapack.exceptions.StreamCorruptedException;
import se.culvertsoft.mgen.javapack.exceptions.UnexpectedTypeException;
import se.culvertsoft.mgen.javapack.util.Varint;

public class BinaryReader extends BuiltInReader {

	public BinaryReader(final InputStream stream, final ClassRegistryBase classRegistry) {
		super(stream instanceof DataInputStream ? (DataInputStream) stream : new DataInputStream(
				stream), classRegistry);
	}

	@Override
	public final MGenBase readObject() throws IOException {
		return readMGenObject(true, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends MGenBase> T readObject(final Class<T> typ) throws IOException {

		final MGenBase out = readMGenObject(true, getRegEntry(typ).typ());

		if (out != null && !typ.isAssignableFrom(out.getClass())) {
			throw new UnexpectedTypeException("Unexpected type. Expected " + typ.getName()
					+ " but got " + out.getClass().getName());
		}

		return (T) out;
	}

	@Override
	public boolean readBooleanField(final Field field, final Object context) throws IOException {
		ensureTypeTag(field, TAG_BOOL, readTypeTag());
		return readBoolean(false);
	}

	@Override
	public byte readInt8Field(final Field field, final Object context) throws IOException {
		ensureTypeTag(field, TAG_INT8, readTypeTag());
		return readInt8(false);
	}

	@Override
	public short readInt16Field(final Field field, final Object context) throws IOException {
		ensureTypeTag(field, TAG_INT16, readTypeTag());
		return readInt16(false);
	}

	@Override
	public int readInt32Field(final Field field, final Object context) throws IOException {
		ensureTypeTag(field, TAG_INT32, readTypeTag());
		return readInt32(false);
	}

	@Override
	public long readInt64Field(final Field field, final Object context) throws IOException {
		ensureTypeTag(field, TAG_INT64, readTypeTag());
		return readInt64(false);
	}

	@Override
	public float readFloat32Field(final Field field, final Object context) throws IOException {
		ensureTypeTag(field, TAG_FLOAT32, readTypeTag());
		return readFloat32(false);
	}

	@Override
	public double readFloat64Field(final Field field, final Object context) throws IOException {
		ensureTypeTag(field, TAG_FLOAT64, readTypeTag());
		return readFloat64(false);
	}

	@Override
	public String readStringField(final Field field, final Object context) throws IOException {
		ensureTypeTag(field, TAG_STRING, readTypeTag());
		return readString(false);
	}

	@Override
	public Object readArrayField(final Field field, final Object context) throws IOException {
		ensureTypeTag(field, TAG_LIST, readTypeTag());
		return readArray(false, (ArrayType) field.typ());
	}

	@Override
	public ArrayList<Object> readListField(final Field field, final Object context)
			throws IOException {
		ensureTypeTag(field, TAG_LIST, readTypeTag());
		return readList(false, (ListType) field.typ());
	}

	@Override
	public HashMap<Object, Object> readMapField(final Field field, final Object context)
			throws IOException {
		ensureTypeTag(field, TAG_MAP, readTypeTag());
		return readMap(false, (MapType) field.typ());
	}

	@Override
	public MGenBase readMgenObjectField(final Field f, final Object context) throws IOException {
		ensureTypeTag(f, TAG_CUSTOM, readTypeTag());
		return readMGenObject(false, (CustomType) f.typ());
	}

	@Override
	public Enum<?> readEnumField(final Field f, final Object context) throws IOException {
		final byte tag = readTypeTag();
		ensureTypeTag(f, TAG_STRING, tag);
		return readEnum(false, (EnumType) f.typ());
	}

	@Override
	public void handleUnknownField(final Field field, final Object context) throws IOException {
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

	private String readString(final boolean readTag) throws IOException {

		if (readTag)
			ensureTypeTag(null, TAG_STRING, readTypeTag());

		final int nBytes = readSize();

		if (nBytes > 0) {
			final byte[] bytes = new byte[nBytes];
			m_stream.readFully(bytes);
			return new String(bytes, charset);
		} else {
			return "";
		}
	}

	private void readFields(final MGenBase object, final int nFields) throws IOException {

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

	private MGenBase readMGenObject(final boolean readTypeTag, final CustomType constraint)
			throws IOException {

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

		if (ids == null && constraint == null) {
			throw new MissingRequiredFieldsException("Missing type information");
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

	private void ensureTypeTag(final Field field, final byte expTag, final byte readTag)
			throws IOException {
		if (expTag != readTag) {
			throwUnexpectTag("(for field " + field + ")", expTag, readTag);
		}
	}

	private Object readArray2(final int n, final byte elemTypeTag, final ArrayType constraint)
			throws IOException {

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
			if (constraint != null && constraint.elementType().typeEnum() == TypeEnum.ENUM) {
				return readEnumArray(n, constraint, false);
			} else {
				return readStringArray(n, false);
			}
		case TAG_LIST:
		case TAG_MAP:
		case TAG_CUSTOM:
			return readObjectArray(n, elemTypeTag, constraint);
		default:
			throw new StreamCorruptedException("Unknown elemTypeTag: " + elemTypeTag);
		}
	}

	private Object readBooleanArray(final int n, final boolean b) throws IOException {
		final boolean[] array = new boolean[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readBoolean(false);
		return array;
	}

	private Object readInt8Array(final int n, final boolean b) throws IOException {
		final byte[] array = new byte[n];
		m_stream.readFully(array);
		return array;
	}

	private Object readInt16Array(final int n, final boolean b) throws IOException {
		final short[] array = new short[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readInt16(false);
		return array;
	}

	private Object readInt32Array(final int n, final boolean b) throws IOException {
		final int[] array = new int[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readInt32(false);
		return array;
	}

	private Object readInt64Array(final int n, final boolean b) throws IOException {
		final long[] array = new long[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readInt64(false);
		return array;
	}

	private Object readFloat32Array(final int n, final boolean b) throws IOException {
		final float[] array = new float[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readFloat32(false);
		return array;
	}

	private Object readFloat64Array(final int n, final boolean b) throws IOException {
		final double[] array = new double[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readFloat64(false);
		return array;
	}

	private Object readEnumArray(final int n, final ArrayType constraint, final boolean b)
			throws IOException {
		final Enum<?>[] array = constraint != null ? (Enum<?>[]) constraint.newInstance(n)
				: new Enum<?>[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readEnum(false, constraint != null ? (EnumType) constraint.elementType()
					: null);
		return array;
	}

	private String[] readStringArray(final int n, final boolean b) throws IOException {
		final String[] array = new String[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readString(false);
		return array;
	}

	private Object readObjectArray(final int n, final byte elemTypeTag, final ArrayType constraint)
			throws IOException {
		final Object[] array = constraint != null ? (Object[]) constraint.newInstance(n)
				: new Object[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readObject(elemTypeTag, constraint != null ? constraint.elementType() : null);
		return array;
	}

	private Object readObject(final byte typeTag, final Type constraint) throws IOException {
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
				return readEnum(false, (EnumType) constraint);
			} else {
				return readString(false);
			}
		case TAG_LIST: // array and list have the same write tag
			return readList(false, (ListOrArrayType) constraint);
		case TAG_MAP:
			return readMap(false, (MapType) constraint);
		case TAG_CUSTOM:
			return readMGenObject(false, (CustomType) constraint);
		default:
			throw new StreamCorruptedException("Unknown type tag: " + typeTag);
		}
	}

	private Object readArray(final boolean readTag, final ArrayType constraint) throws IOException {
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
			return constraint != null ? constraint.newInstance(0) : new Object[0];
		}
	}

	private ArrayList<Object> readList(final boolean tag, final ListOrArrayType constraint)
			throws IOException {

		if (tag)
			ensureTypeTag(null, TAG_LIST, readTypeTag());

		return readElements(constraint != null ? constraint.elementType() : null);

	}

	private HashMap<Object, Object> readMap(final boolean readTag, final MapType constraint)
			throws IOException {

		if (readTag)
			ensureTypeTag(null, TAG_MAP, readTypeTag());

		final int nElements = readSize();
		final HashMap<Object, Object> out = new HashMap<Object, Object>(nElements);

		if (nElements > 0) {

			final List<Object> keys = readElements(constraint != null ? constraint.keyType() : null);
			final List<Object> values = readElements(constraint != null ? constraint.valueType()
					: null);

			if (keys.size() != values.size() || keys.size() != nElements)
				throw new StreamCorruptedException("nKeys != nValues in map");

			for (int i = 0; i < keys.size(); i++)
				out.put(keys.get(i), values.get(i));

		}

		return out;
	}

	private ArrayList<Object> readElements(final Type constraint) throws IOException {

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
				if (constraint != null && constraint.typeEnum() == TypeEnum.ENUM) {
					final EnumType t = (EnumType) constraint;
					for (int i = 0; i < nElements; i++)
						out.add(readEnum(false, t));
				} else {
					for (int i = 0; i < nElements; i++)
						out.add(readFloat64(false));
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

	private Enum<?> readEnum(final boolean readTag, final EnumType constraint) throws IOException {
		final String writtenName = readString(readTag);
		return constraint != null ? constraint.get(writtenName) : null;
	}

}
