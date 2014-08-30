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
import static se.culvertsoft.mgen.javapack.util.BuiltInSerializerUtils.ensureNoMissingReqFields;
import static se.culvertsoft.mgen.javapack.util.BuiltInSerializerUtils.throwUnexpectTag;

import java.io.ByteArrayInputStream;
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

/**
 * A class for reading data streams written in the MGen binary wire format.
 * BinaryReader objects are constructed with two type parameters: MGenStreamType
 * and ClassRegistryType. These specify which type of data input stream is to be
 * read from and what classes can be marshalled, respectively.
 */
public class BinaryReader extends BuiltInReader {

	private InputStream m_stream;
	private final byte m_readBuffer64[] = new byte[8];

	/**
	 * Creates a new binary reader, wrapping a data input source (InputStream)
	 * and a ClassRegistry.
	 * 
	 * @param stream
	 *            The data input source.
	 * 
	 * @param classRegistry
	 *            The class registry to use for marshaling objects
	 */
	public BinaryReader(final InputStream stream, final ClassRegistryBase classRegistry) {
		super(classRegistry);
		m_stream = stream;
	}

	/**
	 * Creates a new binary reader without setting a data input source.
	 * 
	 * @param classRegistry
	 *            The class registry to use for marshaling objects.
	 */
	public BinaryReader(final ClassRegistryBase classRegistry) {
		this(EMPTY_INPUT_STREAM, classRegistry);
	}

	/**
	 * Sets the data input source for this reader.
	 * 
	 * @param stream
	 *            The new data input source.
	 * 
	 * @return This reader
	 */
	public BinaryReader setInput(final InputStream stream) {
		m_stream = stream;
		return this;
	}

	/**
	 * Reads an MGen object from the input source of this reader.
	 * 
	 * @return The MGen object read, or null if it was of unknown type.
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	@Override
	public final MGenBase readObject() throws IOException {
		return readMGenObject(true, null);
	}

	/**
	 * Convenience method for reading a single MGen object from a byte array.
	 * 
	 * @param objData
	 *            The bytes containing the MGen object.
	 * 
	 * @return The read Mgen object, or null if it was of unknown type.
	 * 
	 * @throws IOException
	 *             If the data input source throws an IOException
	 */
	public final MGenBase readObject(final byte[] objData) throws IOException {
		return setInput(new ByteArrayInputStream(objData)).readMGenObject(true, null);
	}

	/**
	 * Reads an object of the specified type, or a subtype of the specified
	 * type.
	 * 
	 * @param typ
	 *            The type to read an object of.
	 * 
	 * @return The object read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends MGenBase> T readObject(final Class<T> typ) throws IOException {

		final ClassRegistryEntry entry = m_clsReg.getByClass(typ);

		if (entry == null)
			throw new UnknownTypeException("Could not read object of type " + typ
					+ ", since it is know known by the class registry");

		return (T) readMGenObject(true, entry.typ());
	}

	/**
	 * Convenience method for reading a single MGen object from a byte array.
	 * Reads an object of the specified type, or a subtype of the specified
	 * type.
	 * 
	 * @param objData
	 *            The bytes containing the object
	 * 
	 * @param typ
	 *            The type of the object to read
	 * 
	 * @return The object read
	 * 
	 * @throws IOException
	 *             If the data input source throws an IOException
	 */
	public <T extends MGenBase> T readObject(final byte[] objData, final Class<T> typ)
			throws IOException {
		return setInput(new ByteArrayInputStream(objData)).readObject(typ);
	}

