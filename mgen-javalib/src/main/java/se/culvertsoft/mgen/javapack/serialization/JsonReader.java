package se.culvertsoft.mgen.javapack.serialization;

import static se.culvertsoft.mgen.javapack.util.BuiltInSerializerUtils.ensureNoMissingReqFields;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import se.culvertsoft.mgen.api.model.ArrayType;
import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.api.model.ListType;
import se.culvertsoft.mgen.api.model.MapType;
import se.culvertsoft.mgen.api.model.RuntimeClassType;
import se.culvertsoft.mgen.api.model.RuntimeEnumType;
import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.javapack.classes.ClassRegistryBase;
import se.culvertsoft.mgen.javapack.classes.ClassRegistryEntry;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.exceptions.MissingRequiredFieldsException;
import se.culvertsoft.mgen.javapack.exceptions.StreamCorruptedException;
import se.culvertsoft.mgen.javapack.exceptions.UnexpectedTypeException;
import se.culvertsoft.mgen.javapack.exceptions.UnknownTypeException;
import se.culvertsoft.mgen.javapack.serialization.mgen2jsonsimple.MGenJSONParser;

/**
 * A class for reading data streams written in JSON format. JsonReader objects
 * must be constructed with a ClassRegistry object to facilitate object
 * marshaling.
 */
public class JsonReader extends BuiltInReader {

	private final MGenJSONParser m_parser;

	/**
	 * Creates a new JSON reader without specifying a data input source.
	 * 
	 * @param classRegistry
	 *            The class registry to use for marshaling objects
	 */
	public JsonReader(final ClassRegistryBase classRegistry) {
		this(EMPTY_INPUT_STREAM, classRegistry);
	}

	/**
	 * Creates a new JsonReader, wrapping a data input source (InputStream) and
	 * a ClassRegistry.
	 * 
	 * @param stream
	 *            The data input source.
	 * 
	 * @param classRegistry
	 *            The class registry to use for marshaling objects
	 */
	public JsonReader(final InputStream stream, final ClassRegistryBase classRegistry) {
		this(new InputStreamReader(stream, CHARSET_UTF8), classRegistry);
	}

	/**
	 * Creates a new JsonReader around a specific Java StringReader.
	 * 
	 * @param stringReader
	 *            The StringReader to read from
	 * 
	 * @param classRegistry
	 *            The class registry to use for marshaling objects
	 */
	public JsonReader(final StringReader stringReader, final ClassRegistryBase classRegistry) {
		this((java.io.Reader) stringReader, classRegistry);
	}

	/**
	 * Creates a new JsonReader around a String as it's data input stource.
	 * 
	 * @param json
	 *            The JSON string to read from
	 * 
	 * @param classRegistry
	 *            The class registry to use for marshaling objects
	 */
	public JsonReader(final String json, final ClassRegistryBase classRegistry) {
		this(new StringReader(json), classRegistry);
	}

	/**
	 * Replaces the internal data input source with a new one.
	 * 
	 * @param byteStream
	 *            The new data input source.
	 * 
	 * @return This JsonReader
	 */
	public JsonReader setInput(final InputStream byteStream) {
		return setInput(new InputStreamReader(byteStream, CHARSET_UTF8));
	}

