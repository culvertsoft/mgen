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

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.culvertsoft.mgen.api.exceptions.MissingRequiredFieldsException;
import se.culvertsoft.mgen.api.exceptions.SerializationException;
import se.culvertsoft.mgen.api.model.ArrayType;
import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.api.model.ListOrArrayType;
import se.culvertsoft.mgen.api.model.ListType;
import se.culvertsoft.mgen.api.model.MapType;
import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.api.model.UnknownCustomType;
import se.culvertsoft.mgen.api.util.Varint;
import se.culvertsoft.mgen.javapack.classes.ClassRegistry;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.metadata.FieldSetDepth;

public class BinaryReader extends BuiltInReader {

	private static final Charset charset = Charset.forName("UTF8");
	private static final short[] NO_IDS = new short[0];
	private final DataInput stream;
	@SuppressWarnings("unused")
	private final ReaderSettings readerSettings;
	private final ReadErrorListener errorHandler;

	public BinaryReader(final InputStream stream,
			final ClassRegistry classRegistry,
			final ReaderSettings readerSettings,
			final ReadErrorListener errorHandler) {
		super(classRegistry);
		this.stream = stream instanceof DataInput ? (DataInput) stream
				: new DataInputStream(stream);
		this.readerSettings = readerSettings;
		this.errorHandler = errorHandler;
	}

	public BinaryReader(final InputStream stream,
			final ClassRegistry classRegistry) {
		this(stream, classRegistry, ReaderSettings.DEFAULT,
				new ReadErrorAdapter());
	}

	@Override
	public final MGenBase readMGenObject() throws IOException {
		return readMGenObject(true);
	}

	protected MGenBase readMGenObject(final boolean readTypeTag)
			throws IOException {

		if (readTypeTag)
			ensureTypeTag(null, TAG_CUSTOM, readTypeTag());

		final short[] typeIds16Bit = readTypeIdsBit();
		final MGenBase out = m_classRegistry.instantiateByTypeIds16Bit(typeIds16Bit);

		readFields(out);

		return out;
	}

	private Field nextField(final MGenBase object) throws IOException {
		if (object != null) {
			// TODO: Handle fieldIdType settings
			final short fieldId = readFieldId();
			return object._fieldById(fieldId);
		} else {
			return null;
		}
	}

	private void readFields(final MGenBase object) throws IOException {

		final int nFields = readSize();

		System.out.println("Reading " + object);

		if (nFields < 0)
			throw new SerializationException(
					"MGenReader.read(..): stream corrupted, nFields < 0");

		for (int i = 0; i < nFields; i++) {
			final Field field = nextField(object);
			System.out.println("Reading field " + field);
			if (field != null) {
				object._readField(field, null, this);
			} else {
				handleUnknownField(null, null);
			}
		}

		System.out.println("read: " + object);
		System.out.println(object._validate(FieldSetDepth.SHALLOW));
		if (object != null && !object._validate(FieldSetDepth.SHALLOW)) {
			final ArrayList<Field> missingReqFields = new ArrayList<Field>();
			for (final Field f : object._fields()) {
				if (f.isRequired()
						&& !object._isFieldSet(f, FieldSetDepth.SHALLOW)) {
					missingReqFields.add(f);
				}
			}

			throw new MissingRequiredFieldsException(
					"MGenReader.read(..): Missing required fields ["
							+ missingReqFields + "] while reading object '"
							+ object + "'");
		}

	}

	private MGenBase readMGenObject(final boolean readTypeTag,
			final UnknownCustomType constraint) throws IOException {

		final MGenBase out = readMGenObject(readTypeTag);

		if (out != null && constraint != null) {
			if (!out.isInstanceOfTypeWithId(constraint.typeId())) {
				// TODO: Handle constraints failure
				return null;
			}
		}

		return out;

	}