	/**
	 * Reads a boolean field from the underlying data input source.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            Not used by this reader
	 * 
	 * @return The field value read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	@Override
	public boolean readBooleanField(final Field field, final Object context) throws IOException {
		ensureTypeTag(field, TAG_BOOL, readTypeTag());
		return readBoolean(false);
	}

	/**
	 * Reads an int8 field from the underlying data input source.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            Not used by this reader
	 * 
	 * @return The field value read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	@Override
	public byte readInt8Field(final Field field, final Object context) throws IOException {
		ensureTypeTag(field, TAG_INT8, readTypeTag());
		return readInt8(false);
	}

	/**
	 * Reads an int16 field from the underlying data input source.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            Not used by this reader
	 * 
	 * @return The field value read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	@Override
	public short readInt16Field(final Field field, final Object context) throws IOException {
		ensureTypeTag(field, TAG_INT16, readTypeTag());
		return readInt16(false);
	}

	/**
	 * Reads an int32 field from the underlying data input source.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            Not used by this reader
	 * 
	 * @return The field value read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	@Override
	public int readInt32Field(final Field field, final Object context) throws IOException {
		ensureTypeTag(field, TAG_INT32, readTypeTag());
		return readInt32(false);
	}

	/**
	 * Reads an int64 field from the underlying data input source.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            Not used by this reader
	 * 
	 * @return The field value read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	@Override
	public long readInt64Field(final Field field, final Object context) throws IOException {
		ensureTypeTag(field, TAG_INT64, readTypeTag());
		return readInt64(false);
	}

	/**
	 * Reads a float32 field from the underlying data input source.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            Not used by this reader
	 * 
	 * @return The field value read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	@Override
	public float readFloat32Field(final Field field, final Object context) throws IOException {
		ensureTypeTag(field, TAG_FLOAT32, readTypeTag());
		return readFloat32(false);
	}

	/**
	 * Reads a float64 field from the underlying data input source.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            Not used by this reader
	 * 
	 * @return The field value read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	@Override
	public double readFloat64Field(final Field field, final Object context) throws IOException {
		ensureTypeTag(field, TAG_FLOAT64, readTypeTag());
		return readFloat64(false);
	}

	/**
	 * Reads a string field from the underlying data input source.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            Not used by this reader
	 * 
	 * @return The field value read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	@Override
	public String readStringField(final Field field, final Object context) throws IOException {
		ensureTypeTag(field, TAG_STRING, readTypeTag());
		return readString(false);
	}

	/**
	 * Reads an object array (T[]) field from the underlying data input source.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            Not used by this reader
	 * 
	 * @return The field value read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	@Override
	public Object readArrayField(final Field field, final Object context) throws IOException {
		ensureTypeTag(field, TAG_LIST, readTypeTag());
		return readArray(false, (ArrayType) field.typ());
	}

	/**
	 * Reads an object java.util.ArrayList field from the underlying data input
	 * source.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            Not used by this reader
	 * 
	 * @return The field value read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	@Override
	public ArrayList<Object> readListField(final Field field, final Object context)
			throws IOException {
		ensureTypeTag(field, TAG_LIST, readTypeTag());
		return readList(false, (ListType) field.typ());
	}

	/**
	 * Reads an object java.util.HashMap field from the underlying data input
	 * source.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            Not used by this reader
	 * 
	 * @return The field value read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	@Override
	public HashMap<Object, Object> readMapField(final Field field, final Object context)
			throws IOException {
		ensureTypeTag(field, TAG_MAP, readTypeTag());
		return readMap(false, (MapType) field.typ());
	}

	/**
	 * Reads an MGen object field from the underlying data input source.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            Not used by this reader
	 * 
	 * @return The field value read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	@Override
	public MGenBase readMgenObjectField(final Field f, final Object context) throws IOException {
		ensureTypeTag(f, TAG_CLASS, readTypeTag());
		return readMGenObject(false, (RuntimeClassType) f.typ());
	}

	/**
	 * Reads an enum field from the underlying data input source.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            Not used by this reader
	 * 
	 * @return The field value read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	@Override
	public Enum<?> readEnumField(final Field f, final Object context) throws IOException {
		final byte tag = readTypeTag();
		ensureTypeTag(f, TAG_STRING, tag);
		return readEnum(false, (RuntimeEnumType) f.typ());
	}

	/**
	 * 
	 * When reading objects from a data stream, new fields may have been added
	 * to the data model without us having generated source code for it yet.
	 * New/Unknown fields are simply discarded and skipped past in the stream.
	 * This callback is meant to be overloaded should you want to deal with
	 * unknown fields in a different manner.
	 * 
	 * @param field
	 *            null
	 * 
	 * @param context
	 *            Not used by this reader
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
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

	/**
	 * Internal helper array returned when no type ids are read.
	 */
	private static final short[] NO_IDS = new short[0];

