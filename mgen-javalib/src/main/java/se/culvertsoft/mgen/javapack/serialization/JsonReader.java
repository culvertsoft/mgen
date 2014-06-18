package se.culvertsoft.mgen.javapack.serialization;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import se.culvertsoft.mgen.api.exceptions.SerializationException;
import se.culvertsoft.mgen.api.model.ArrayType;
import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.api.model.ListType;
import se.culvertsoft.mgen.api.model.MapType;
import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.api.model.UnknownCustomType;
import se.culvertsoft.mgen.javapack.classes.ClassRegistry;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.metadata.FieldSetDepth;
import se.culvertsoft.mgen.javapack.serialization.SerializationSettings.TypeIdType;

public class JsonReader extends BuiltInReader {

	private final DataInputStream stream;
	private final ReaderSettings readerSettings;
	@SuppressWarnings("unused")
	private final ReadErrorListener errorHandler;

	public JsonReader(final InputStream stream,
			final ClassRegistry classRegistry,
			final ReaderSettings readerSettings,
			final ReadErrorListener errorHandler) {
		super(classRegistry);
		this.stream = stream instanceof DataInputStream ? (DataInputStream) stream
				: new DataInputStream(stream);
		this.readerSettings = readerSettings;
		this.errorHandler = errorHandler;
	}

	public JsonReader(final InputStream stream,
			final ClassRegistry classRegistry) {
		this(stream, classRegistry, ReaderSettings.DEFAULT,
				new ReadErrorAdapter());
	}

	@Override
	public MGenBase readMGenObject() throws IOException {
		try {
			final JSONParser parser = new JSONParser();
			final Object parsed = parser.parse(new InputStreamReader(stream));
			return readMGenObject((JSONObject) parsed);
		} catch (final Throwable e) {
			throw new SerializationException(e);
		}
	}

	protected MGenBase readMGenObject(final JSONObject node) throws IOException {

		if (node == null)
			return null;

		final JSONArray typeNames = (JSONArray) node.get("__t");
		throwMissingFieldIfNull(typeNames, "__t");

		final MGenBase out = instantiateFromGlobalIds(typeNames);

		if (out != null) {
			readObjectFields(out, node);
			validateThrow(out);
		}

		return out;
	}

	private JSONObject getJsonObj(final Field field, final Object context) {
		return (JSONObject) ((JSONObject) context).get(field.name());
	}

	private JSONArray getJsonArr(final Field field, final Object context) {
		return (JSONArray) ((JSONObject) context).get(field.name());
	}

	@Override
	public boolean readBooleanField(final Field field, final Object context)
			throws IOException {
		return (Boolean) (((JSONObject) context).get(field.name()));
	}

	@Override
	public byte readInt8Field(final Field field, final Object context)
			throws IOException {
		return ((Long) (((JSONObject) context).get(field.name()))).byteValue();
	}

	@Override
	public short readInt16Field(final Field field, final Object context)
			throws IOException {
		return ((Long) (((JSONObject) context).get(field.name()))).shortValue();
	}

	@Override
	public int readInt32Field(final Field field, final Object context)
			throws IOException {
		return ((Long) (((JSONObject) context).get(field.name()))).intValue();
	}

	@Override
	public long readInt64Field(final Field field, final Object context)
			throws IOException {
		return ((Long) (((JSONObject) context).get(field.name()))).longValue();
	}

	@Override
	public float readFloat32Field(final Field field, final Object context)
			throws IOException {
		return ((Double) (((JSONObject) context).get(field.name())))
				.floatValue();
	}

	@Override
	public double readFloat64Field(final Field field, final Object context)
			throws IOException {
		return ((Double) (((JSONObject) context).get(field.name())))
				.doubleValue();
	}

	@Override
	public String readStringField(final Field field, final Object context)
			throws IOException {
		return (String) (((JSONObject) context).get(field.name()));
	}

	@Override
	public Object readArrayField(final Field field, final Object context)
			throws IOException {
		return readArray((ArrayType) field.typ(), getJsonArr(field, context));
	}

	@Override
	public ArrayList<?> readListField(final Field field, final Object context)
			throws IOException {
		return readList((ListType) field.typ(), getJsonArr(field, context));
	}

