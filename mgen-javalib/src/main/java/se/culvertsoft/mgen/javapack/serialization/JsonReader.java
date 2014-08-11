package se.culvertsoft.mgen.javapack.serialization;

import static se.culvertsoft.mgen.javapack.serialization.BuiltInSerializerUtils.ensureNoMissingReqFields;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

public class JsonReader extends BuiltInReader {

	private final MGenJSONParser m_parser;

	public JsonReader(final InputStream stream, final ClassRegistryBase classRegistry) {
		super(classRegistry);
		m_parser = new MGenJSONParser(new InputStreamReader(stream, CHARSET));
	}

	public JsonReader setInput(final InputStream stream) {
		m_parser.setInput(new InputStreamReader(stream, CHARSET));
		return this;
	}

	@Override
	public MGenBase readObject() throws IOException {
		return readMGenObject(parseRootObject(), null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends MGenBase> T readObject(final Class<T> typ) throws IOException {

		final ClassRegistryEntry entry = m_clsReg.getByClass(typ);

		if (entry == null)
			throw new UnknownTypeException("Could not read object of type " + typ
					+ ", since it is know known by the class registry");

		return (T) readMGenObject(parseRootObject(), entry.typ());
	}

	@Override
	public boolean readBooleanField(final Field field, final Object context) throws IOException {
		return (Boolean) (((JSONObject) context).get(field.name()));
	}

	@Override
	public byte readInt8Field(final Field field, final Object context) throws IOException {
		return ((Number) (((JSONObject) context).get(field.name()))).byteValue();
	}

	@Override
	public short readInt16Field(final Field field, final Object context) throws IOException {
		return ((Number) (((JSONObject) context).get(field.name()))).shortValue();
	}

	@Override
	public int readInt32Field(final Field field, final Object context) throws IOException {
		return ((Number) (((JSONObject) context).get(field.name()))).intValue();
	}

	@Override
	public long readInt64Field(final Field field, final Object context) throws IOException {
		return ((Number) (((JSONObject) context).get(field.name()))).longValue();
	}

	@Override
	public float readFloat32Field(final Field field, final Object context) throws IOException {
		return ((Number) (((JSONObject) context).get(field.name()))).floatValue();
	}

	@Override
	public double readFloat64Field(final Field field, final Object context) throws IOException {
		return ((Number) (((JSONObject) context).get(field.name()))).doubleValue();
	}

	@Override
	public String readStringField(final Field field, final Object context) throws IOException {
		return (String) (((JSONObject) context).get(field.name()));
	}

	@Override
	public Object readArrayField(final Field field, final Object context) throws IOException {
		return readArray((ArrayType) field.typ(), getJsonArr(field, context));
	}

	@Override
	public ArrayList<?> readListField(final Field field, final Object context) throws IOException {
		return readList((ListType) field.typ(), getJsonArr(field, context));
	}

	@Override
	public HashMap<?, ?> readMapField(final Field field, final Object context) throws IOException {
		return readMap((MapType) field.typ(), getJsonObj(field, context));
	}

	@Override
	public final MGenBase readMgenObjectField(final Field field, final Object context)
			throws IOException {
		return readMGenObject(getJsonObj(field, context), (RuntimeClassType) field.typ());
	}

	@Override
	public Enum<?> readEnumField(final Field field, final Object context) throws IOException {
		final String stringVal = (String) (((JSONObject) context).get(field.name()));
		return readEnum((RuntimeEnumType) field.typ(), stringVal);
	}

	@Override
	public void handleUnknownField(final Field field, final Object context) throws IOException {
	}

	/************************************************
	 * 
	 * 
	 * PRIVATE METHODS
	 * 
	 ***********************************************/

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

	private JSONObject getJsonObj(final Field field, final Object context) {
		return (JSONObject) ((JSONObject) context).get(field.name());
	}

	private JSONArray getJsonArr(final Field field, final Object context) {
		return (JSONArray) ((JSONObject) context).get(field.name());
	}

	private void readObjectFields(final MGenBase object, final JSONObject node) throws IOException {
		for (final Object keyO : node.keySet()) {
			final String name = (String) keyO;
			final Field field = object._fieldByName(name);
			if (field != null) {
				object._readField(field.id(), node, this);
			}
		}

	}

	private HashMap<?, ?> readMap(MapType typ, JSONObject node) throws IOException {

		if (node == null)
			return null;

		final HashMap<Object, Object> out = new HashMap<Object, Object>(node.size());

		for (final Object keyNode : node.keySet()) {
			final Object key = cvtString((String) keyNode, typ.keyType());
			final Object value = readObject(node.get(keyNode), typ.valueType());
			out.put(key, value);
		}

		return out;
	}

	private Object cvtString(String keyString, Type constraint) {

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

	private Object readEnumArray(final JSONArray node, final ArrayType constraint) {
		final RuntimeEnumType elementType = (RuntimeEnumType) constraint.elementType();
		final Enum<?>[] out = (Enum<?>[]) constraint.newInstance(node.size());
		for (int i = 0; i < node.size(); i++)
			out[i] = readEnum(elementType, (String) node.get(i));
		return out;
	}

	private boolean[] readBoolArray(JSONArray node) throws IOException {
		final boolean[] out = new boolean[node.size()];
		for (int i = 0; i < node.size(); i++)
			out[i] = (Boolean) node.get(i);
		return out;
	}

	private byte[] readInt8Array(JSONArray node) throws IOException {
		final byte[] out = new byte[node.size()];
		for (int i = 0; i < node.size(); i++)
			out[i] = ((Number) node.get(i)).byteValue();
		return out;
	}

	private short[] readInt16Array(JSONArray node) throws IOException {
		final short[] out = new short[node.size()];
		for (int i = 0; i < node.size(); i++)
			out[i] = ((Number) node.get(i)).shortValue();
		return out;
	}

	private int[] readInt32Array(JSONArray node) throws IOException {
		final int[] out = new int[node.size()];
		for (int i = 0; i < node.size(); i++)
			out[i] = ((Number) node.get(i)).intValue();
		return out;
	}

	private long[] readInt64Array(JSONArray node) throws IOException {
		final long[] out = new long[node.size()];
		for (int i = 0; i < node.size(); i++)
			out[i] = ((Number) node.get(i)).longValue();
		return out;
	}

	private float[] readFloat32Array(JSONArray node) throws IOException {
		final float[] out = new float[node.size()];
		for (int i = 0; i < node.size(); i++)
			out[i] = ((Number) node.get(i)).floatValue();
		return out;
	}

	private double[] readFloat64Array(JSONArray node) throws IOException {
		final double[] out = new double[node.size()];
		for (int i = 0; i < node.size(); i++)
			out[i] = ((Number) node.get(i)).doubleValue();
		return out;
	}

	private String[] readStringArray(JSONArray node) throws IOException {
		final String[] out = new String[node.size()];
		for (int i = 0; i < node.size(); i++)
			out[i] = (String) node.get(i);
		return out;
	}

	private Object readObjectArray(JSONArray node, ArrayType typ) throws IOException {
		final Object out = typ.newInstance(node.size());
		for (int i = 0; i < node.size(); i++)
			Array.set(out, i, readObject(node.get(i), typ.elementType()));
		return out;
	}

	private ArrayList<Enum<?>> readEnumList(JSONArray node, ListType typ) {
		final RuntimeEnumType elementType = (RuntimeEnumType) typ.elementType();
		final ArrayList<Enum<?>> out = new ArrayList<Enum<?>>(node.size());
		for (int i = 0; i < node.size(); i++)
			out.add(readEnum(elementType, (String) node.get(i)));
		return out;
	}

	private ArrayList<?> readBoolList(JSONArray node) throws IOException {
		final ArrayList<Boolean> out = new ArrayList<Boolean>(node.size());
		for (int i = 0; i < node.size(); i++) {
			out.add((Boolean) node.get(i));
		}
		return out;
	}

	private ArrayList<?> readInt8List(JSONArray node) throws IOException {
		final ArrayList<Byte> out = new ArrayList<Byte>(node.size());
		for (int i = 0; i < node.size(); i++) {
			final Object o = node.get(i);
			out.add(o == null ? null : ((Number) o).byteValue());
		}
		return out;
	}

	private ArrayList<?> readInt16List(JSONArray node) throws IOException {
		final ArrayList<Short> out = new ArrayList<Short>(node.size());
		for (int i = 0; i < node.size(); i++) {
			final Object o = node.get(i);
			out.add(o == null ? null : ((Number) o).shortValue());
		}
		return out;
	}

	private ArrayList<?> readInt32List(JSONArray node) throws IOException {
		final ArrayList<Integer> out = new ArrayList<Integer>(node.size());
		for (int i = 0; i < node.size(); i++) {
			final Object o = node.get(i);
			out.add(o == null ? null : ((Number) o).intValue());
		}
		return out;
	}

	private ArrayList<?> readInt64List(JSONArray node) throws IOException {
		final ArrayList<Long> out = new ArrayList<Long>(node.size());
		for (int i = 0; i < node.size(); i++) {
			final Object o = node.get(i);
			out.add(o == null ? null : ((Number) o).longValue());
		}
		return out;
	}

	private ArrayList<?> readFloat32List(JSONArray node) throws IOException {
		final ArrayList<Float> out = new ArrayList<Float>(node.size());
		for (int i = 0; i < node.size(); i++) {
			final Object o = node.get(i);
			out.add(o == null ? null : ((Number) o).floatValue());
		}
		return out;
	}

	private ArrayList<?> readFloat64List(JSONArray node) throws IOException {
		final ArrayList<Double> out = new ArrayList<Double>(node.size());
		for (int i = 0; i < node.size(); i++) {
			final Object o = node.get(i);
			out.add(o == null ? null : ((Number) o).doubleValue());
		}
		return out;
	}

	private ArrayList<?> readStringList(JSONArray node) throws IOException {
		final ArrayList<String> out = new ArrayList<String>(node.size());
		for (int i = 0; i < node.size(); i++) {
			final Object o = node.get(i);
			out.add(o == null ? null : (String) o);
		}
		return out;
	}

	private ArrayList<?> readObjectList(JSONArray node, ListType typ) throws IOException {
		final ArrayList<Object> out = new ArrayList<Object>(node.size());
		for (int i = 0; i < node.size(); i++) {
			final Object o = node.get(i);
			out.add(o == null ? null : readObject(o, typ.elementType()));
		}
		return out;
	}

	private Enum<?> readEnum(RuntimeEnumType typ, String writtenName) {
		if (writtenName == null)
			return null;
		return typ.get(writtenName);
	}

	private Object readObject(Object node, Type typ) throws IOException {

		if (node == null)
			return null;

		switch (typ.typeEnum()) {
		case ENUM:
			return readEnum((RuntimeEnumType) typ, (String) node);
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

	private JSONObject parseRootObject() throws IOException {
		try {
			return (JSONObject) m_parser.parseNext();
		} catch (final ParseException e) {
			throw new StreamCorruptedException(e);
		}
	}

}