	/**
	 * Internal method for skipping a value
	 * 
	 * @param typeTag
	 *            The binary type tag for what type to skip
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
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
			throw new StreamCorruptedException("Cannot skip item of unknown typeTag: " + typeTag);
		}
	}

	/**
	 * Internal method for skipping a map
	 * 
	 * @param doReadTag
	 *            If we should start by reading the map's binary type tag
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
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

	/**
	 * Internal method for skipping a list
	 * 
	 * @param doReadTag
	 *            If we should start by reading the map's binary type tag
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
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

	/**
	 * Internal method for skipping a string
	 * 
	 * @param doReadTag
	 *            If we should start by reading the map's binary type tag
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private void skipString(final boolean doReadTag) throws IOException {
		if (doReadTag)
			ensureTypeTag(null, TAG_STRING, readTypeTag());
		final int nBytes = readSize();
		for (int i = 0; i < nBytes; i++)
			m_stream.read();
	}

	/**
	 * Internal method for skipping an MGen object
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
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

	/**
	 * Internal method for skipping past type ids written before an MGen object
	 * 
	 * @param n
	 *            The number of type ids to skip past
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private void skipTypeIds(int n) throws IOException {
		for (int i = 0; i < n; i++)
			readMgenTypeId();
	}

	/**
	 * Internal method for reading a string
	 * 
	 * @param readTag
	 *            If we should start by verifying the written binary type tag
	 * 
	 * @return The string read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
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

	/**
	 * Internal method for reading fields from a stream to an object.
	 * 
	 * @param object
	 *            The object to read fields to
	 * 
	 * @param nFields
	 *            The number of fields to read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private void readFields(final MGenBase object, final int nFields) throws IOException {

		for (int i = 0; i < nFields; i++)
			object._readField(readFieldId(), null, this);

		ensureNoMissingReqFields(object);
	}

	/**
	 * Internal method for skipping MGen object fields
	 * 
	 * @param nFields
	 *            The number of fields to skip
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private void skipFields(final int nFields) throws IOException {
		for (int i = 0; i < nFields; i++) {
			readFieldId();
			skip(readTypeTag());
		}
	}

	/**
	 * Internal method for reading an MGen object
	 * 
	 * @param readTypeTag
	 *            If we should start by verifying the written binary type tag
	 * 
	 * @param constraint
	 *            An optional type constraint. If the object read doesn't match
	 *            the type constraint, an UnexpectedTypeException is thrown
	 * 
	 * @return The object read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private MGenBase readMGenObject(final boolean readTypeTag, final RuntimeClassType constraint)
			throws IOException {

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

	/**
	 * Internal method for reading type ids that are prepended in streams before
	 * MGen objects.
	 * 
	 * @param nTypeIds
	 *            The number of ids to read
	 * 
	 * @return The read ids
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
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

	/**
	 * Internal method for reading a binary type tag
	 * 
	 * @return The binary type tag read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private byte readTypeTag() throws IOException {
		return (byte) m_stream.read();
	}

	/**
	 * Internal method for reading a boolean
	 * 
	 * @param readTag
	 *            If we should start by verifying the written binary type tag
	 * 
	 * @return The value read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private boolean readBoolean(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_BOOL, readTypeTag());
		return m_stream.read() != 0;
	}

	/**
	 * Internal method for reading an int8
	 * 
	 * @param readTag
	 *            If we should start by verifying the written binary type tag
	 * 
	 * @return The value read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private byte readInt8(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_INT8, readTypeTag());
		return (byte) m_stream.read();
	}

	/**
	 * Internal method for reading an int16
	 * 
	 * @param readTag
	 *            If we should start by verifying the written binary type tag
	 * 
	 * @return The value read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private short readInt16(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_INT16, readTypeTag());
		int ch1 = m_stream.read();
		int ch2 = m_stream.read();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return (short) ((ch1 << 8) + (ch2 << 0));
	}

	/**
	 * Internal method for reading an int32
	 * 
	 * @param readTag
	 *            If we should start by verifying the written binary type tag
	 * 
	 * @return The value read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private int readInt32(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_INT32, readTypeTag());
		return Varint.readSignedVarInt(m_stream);
	}

	/**
	 * Internal method for reading an int64
	 * 
	 * @param readTag
	 *            If we should start by verifying the written binary type tag
	 * 
	 * @return The value read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private long readInt64(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_INT64, readTypeTag());
		return Varint.readSignedVarLong(m_stream);
	}

	/**
	 * Internal method for reading a float32
	 * 
	 * @param readTag
	 *            If we should start by verifying the written binary type tag
	 * 
	 * @return The value read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private float readFloat32(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_FLOAT32, readTypeTag());

		return Float.intBitsToFloat(read32());
	}

	/**
	 * Internal method for reading a fixed size big-endian 32 bit integer.
	 * 
	 * @return The value read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private int read32() throws IOException {
		int ch1 = m_stream.read();
		int ch2 = m_stream.read();
		int ch3 = m_stream.read();
		int ch4 = m_stream.read();
		if ((ch1 | ch2 | ch3 | ch4) < 0)
			throw new EOFException();
		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}

	/**
	 * Internal method for reading a float64
	 * 
	 * @param readTag
	 *            If we should start by verifying the written binary type tag
	 * 
	 * @return The value read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private double readFloat64(final boolean readTag) throws IOException {
		if (readTag)
			ensureTypeTag(null, TAG_FLOAT64, readTypeTag());
		return Double.longBitsToDouble(read64());
	}

	/**
	 * Internal method for reading a fixed size big-endian 64 bit integer.
	 * 
	 * @return The value read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private long read64() throws IOException {
		StreamUtil.readFully(m_stream, 8, m_readBuffer64);
		return (((long) m_readBuffer64[0] << 56) + ((long) (m_readBuffer64[1] & 255) << 48)
				+ ((long) (m_readBuffer64[2] & 255) << 40)
				+ ((long) (m_readBuffer64[3] & 255) << 32)
				+ ((long) (m_readBuffer64[4] & 255) << 24) + ((m_readBuffer64[5] & 255) << 16)
				+ ((m_readBuffer64[6] & 255) << 8) + ((m_readBuffer64[7] & 255) << 0));
	}

	/**
	 * Internal method for reading a size value
	 * 
	 * @return The value read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private int readSize() throws IOException {
		final int out = Varint.readUnsignedVarInt(m_stream);
		if (out < 0)
			throw new StreamCorruptedException("readSize() < 0");
		return out;
	}

	/**
	 * Internal method for reading a single 16 bit type id.
	 * 
	 * @return The id read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private short readMgenTypeId() throws IOException {
		return readInt16(false);
	}

	/**
	 * Internal method for reading a field id.
	 * 
	 * @return The id read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private short readFieldId() throws IOException {
		return readInt16(false);
	}

	/**
	 * Internal convenience method for verifying an expected binary type tag
	 * versus an actual read one.
	 * 
	 * @param field
	 *            The field the comparison is made for, or null
	 * 
	 * @param expTag
	 *            The expected binary type tag
	 * 
	 * @param readTag
	 *            The actual read binary type tag
	 */
	private void ensureTypeTag(final Field field, final byte expTag, final byte readTag) {
		if (expTag != readTag) {
			throwUnexpectTag("(for field " + field + ")", expTag, readTag);
		}
	}