	@Override
	public HashMap<?, ?> readMapField(final Field field, final Object context)
			throws IOException {
		return readMap((MapType) field.typ(), getJsonObj(field, context));
	}

	@Override
	public final MGenBase readMgenObjectField(final Field field,
			final Object context) throws IOException {
		return readMGenObject(getJsonObj(field, context),
				(UnknownCustomType) field.typ());
	}

	@Override
	public void handleUnknownField(final Field field, final Object context)
			throws IOException {
		// Just skip it
	}

	private void readObjectFields(final MGenBase object, final JSONObject node)
			throws IOException {

		final Object context = node;

		for (final Object keyO : node.keySet()) {
			final String key = (String) keyO;
			final Field field = object._fieldByName(key);
			if (field != null) {
				object._readField(field, context, this);
			}
		}

	}

	private MGenBase readMGenObject(final JSONObject node,
			final UnknownCustomType constraint) throws IOException {

		final MGenBase out = readMGenObject(node);

		if (out != null && constraint != null) {
			if (!out.isInstanceOfLocalId(constraint.localTypeId())) {
				// TODO: Handle constraints failure
				return null;
			}
		}

		return out;
	}

	protected MGenBase instantiateFromGlobalIds(final JSONArray array)
			throws IOException {
		@SuppressWarnings("unchecked")
		final String[] ids = ((ArrayList<String>) array).toArray(new String[array.size()]);
		if (readerSettings.typeIdType() == TypeIdType.HASH_16_BIT) {
			return m_classRegistry.instantiateFromHash16Base64Ids(ids);
		} else {
			return m_classRegistry.instantiateFromNames(ids);
		}
	}

	private HashMap<?, ?> readMap(MapType typ, JSONObject node)
			throws IOException {

		if (node == null)
			return null;

		final HashMap<Object, Object> out = new HashMap<Object, Object>();

		for (final Object keyNode : node.keySet()) {
			final Object valueNode = node.get(keyNode);
			final Object key = readObject(keyNode, typ.keyType());
			final Object value = readObject(valueNode, typ.valueType());
			out.put(key, value);
		}

		return out;
	}

	private Object readArray(final ArrayType typ, final JSONArray node)
			throws IOException {

		if (node == null)
			return null;

		switch (typ.elementType().typeEnum()) {
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
		case MGEN_BASE:
		case CUSTOM:
		case UNKNOWN:
			return readObjectArray(node, typ);
		default:
			throw new SerializationException(
					"MgenJsonReader.readArray: Unknown type: "
							+ typ.elementType());
		}
	}

	private ArrayList<?> readList(final ListType typ, final JSONArray node)
			throws IOException {

		if (node == null)
			return null;

		switch (typ.elementType().typeEnum()) {
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
		case MGEN_BASE:
		case CUSTOM:
		case UNKNOWN:
			return readObjectList(node, typ);
		default:
			throw new SerializationException(
					"MgenJsonReader.readList: Unknown type: "
							+ typ.elementType());
		}
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
			out[i] = ((Long) node.get(i)).byteValue();
		return out;
	}

	private short[] readInt16Array(JSONArray node) throws IOException {
		final short[] out = new short[node.size()];
		for (int i = 0; i < node.size(); i++)
			out[i] = ((Long) node.get(i)).shortValue();
		return out;
	}

	private int[] readInt32Array(JSONArray node) throws IOException {
		final int[] out = new int[node.size()];
		for (int i = 0; i < node.size(); i++)
			out[i] = ((Long) node.get(i)).intValue();
		return out;
	}

	private long[] readInt64Array(JSONArray node) throws IOException {
		final long[] out = new long[node.size()];
		for (int i = 0; i < node.size(); i++)
			out[i] = (Long) node.get(i);
		return out;
	}

	private float[] readFloat32Array(JSONArray node) throws IOException {
		final float[] out = new float[node.size()];
		for (int i = 0; i < node.size(); i++)
			out[i] = ((Double) node.get(i)).floatValue();
		return out;
	}

	private double[] readFloat64Array(JSONArray node) throws IOException {
		final double[] out = new double[node.size()];
		for (int i = 0; i < node.size(); i++)
			out[i] = (Double) node.get(i);
		return out;
	}