	private short[] readTypeIdsBit() throws IOException {

		final int nTypeIds = readSize();

		if (nTypeIds > 0) {

			final short[] typeIds = new short[nTypeIds];

			for (int i = 0; i < typeIds.length; i++)
				typeIds[i] = readMgenTypeId();

			return typeIds;

		} else if (nTypeIds < 0) {
			throw new SerializationException(
					"MGenReader.readObject(..): Stream currupted. nTypeIds < 0");
		}

		return NO_IDS;

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
	public ArrayList<Object> readListField(final Field field,
			final Object context) throws IOException {
		ensureTypeTag(field, TAG_LIST, readTypeTag());
		return readList(false, (ListType) field.typ());
	}

	@Override
	public HashMap<Object, Object> readMapField(final Field field,
			final Object context) throws IOException {
		ensureTypeTag(field, TAG_MAP, readTypeTag());
		return readMap(false, (MapType) field.typ());
	}

	@Override
	public MGenBase readMgenObjectField(final Field field, final Object context)
			throws IOException {
		ensureTypeTag(field, TAG_CUSTOM, readTypeTag());
		return readMGenObject(false, (UnknownCustomType) field.typ());
	}

	@Override
	public void handleUnknownField(final Field field, final Object context)
			throws IOException {
		skip(readTypeTag());
	}

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
			throw new SerializationException("Stream corrupted!");
		}
	}

	private String readString(final boolean b) throws IOException {

		final int nBytes = readSize();

		if (nBytes < 0)
			throw new SerializationException("Stream corrupted!");

		if (nBytes > 0) {
			final byte[] bytes = new byte[nBytes];
			stream.readFully(bytes);
			return new String(bytes, charset);
		} else {
			return "";
		}

	}

	/*******************************************************************
	 * 
	 * 
	 * - - - - - - - - - - INTERNAL METHODS
	 * 
	 ******************************************************************/

	private byte readTypeTag() throws IOException {
		return stream.readByte();
	}

	private boolean readBoolean(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_BOOL, readTypeTag());
		return stream.readByte() != 0;
	}

	private byte readInt8(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_INT8, readTypeTag());
		return stream.readByte();
	}

	private short readInt16(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_INT16, readTypeTag());
		return stream.readShort();
	}

	private int readInt32(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_INT32, readTypeTag());
		return Varint.readSignedVarInt(stream);
	}

	private long readInt64(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_INT64, readTypeTag());
		return Varint.readSignedVarLong(stream);
	}

	private float readFloat32(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_FLOAT32, readTypeTag());
		return stream.readFloat();
	}

	private double readFloat64(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_FLOAT64, readTypeTag());
		return stream.readDouble();
	}

	private int readSize() throws IOException {
		return Varint.readUnsignedVarInt(stream);
	}

	private short readMgenTypeId() throws IOException {
		return readInt16(false);
	}

	private short readFieldId() throws IOException {
		return readInt16(false);
	}

	private void ensureTypeTag(final Field field, final byte expTag,
			final byte readTag) throws IOException {
		if (expTag != readTag) {
			handleUnexpectedType(field, expTag, readTag);
		}
	}

	private Object readArray2(final int n, final byte elemTypeTag,
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
			throw new SerializationException("Stream corrupted!");
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
		stream.readFully(array);
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

	private Object readObjectArray(final int n, final byte elemTypeTag,
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
			throw new SerializationException("Stream corrupted!");
		}
	}

	private Object readArray(final boolean readTag, final ArrayType expectType)
			throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_ARRAY, readTypeTag());

		final int nElements = readSize();

		if (nElements > 0) {

			final byte readElemTypeTag = readTypeTag();
			final Type expectElemType = expectType != null ? expectType
					.elementType() : null;
			final byte expectElemTypeTag = expectElemType != null ? expectElemType
					.binaryTypeTag() : readElemTypeTag;
			final Object array = readArray2(nElements, readElemTypeTag,
					expectType);

			if (expectElemTypeTag == readElemTypeTag) {
				return array;
			} else {
				errorHandler.handleUnexpectedElementType(null,
						expectElemTypeTag, readElemTypeTag, NO_IDS, array);
				return null;
			}

		} else if (nElements == 0) {

			return expectType != null ? expectType.newInstance(0) : null;

		} else {
			throw new SerializationException("Stream corrupt!");
		}

	}

	private ArrayList<Object> readList(final boolean readTag,
			final ListOrArrayType listOrArrayType) throws IOException {

		if (readTag)
			ensureTypeTag(null, TAG_LIST, readTypeTag());

		return readElements(listOrArrayType != null ? listOrArrayType
				.elementType() : null);

	}

	private HashMap<Object, Object> readMap(final boolean readTag,
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
				throw new SerializationException("Stream corrupted!");

			for (int i = 0; i < keys.size(); i++)
				out.put(keys.get(i), values.get(i));

		} else if (nElements < 0) {
			throw new SerializationException("Stream corrupt!");
		}

		return out;
	}

	private ArrayList<Object> readElements(final Type constraint)
			throws IOException {

		final int nElements = readSize();

		if (nElements < 0) {
			throw new SerializationException("Stream corrupt!");
		}

		final ArrayList<Object> out = new ArrayList<Object>(nElements);

		final byte readElemTag = readTypeTag();
		final boolean typeTagOk = constraint == null
				|| constraint.binaryTypeTag() == readElemTag;

		if (typeTagOk) {
			for (int i = 0; i < nElements; i++)
				out.add(readObject(readTypeTag(), constraint));
		} else {

			// TODO: notify somehow that the wrong type is arriving...

			for (int i = 0; i < nElements; i++)
				readObject(readTypeTag(), constraint);
		}

		return out;
	}

	private void handleUnexpectedType(final Field field, final byte expTag,
			final byte readTag) throws IOException {
		skip(readTag);
		System.err.println("Unexpected type (field=" + field
				+ ") expect/read: " + expTag + "/" + readTag);
	}

}