	/**
	 * Helper method for reading an array
	 * 
	 * @param n
	 *            The number of array elements to read
	 * 
	 * @param elemTypeTag
	 *            The binary type tag of the array elements
	 * 
	 * @param constraint
	 *            The type metadata describing the array type
	 * 
	 * @return The array read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
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
		case TAG_CLASS:
			return readObjectArray(n, elemTypeTag, constraint);
		default:
			throw new StreamCorruptedException("Unknown elemTypeTag: " + elemTypeTag);
		}
	}

	/**
	 * Internal helper method for reading an array of booleans quickly
	 * 
	 * @param n
	 *            The number of array elements to read
	 * 
	 * @param b
	 *            ignored
	 * 
	 * @return The array read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private Object readBooleanArray(final int n, final boolean b) throws IOException {
		final boolean[] array = new boolean[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readBoolean(false);
		return array;
	}

	/**
	 * Internal helper method for reading an array of int8s quickly
	 * 
	 * @param n
	 *            The number of array elements to read
	 * 
	 * @param b
	 *            ignored
	 * 
	 * @return The array read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private Object readInt8Array(final int n, final boolean b) throws IOException {
		final byte[] array = new byte[n];
		StreamUtil.readFully(m_stream, n, array);
		return array;
	}

	/**
	 * Internal helper method for reading an array of int16s quickly
	 * 
	 * @param n
	 *            The number of array elements to read
	 * 
	 * @param b
	 *            ignored
	 * 
	 * @return The array read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private Object readInt16Array(final int n, final boolean b) throws IOException {
		final short[] array = new short[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readInt16(false);
		return array;
	}

	/**
	 * Internal helper method for reading an array of int32s quickly
	 * 
	 * @param n
	 *            The number of array elements to read
	 * 
	 * @param b
	 *            ignored
	 * 
	 * @return The array read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private Object readInt32Array(final int n, final boolean b) throws IOException {
		final int[] array = new int[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readInt32(false);
		return array;
	}

	/**
	 * Internal helper method for reading an array of int64s quickly
	 * 
	 * @param n
	 *            The number of array elements to read
	 * 
	 * @param b
	 *            ignored
	 * 
	 * @return The array read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private Object readInt64Array(final int n, final boolean b) throws IOException {
		final long[] array = new long[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readInt64(false);
		return array;
	}

	/**
	 * Internal helper method for reading an array of float32s quickly
	 * 
	 * @param n
	 *            The number of array elements to read
	 * 
	 * @param b
	 *            ignored
	 * 
	 * @return The array read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private Object readFloat32Array(final int n, final boolean b) throws IOException {
		final float[] array = new float[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readFloat32(false);
		return array;
	}

	/**
	 * Internal helper method for reading an array of float64s quickly
	 * 
	 * @param n
	 *            The number of array elements to read
	 * 
	 * @param b
	 *            ignored
	 * 
	 * @return The array read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private Object readFloat64Array(final int n, final boolean b) throws IOException {
		final double[] array = new double[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readFloat64(false);
		return array;
	}

	/**
	 * Internal helper method for reading an array of enums quickly
	 * 
	 * @param n
	 *            The number of array elements to read
	 * 
	 * @param b
	 *            ignored
	 * 
	 * @return The array read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private Object readEnumArray(final int n, final ArrayType constraint, final boolean b)
			throws IOException {
		final Enum<?>[] array = constraint != null ? (Enum<?>[]) constraint.newInstance(n)
				: new Enum<?>[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readEnum(
					false,
					constraint != null ? (RuntimeEnumType) constraint.elementType() : null);
		return array;
	}

	/**
	 * Internal helper method for reading an array of strings quickly
	 * 
	 * @param n
	 *            The number of array elements to read
	 * 
	 * @param b
	 *            ignored
	 * 
	 * @return The array read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private String[] readStringArray(final int n, final boolean b) throws IOException {
		final String[] array = new String[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readString(false);
		return array;
	}

	/**
	 * Internal helper method for reading an array of objects quickly
	 * 
	 * @param n
	 *            The number of array elements to read
	 * 
	 * @param elemTypeTag
	 *            The binary type tag of the elements
	 * 
	 * @param constraint
	 *            Optional parameter which specifies a type constraint for the
	 *            array to read back. Throws UnexpectedTypeException if the type
	 *            read doesn't match the constraint.
	 * 
	 * @return The array read back.
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private Object readObjectArray(final int n, final byte elemTypeTag, final ArrayType constraint)
			throws IOException {
		final Object[] array = constraint != null ? (Object[]) constraint.newInstance(n)
				: new Object[n];
		for (int i = 0; i < array.length; i++)
			array[i] = readObject(elemTypeTag, constraint != null ? constraint.elementType() : null);
		return array;
	}

	/**
	 * Internal method for reading a generic Object
	 * 
	 * @param typeTag
	 *            The binary type tag read
	 * 
	 * @param constraint
	 *            An optional type constraint for the object to be read. Throws
	 *            UnexpectedTypeException if the type read doesn't match the
	 *            constraint.
	 * 
	 * @return The object read.
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
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

	/**
	 * Internal method for reading an array
	 * 
	 * @param readTag
	 *            If we should start by verifying the written binary type tag
	 * 
	 * @param constraint
	 *            An optional type constraint for the array to be read. Throws
	 *            UnexpectedTypeException if the type read doesn't match the
	 *            constraint.
	 * 
	 * @return The array read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
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

	/**
	 * Internal method for reading a list.
	 * 
	 * @param doReadTag
	 *            If we should start by verifying the written binary type tag
	 * 
	 * @param constraint
	 *            An optional type constraint for the list to be read. Throws
	 *            UnexpectedTypeException if the type read doesn't match the
	 *            constraint.
	 * 
	 * @return The list read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private ArrayList<Object> readList(final boolean doReadTag, final ListOrArrayType constraint)
			throws IOException {

		if (doReadTag)
			ensureTypeTag(null, TAG_LIST, readTypeTag());

		return readElements(false, constraint != null ? constraint.elementType() : null);

	}

	/**
	 * Internal method for reading a map
	 * 
	 * @param doReadTag
	 *            If we should start by verifying the written binary type tag
	 * 
	 * @param constraint
	 *            An optional type constraint for the map to be read. Throws
	 *            UnexpectedTypeException if the type read doesn't match the
	 *            constraint.
	 * 
	 * @return The read map
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private HashMap<Object, Object> readMap(final boolean doReadTag, final MapType constraint)
			throws IOException {

		if (doReadTag)
			ensureTypeTag(null, TAG_MAP, readTypeTag());

		final int nElements = readSize();
		final HashMap<Object, Object> out = new HashMap<Object, Object>(nElements);

		if (nElements > 0) {

			final byte keyTag = readTypeTag();
			final byte valueTag = readTypeTag();

			final Type keyType = constraint != null ? constraint.keyType() : null;
			final Type valueType = constraint != null ? constraint.valueType() : null;

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

	/**
	 * Internal helper method for reading list elements, used exclusively by
	 * readList
	 * 
	 * @param doReadTag
	 *            If we should start by verifying the written binary type tag
	 * 
	 * @param constraint
	 *            An optional type constraint for the list to be read. Throws
	 *            UnexpectedTypeException if the type read doesn't match the
	 *            constraint.
	 * 
	 * @return The list read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private ArrayList<Object> readElements(final boolean doReadTag, final Type constraint)
			throws IOException {

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
				if (constraint != null && constraint.typeEnum() == TypeEnum.ENUM) {
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

	/**
	 * Internal helper method for instantiating an object from read 16 bit type
	 * ids.
	 * 
	 * @param ids
	 *            The type ids
	 * 
	 * @param constraint
	 *            An optional type constraint. Throws UnexpectedTypeException if
	 *            the type read doesn't match the constraint.
	 * 
	 * @return The instantiated read
	 */
	protected MGenBase instantiate(final short[] ids, final RuntimeClassType constraint) {

		if (ids == null && constraint == null)
			return null;

		final ClassRegistryEntry entry = ids != null ? m_clsReg.getByTypeIds16Bit(ids) : m_clsReg
				.getById(constraint.typeId());

		if (constraint != null) {
			if (entry == null) {
				throw new UnexpectedTypeException("Unknown type: " + Arrays.toString(ids));
			} else if (!entry.isInstanceOfTypeId(constraint.typeId())) {
				throw new UnexpectedTypeException("Unexpected type. Expected "
						+ constraint.fullName() + " but got " + entry.clsName());
			}
		}

		return entry != null ? entry.construct() : null;

	}

	/**
	 * Internal helper method for reading an enum
	 * 
	 * @param readTag
	 *            If we should start by verifying the written binary type tag
	 * 
	 * @param constraint
	 *            An required type constraint. In order to read the enum value,
	 *            an enum type (the constraint) must be specified. Enums are
	 *            written on the wire as strings, so the constraint parameter is
	 *            what matches these strings against an actual local enum value.
	 * 
	 * @return The enum read
	 * 
	 * @throws IOException
	 *             If the underlying data input source throws an IOException
	 */
	private Enum<?> readEnum(final boolean readTag, final RuntimeEnumType constraint)
			throws IOException {
		final String writtenName = readString(readTag);
		return constraint != null ? constraint.get(writtenName) : null;
	}

}