	/**
	 * Replaces the internal data input source with a new one.
	 * 
	 * @param json
	 *            The new data input source.
	 * 
	 * @return This JsonReader
	 */
	public JsonReader setInput(final String json) {
		return setInput(new StringReader(json));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MGenBase readObject() throws IOException {
		return readMGenObject(parseRootObject(), null);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends MGenBase> T readObject(final Class<T> typ) throws IOException {

		final ClassRegistryEntry entry = m_clsReg.getByClass(typ);

		if (entry == null)
			throw new UnknownTypeException("Could not read object of type " + typ
					+ ", since it is know known by the class registry");

		return (T) readMGenObject(parseRootObject(), entry.typ());
	}

	/**
	 * Read API for users. This method reads an MGen object from the provided
	 * JSON string.
	 * 
	 * @return The MGen object read from the string, or null if the type of the
	 *         object on the stream was unknown (Readers then skip past the
	 *         object).
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	public MGenBase readObject(final String object) throws IOException {
		return setInput(new StringReader(object)).readObject();
	}

	/**
	 * Read API for users. This method reads an MGen object from the provided
	 * JSON string.
	 * 
	 * @return The MGen object read from the string, or null if the type of the
	 *         object on the stream was unknown (Readers then skip past the
	 *         object).
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	public <T extends MGenBase> T readObject(final String object, final Class<T> typ)
			throws IOException {
		return setInput(new StringReader(object)).readObject(typ);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean readBooleanField(final Field field, final Object context) throws IOException {
		return (Boolean) (((JSONObject) context).get(field.name()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte readInt8Field(final Field field, final Object context) throws IOException {
		return ((Number) (((JSONObject) context).get(field.name()))).byteValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public short readInt16Field(final Field field, final Object context) throws IOException {
		return ((Number) (((JSONObject) context).get(field.name()))).shortValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int readInt32Field(final Field field, final Object context) throws IOException {
		return ((Number) (((JSONObject) context).get(field.name()))).intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long readInt64Field(final Field field, final Object context) throws IOException {
		return ((Number) (((JSONObject) context).get(field.name()))).longValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float readFloat32Field(final Field field, final Object context) throws IOException {
		return ((Number) (((JSONObject) context).get(field.name()))).floatValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double readFloat64Field(final Field field, final Object context) throws IOException {
		return ((Number) (((JSONObject) context).get(field.name()))).doubleValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String readStringField(final Field field, final Object context) throws IOException {
		return (String) (((JSONObject) context).get(field.name()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object readArrayField(final Field field, final Object context) throws IOException {
		return readArray((ArrayType) field.typ(), getJsonArr(field, context));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<?> readListField(final Field field, final Object context) throws IOException {
		return readList((ListType) field.typ(), getJsonArr(field, context));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HashMap<?, ?> readMapField(final Field field, final Object context) throws IOException {
		return readMap((MapType) field.typ(), getJsonObj(field, context));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final MGenBase readMgenObjectField(final Field field, final Object context)
			throws IOException {
		return readMGenObject(getJsonObj(field, context), (RuntimeClassType) field.typ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enum<?> readEnumField(final Field field, final Object context) throws IOException {
		final String stringVal = (String) (((JSONObject) context).get(field.name()));
		return cvtString2Enum((RuntimeEnumType) field.typ(), stringVal);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleUnknownField(final Field field, final Object context) throws IOException {
	}

	/************************************************
	 * 
	 * 
	 * PRIVATE METHODS
	 * 
	 ***********************************************/

	/**
	 * Internal constructor that all other constructors call to.
	 * 
	 * @param utf8Reader
	 *            The input source to use
	 * 
	 * @param classRegistry
	 *            The class registry to use for marshaling objects
	 */
	private JsonReader(final java.io.Reader utf8Reader, final ClassRegistryBase classRegistry) {
		super(classRegistry);
		m_parser = new MGenJSONParser(utf8Reader);
	}

	/**
	 * Internal method for changing the current input source
	 * 
	 * @param utf8Reader
	 *            The new input source to use
	 * 
	 * @return This JsonReader
	 */
	private JsonReader setInput(final java.io.Reader utf8Reader) {
		m_parser.setInput(utf8Reader);
		return this;
	}

	/**
	 * Internal method for reading an MGen object
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @param constraint
	 *            An optional type constraint. If the constraint is not met, an
	 *            UnexpectedTypeException is thrown.
	 * 
	 * @return The object read, or null if it was of unknown type
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private MGenBase readMGenObject(final JSONObject node, final RuntimeClassType constraint)
			throws IOException {

		if (node == null)
			return null;

		final MGenBase object = instantiate(readIds(node, constraint), constraint);

		if (object != null) {
			readObjectFields(object, node);
			ensureNoMissingReqFields(object);
			return object;
		} else {
			return null;
		}

	}

	/**
	 * Internal helper method for reading 16bit MGen type ids in base64 form
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @param constraint
	 *            An optional type constraint. If the constraint is not met, an
	 *            UnexpectedTypeException is thrown.
	 * 
	 * @return The 16bit MGen type ids in base64 form read
	 */
	private String[] readIds(final JSONObject node, final Type constraint) {

		final Object tNode = node.get("__t");

		// Try default type
		if (tNode == null) {

			if (constraint != null)
				return null;

			throw new MissingRequiredFieldsException(
					"MGenJSonReader.readMGenObject: Missing field '__t'");
		}

		final String typeIdsString = tNode.toString();

		final String[] ids = new String[typeIdsString.length() / 3];
		for (int i = 0; i < ids.length; i++)
			ids[i] = typeIdsString.substring(i * 3, i * 3 + 3);

		return ids;
	}

	/**
	 * Internal helper method. Attempts to interpret a JSON node as a JSONObject
	 * and get a field from it, and interpret that field as another JSONObject.
	 * 
	 * @param field
	 *            The field to find.
	 * 
	 * @param context
	 *            The parent to interpret as a JSONObject/JSON node
	 * 
	 * @return The final interpreted node
	 */
	private JSONObject getJsonObj(final Field field, final Object context) {
		return (JSONObject) ((JSONObject) context).get(field.name());
	}

	/**
	 * Internal helper method. Attempts to interpret a JSON node as a JSONObject
	 * and get a field from it, and interpret that field as an array.
	 * 
	 * @param field
	 *            The field to find.
	 * 
	 * @param context
	 *            The parent to interpret as a JSONObject/JSON node
	 * 
	 * @return The final interpreted node
	 */
	private JSONArray getJsonArr(final Field field, final Object context) {
		return (JSONArray) ((JSONObject) context).get(field.name());
	}

	/**
	 * Internal helper method for reading the fields of an object from a JSON
	 * node.
	 * 
	 * @param object
	 *            The object to read the fields to
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private void readObjectFields(final MGenBase object, final JSONObject node) throws IOException {
		for (final Object keyO : node.keySet()) {
			final String name = (String) keyO;
			final Field field = object._fieldByName(name);
			if (field != null) {
				object._readField(field.id(), node, this);
			}
		}

	}

	/**
	 * Internal method for reading a map
	 * 
	 * @param typ
	 *            Type meta data information about the map to read
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @return The map read
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private HashMap<?, ?> readMap(MapType typ, JSONObject node) throws IOException {

		if (node == null)
			return null;

		final HashMap<Object, Object> out = new HashMap<Object, Object>(node.size());

		for (final Object keyNode : node.keySet()) {
			final Object key = cvtMapKeyType((String) keyNode, typ.keyType());
			final Object value = readObject(node.get(keyNode), typ.valueType());
			out.put(key, value);
		}

		return out;
	}

	/**
	 * Internal helper method for converting JSON object/map key types. This is
	 * necessary since JSON maps only supports strings as keys, while MGen
	 * supports both numeric values and strings.
	 * 
	 * @param keyString
	 *            The key string that was read from json
	 * 
	 * @param constraint
	 *            The type metadata describing what type to convert this string
	 *            to
	 * 
	 * @return The converted key
	 */
	private Object cvtMapKeyType(String keyString, Type constraint) {

		if (keyString == null)
			return null;

		switch (constraint.typeEnum()) {
		case ENUM:
			return ((RuntimeEnumType) constraint).get(keyString);
		case BOOL:
			return java.lang.Boolean.valueOf(keyString);
		case INT8:
			return java.lang.Byte.valueOf(keyString);
		case INT16:
			return java.lang.Short.valueOf(keyString);
		case INT32:
			return java.lang.Integer.valueOf(keyString);
		case INT64:
			return java.lang.Long.valueOf(keyString);
		case FLOAT32:
			return java.lang.Float.valueOf(keyString);
		case FLOAT64:
			return java.lang.Double.valueOf(keyString);
		case STRING:
			return keyString;
		default:
			throw new UnknownTypeException("Unknown map key type: " + constraint);
		}
	}

	/**
	 * Internal method for reading an array
	 * 
	 * @param constraint
	 *            Type metadata describing the array
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @return The read array
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private Object readArray(final ArrayType constraint, final JSONArray node) throws IOException {

		if (node == null)
			return null;

		switch (constraint.elementType().typeEnum()) {
		case ENUM:
			return readEnumArray(node, constraint);
		case BOOL:
			return readBoolArray(node);
		case INT8:
			return readInt8Array(node);
		case INT16:
			return readInt16Array(node);
		case INT32:
			return readInt32Array(node);
		case INT64:
			return readInt64Array(node);
		case FLOAT32:
			return readFloat32Array(node);
		case FLOAT64:
			return readFloat64Array(node);
		case STRING:
			return readStringArray(node);
		case ARRAY:
		case LIST:
		case MAP:
		case CLASS:
			return readObjectArray(node, constraint);
		default:
			throw new UnknownTypeException("Unknown array elementType: " + constraint.elementType());
		}
	}

	/**
	 * Internal method for reading a list
	 * 
	 * @param constraint
	 *            Type meta data describing what to read
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @return The read value/object
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private ArrayList<?> readList(final ListType constraint, final JSONArray node)
			throws IOException {

		if (node == null)
			return null;

		switch (constraint.elementType().typeEnum()) {
		case ENUM:
			return readEnumList(node, constraint);
		case BOOL:
			return readBoolList(node);
		case INT8:
			return readInt8List(node);
		case INT16:
			return readInt16List(node);
		case INT32:
			return readInt32List(node);
		case INT64:
			return readInt64List(node);
		case FLOAT32:
			return readFloat32List(node);
		case FLOAT64:
			return readFloat64List(node);
		case STRING:
			return readStringList(node);
		case ARRAY:
		case LIST:
		case MAP:
		case CLASS:
			return readObjectList(node, constraint);
		default:
			throw new UnknownTypeException("Unknown array element type: "
					+ constraint.elementType());
		}
	}

	/**
	 * Internal method for reading an enum array
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @param constraint
	 *            Type meta data describing what to read
	 * 
	 * @return The read array
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private Object readEnumArray(final JSONArray node, final ArrayType constraint) {
		final RuntimeEnumType elementType = (RuntimeEnumType) constraint.elementType();
		final Enum<?>[] out = (Enum<?>[]) constraint.newInstance(node.size());
		for (int i = 0; i < node.size(); i++)
			out[i] = cvtString2Enum(elementType, (String) node.get(i));
		return out;
	}

	/**
	 * Internal method for reading a boolean array
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @param constraint
	 *            Type meta data describing what to read
	 * 
	 * @return The read array
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private boolean[] readBoolArray(JSONArray node) throws IOException {
		final boolean[] out = new boolean[node.size()];
		for (int i = 0; i < node.size(); i++)
			out[i] = (Boolean) node.get(i);
		return out;
	}

	/**
	 * Internal method for reading an int8 array
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @param constraint
	 *            Type meta data describing what to read
	 * 
	 * @return The read array
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private byte[] readInt8Array(JSONArray node) throws IOException {
		final byte[] out = new byte[node.size()];
		for (int i = 0; i < node.size(); i++)
			out[i] = ((Number) node.get(i)).byteValue();
		return out;
	}

	/**
	 * Internal method for reading an int16 array
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @param constraint
	 *            Type meta data describing what to read
	 * 
	 * @return The read array
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private short[] readInt16Array(JSONArray node) throws IOException {
		final short[] out = new short[node.size()];
		for (int i = 0; i < node.size(); i++)
			out[i] = ((Number) node.get(i)).shortValue();
		return out;
	}

	/**
	 * Internal method for reading an int32 array
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @param constraint
	 *            Type meta data describing what to read
	 * 
	 * @return The read array
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private int[] readInt32Array(JSONArray node) throws IOException {
		final int[] out = new int[node.size()];
		for (int i = 0; i < node.size(); i++)
			out[i] = ((Number) node.get(i)).intValue();
		return out;
	}

	/**
	 * Internal method for reading an int64 array
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @param constraint
	 *            Type meta data describing what to read
	 * 
	 * @return The read array
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private long[] readInt64Array(JSONArray node) throws IOException {
		final long[] out = new long[node.size()];
		for (int i = 0; i < node.size(); i++)
			out[i] = ((Number) node.get(i)).longValue();
		return out;
	}

	/**
	 * Internal method for reading a float32 array
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @param constraint
	 *            Type meta data describing what to read
	 * 
	 * @return The read array
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private float[] readFloat32Array(JSONArray node) throws IOException {
		final float[] out = new float[node.size()];
		for (int i = 0; i < node.size(); i++)
			out[i] = ((Number) node.get(i)).floatValue();
		return out;
	}

	/**
	 * Internal method for reading a float64 array
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @param constraint
	 *            Type meta data describing what to read
	 * 
	 * @return The read array
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private double[] readFloat64Array(JSONArray node) throws IOException {
		final double[] out = new double[node.size()];
		for (int i = 0; i < node.size(); i++)
			out[i] = ((Number) node.get(i)).doubleValue();
		return out;
	}

	/**
	 * Internal method for reading a string array
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @param constraint
	 *            Type meta data describing what to read
	 * 
	 * @return The read array
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private String[] readStringArray(JSONArray node) throws IOException {
		final String[] out = new String[node.size()];
		for (int i = 0; i < node.size(); i++)
			out[i] = (String) node.get(i);
		return out;
	}

	/**
	 * Internal method for reading a generic object array
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @param constraint
	 *            Type meta data describing what to read
	 * 
	 * @return The read array
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private Object readObjectArray(JSONArray node, ArrayType typ) throws IOException {
		final Object out = typ.newInstance(node.size());
		for (int i = 0; i < node.size(); i++)
			Array.set(out, i, readObject(node.get(i), typ.elementType()));
		return out;
	}

	/**
	 * Internal method for reading an enum list
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @param constraint
	 *            Type meta data describing what to read
	 * 
	 * @return The read array
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private ArrayList<Enum<?>> readEnumList(JSONArray node, ListType typ) {
		final RuntimeEnumType elementType = (RuntimeEnumType) typ.elementType();
		final ArrayList<Enum<?>> out = new ArrayList<Enum<?>>(node.size());
		for (int i = 0; i < node.size(); i++)
			out.add(cvtString2Enum(elementType, (String) node.get(i)));
		return out;
	}

	/**
	 * Internal method for reading a boolean list
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @param constraint
	 *            Type meta data describing what to read
	 * 
	 * @return The read array
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private ArrayList<?> readBoolList(JSONArray node) throws IOException {
		final ArrayList<Boolean> out = new ArrayList<Boolean>(node.size());
		for (int i = 0; i < node.size(); i++) {
			out.add((Boolean) node.get(i));
		}
		return out;
	}

	/**
	 * Internal method for reading an int8 list
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @param constraint
	 *            Type meta data describing what to read
	 * 
	 * @return The read array
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private ArrayList<?> readInt8List(JSONArray node) throws IOException {
		final ArrayList<Byte> out = new ArrayList<Byte>(node.size());
		for (int i = 0; i < node.size(); i++) {
			final Object o = node.get(i);
			out.add(o == null ? null : ((Number) o).byteValue());
		}
		return out;
	}

	/**
	 * Internal method for reading an int16 list
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @param constraint
	 *            Type meta data describing what to read
	 * 
	 * @return The read array
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private ArrayList<?> readInt16List(JSONArray node) throws IOException {
		final ArrayList<Short> out = new ArrayList<Short>(node.size());
		for (int i = 0; i < node.size(); i++) {
			final Object o = node.get(i);
			out.add(o == null ? null : ((Number) o).shortValue());
		}
		return out;
	}

	/**
	 * Internal method for reading an int32 list
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @param constraint
	 *            Type meta data describing what to read
	 * 
	 * @return The read array
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private ArrayList<?> readInt32List(JSONArray node) throws IOException {
		final ArrayList<Integer> out = new ArrayList<Integer>(node.size());
		for (int i = 0; i < node.size(); i++) {
			final Object o = node.get(i);
			out.add(o == null ? null : ((Number) o).intValue());
		}
		return out;
	}

	/**
	 * Internal method for reading an int64 list
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @param constraint
	 *            Type meta data describing what to read
	 * 
	 * @return The read array
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private ArrayList<?> readInt64List(JSONArray node) throws IOException {
		final ArrayList<Long> out = new ArrayList<Long>(node.size());
		for (int i = 0; i < node.size(); i++) {
			final Object o = node.get(i);
			out.add(o == null ? null : ((Number) o).longValue());
		}
		return out;
	}

	/**
	 * Internal method for reading a float32 list
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @param constraint
	 *            Type meta data describing what to read
	 * 
	 * @return The read array
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private ArrayList<?> readFloat32List(JSONArray node) throws IOException {
		final ArrayList<Float> out = new ArrayList<Float>(node.size());
		for (int i = 0; i < node.size(); i++) {
			final Object o = node.get(i);
			out.add(o == null ? null : ((Number) o).floatValue());
		}
		return out;
	}

	/**
	 * Internal method for reading a float64 list
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @param constraint
	 *            Type meta data describing what to read
	 * 
	 * @return The read array
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private ArrayList<?> readFloat64List(JSONArray node) throws IOException {
		final ArrayList<Double> out = new ArrayList<Double>(node.size());
		for (int i = 0; i < node.size(); i++) {
			final Object o = node.get(i);
			out.add(o == null ? null : ((Number) o).doubleValue());
		}
		return out;
	}

	/**
	 * Internal method for reading a string list
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @param constraint
	 *            Type meta data describing what to read
	 * 
	 * @return The read array
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private ArrayList<?> readStringList(JSONArray node) throws IOException {
		final ArrayList<String> out = new ArrayList<String>(node.size());
		for (int i = 0; i < node.size(); i++) {
			final Object o = node.get(i);
			out.add(o == null ? null : (String) o);
		}
		return out;
	}

	/**
	 * Internal method for reading a generic object list
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @param constraint
	 *            Type meta data describing what to read
	 * 
	 * @return The read array
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private ArrayList<?> readObjectList(JSONArray node, ListType typ) throws IOException {
		final ArrayList<Object> out = new ArrayList<Object>(node.size());
		for (int i = 0; i < node.size(); i++) {
			final Object o = node.get(i);
			out.add(o == null ? null : readObject(o, typ.elementType()));
		}
		return out;
	}

	/**
	 * Internal method for converting a string to an enum value
	 * 
	 * @param typ
	 *            Type meta data describing what to read
	 * 
	 * @param writtenName
	 *            The read back string
	 * 
	 * @return The enum value
	 */
	private Enum<?> cvtString2Enum(RuntimeEnumType typ, String writtenName) {
		if (writtenName == null)
			return null;
		return typ.get(writtenName);
	}

	/**
	 * Internal method for reading a generic object
	 * 
	 * @param node
	 *            The current JSON node
	 * 
	 * @param typ
	 *            Type meta data describing what to read
	 * 
	 * @return The object/value read
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private Object readObject(Object node, Type typ) throws IOException {

		if (node == null)
			return null;

		switch (typ.typeEnum()) {
		case ENUM:
			return cvtString2Enum((RuntimeEnumType) typ, (String) node);
		case BOOL:
			return (Boolean) node;
		case INT8:
			return ((Number) node).byteValue();
		case INT16:
			return ((Number) node).shortValue();
		case INT32:
			return ((Number) node).intValue();
		case INT64:
			return ((Number) node).longValue();
		case FLOAT32:
			return ((Number) node).floatValue();
		case FLOAT64:
			return ((Number) node).doubleValue();
		case STRING:
			return (String) node;
		case ARRAY:
			return readArray((ArrayType) typ, (JSONArray) node);
		case LIST:
			return readList((ListType) typ, (JSONArray) node);
		case MAP:
			return readMap((MapType) typ, (JSONObject) node);
		case CLASS:
			return readMGenObject((JSONObject) node, (RuntimeClassType) typ);
		default:
			throw new UnknownTypeException("Unknown type: " + typ);
		}
	}

	/**
	 * Internal helper method for instantiating MGen objects being read back.
	 * 
	 * @param ids
	 *            The type ids in 16 bit base64 form of the class of the object
	 *            to be instantiated
	 * 
	 * @param constraint
	 *            Optional type constraint for the type to be instantiated.
	 *            Throws UnexpectedTypeException if the read back type doesn't
	 *            match the constraint.
	 * 
	 * @return The instantiated MGen object
	 */
	private MGenBase instantiate(final String[] ids, final RuntimeClassType constraint) {

		final ClassRegistryEntry entry = ids != null ? m_clsReg.getByTypeIds16BitBase64(ids)
				: m_clsReg.getById(constraint.typeId());

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
	 * Internal helper method for parsing json without exposing the checked
	 * exceptions of the underlying JSON parser implementation (which may be
	 * replaced at a later point).
	 * 
	 * @return The JSON node parsed
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	private JSONObject parseRootObject() throws IOException {
		try {
			return (JSONObject) m_parser.parseNext();
		} catch (final ParseException e) {
			throw new StreamCorruptedException(e);
		}
	}

}