	private String[] readStringArray(JSONArray node) throws IOException {
		final String[] out = new String[node.size()];
		for (int i = 0; i < node.size(); i++)
			out[i] = (String) node.get(i);
		return out;
	}

	private Object readObjectArray(JSONArray node, ArrayType typ)
			throws IOException {
		final Object out = typ.newInstance(node.size());
		for (int i = 0; i < node.size(); i++)
			Array.set(out, i, readObject(node.get(i), typ.elementType()));
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
			out.add(o == null ? null : ((Long) o).byteValue());
		}
		return out;
	}

	private ArrayList<?> readInt16List(JSONArray node) throws IOException {
		final ArrayList<Short> out = new ArrayList<Short>(node.size());
		for (int i = 0; i < node.size(); i++) {
			final Object o = node.get(i);
			out.add(o == null ? null : ((Long) o).shortValue());
		}
		return out;
	}

	private ArrayList<?> readInt32List(JSONArray node) throws IOException {
		final ArrayList<Integer> out = new ArrayList<Integer>(node.size());
		for (int i = 0; i < node.size(); i++) {
			final Object o = node.get(i);
			out.add(o == null ? null : ((Long) o).intValue());
		}
		return out;
	}

	private ArrayList<?> readInt64List(JSONArray node) throws IOException {
		final ArrayList<Long> out = new ArrayList<Long>(node.size());
		for (int i = 0; i < node.size(); i++) {
			final Object o = node.get(i);
			out.add(o == null ? null : (Long) o);
		}
		return out;
	}

	private ArrayList<?> readFloat32List(JSONArray node) throws IOException {
		final ArrayList<Float> out = new ArrayList<Float>(node.size());
		for (int i = 0; i < node.size(); i++) {
			final Object o = node.get(i);
			out.add(o == null ? null : ((Double) o).floatValue());
		}
		return out;
	}

	private ArrayList<?> readFloat64List(JSONArray node) throws IOException {
		final ArrayList<Double> out = new ArrayList<Double>(node.size());
		for (int i = 0; i < node.size(); i++) {
			final Object o = node.get(i);
			out.add(o == null ? null : (Double) o);
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

	private ArrayList<?> readObjectList(JSONArray node, ListType typ)
			throws IOException {
		final ArrayList<Object> out = new ArrayList<Object>(node.size());
		for (int i = 0; i < node.size(); i++) {
			final Object o = node.get(i);
			out.add(o == null ? null : readObject(o, typ.elementType()));
		}
		return out;
	}

	private Object readObject(Object node, Type typ) throws IOException {

		if (node == null)
			return null;

		switch (typ.typeEnum()) {
		case BOOL:
			return (Boolean) node;
		case INT8:
			return ((Long) node).byteValue();
		case INT16:
			return ((Long) node).shortValue();
		case INT32:
			return ((Long) node).intValue();
		case INT64:
			return ((Long) node).longValue();
		case FLOAT32:
			return ((Double) node).floatValue();
		case FLOAT64:
			return (Double) node;
		case STRING:
			return (String) node;
		case ARRAY:
			return readArray((ArrayType) typ, (JSONArray) node);
		case LIST:
			return readList((ListType) typ, (JSONArray) node);
		case MAP:
			return readMap((MapType) typ, (JSONObject) node);
		case MGEN_BASE:
		case CUSTOM:
		case UNKNOWN:
			return readMGenObject((JSONObject) node, (UnknownCustomType) typ);
		default:
			throw new SerializationException(
					"MgenJsonReader.readObject: Unknown type: " + typ);
		}
	}

	protected void throwMissingField(final String fieldName) {
		throw new SerializationException(
				"MGenJSonReader.readMGenObject: Missing field '" + fieldName
						+ "'");
	}

	protected void throwMissingFieldIfNull(final Object o,
			final String fieldName) {
		if (o == null) {
			throwMissingField(fieldName);
		}
	}

	protected void validateThrow(final MGenBase o) {
		if (o != null && !o._validate(FieldSetDepth.SHALLOW)) {
			throw new SerializationException("MGenReader.validate failed: A "
					+ o.getClass()
					+ " does not have all required fields set, missing: "
					+ o._missingRequiredFields());
		}
	}

}
