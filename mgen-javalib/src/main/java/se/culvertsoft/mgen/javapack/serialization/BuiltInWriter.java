package se.culvertsoft.mgen.javapack.serialization;

import static se.culvertsoft.mgen.javapack.util.BuiltInSerializerUtils.ensureNoMissingReqFields;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.javapack.classes.ClassRegistryBase;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.exceptions.UnknownTypeException;
import se.culvertsoft.mgen.javapack.util.StringEncoder;

/**
 * Base class for all built-in MGen wire format writers - not intended to be
 * used directly. See BinaryWriter, JsonWriter and JsonPrettyWriter instead.
 */
public abstract class BuiltInWriter implements FieldVisitor {

	public static final OutputStream EMPTY_OUTPUT_STREAM = new OutputStream() {
		public void write(int b) throws IOException {
			throw new IOException("Cannot write to empty stream");
		}
	};

	public static final int STRING_ENCODE_BUFFER_SIZE = 256;
	public static final Charset CHARSET = Charset.forName("UTF8");

	protected final StringEncoder m_stringEncoder;
	protected final ClassRegistryBase m_classRegistry;

	private boolean m_shouldValidate;

	public BuiltInWriter(final ClassRegistryBase classRegistry) {
		m_classRegistry = classRegistry;
		m_stringEncoder = new StringEncoder(STRING_ENCODE_BUFFER_SIZE, CHARSET);
		m_shouldValidate = true;
	}

	/**
	 * Writes an MGen object to the underlying output stream. Primary interface
	 * method for writing MGen objects to this writer. The class of the object
	 * must be registered in class registry provided to this BinaryWriter when
	 * it was constructed (this is always the case unless you have multiple data
	 * models in parallel).
	 * 
	 * @param object
	 *            The object to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	public abstract void writeObject(final MGenBase object) throws IOException;

	/**
	 * When this writer is visiting an object it should write, this method will
	 * be called before starting to visit any fields. The purpose is mainly to
	 * let this writer know how many fields will follow, so that this value can
	 * be written to the output stream.
	 * 
	 * @param object
	 *            The object being written
	 * 
	 * @param nFields
	 *            The number of selected fields for writing
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	public abstract void beginWrite(final MGenBase object, final int nFields) throws IOException;

	/**
	 * Method called when all selected fields of an object have been visited. In
	 * some writer implementations, it doesn't do anything, but writers for
	 * other wire formats may use this method.
	 */
	public abstract void finishWrite() throws IOException;

	/**
	 * Method for writing a boolean field
	 * 
	 * @param b
	 *            The value to write
	 * 
	 * @param field
	 *            The field to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	public abstract void writeBooleanField(final boolean b, final Field field) throws IOException;

	/**
	 * Method for writing an int8 field
	 * 
	 * @param b
	 *            The value to write
	 * 
	 * @param field
	 *            The field to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	public abstract void writeInt8Field(final byte b, final Field field) throws IOException;

	/**
	 * Method for writing an int16 field
	 * 
	 * @param s
	 *            The value to write
	 * 
	 * @param field
	 *            The field to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	public abstract void writeInt16Field(final short s, final Field field) throws IOException;

	/**
	 * Method for writing an int32 field
	 * 
	 * @param i
	 *            The value to write
	 * 
	 * @param field
	 *            The field to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	public abstract void writeInt32Field(final int i, final Field field) throws IOException;

	/**
	 * Method for writing an int64 field
	 * 
	 * @param l
	 *            The value to write
	 * 
	 * @param field
	 *            The field to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	public abstract void writeInt64Field(final long l, final Field field) throws IOException;

	/**
	 * Method for writing a float32 field
	 * 
	 * @param f
	 *            The value to write
	 * 
	 * @param field
	 *            The field to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	public abstract void writeFloat32Field(final float f, final Field field) throws IOException;

	/**
	 * Method for writing a float64 field
	 * 
	 * @param d
	 *            The value to write
	 * 
	 * @param field
	 *            The field to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	public abstract void writeFloat64Field(final double d, final Field field) throws IOException;

	/**
	 * Method for writing a string field
	 * 
	 * @param s
	 *            The value to write
	 * 
	 * @param field
	 *            The field to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	public abstract void writeStringField(final String s, final Field field) throws IOException;

	/**
	 * Method for writing an enum field
	 * 
	 * @param e
	 *            The enum value to write
	 * 
	 * @param field
	 *            The field to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	public abstract void writeEnumField(final Enum<?> e, final Field field) throws IOException;

	/**
	 * Method for writing a list field
	 * 
	 * @param list
	 *            The list to write
	 * 
	 * @param field
	 *            The field to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	public abstract void writeListField(final List<Object> list, final Field field)
			throws IOException;

	/**
	 * Method for writing a map field
	 * 
	 * @param map
	 *            The map to write
	 * 
	 * @param field
	 *            The field to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	public abstract void writeMapField(final Map<Object, Object> map, final Field field)
			throws IOException;

	/**
	 * Method for writing an array field
	 * 
	 * @param array
	 *            The array to write
	 * 
	 * @param field
	 *            The field to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	public abstract void writeArrayField(final Object array, final Field field) throws IOException;

	/**
	 * Method for writing an MGen object field
	 * 
	 * @param o
	 *            The object to write
	 * 
	 * @param field
	 *            The field to write
	 * 
	 * @throws IOException
	 *             If an IOException occurs when writing to the the underlying
	 *             output stream
	 */
	public abstract void writeMGenObjectField(final MGenBase o, final Field field)
			throws IOException;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(final boolean b, final Field field) throws IOException {
		writeBooleanField(b, field);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(final byte o, final Field field) throws IOException {
		writeInt8Field(o, field);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(final short o, final Field field) throws IOException {
		writeInt16Field(o, field);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(final int o, final Field field) throws IOException {
		writeInt32Field(o, field);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(final long o, final Field field) throws IOException {
		writeInt64Field(o, field);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(final float o, final Field field) throws IOException {
		writeFloat32Field(o, field);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(final double o, final Field field) throws IOException {
		writeFloat64Field(o, field);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(final String o, final Field field) throws IOException {
		writeStringField(o, field);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(final Enum<?> e, final Field field) throws IOException {
		writeEnumField(e, field);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void visit(final Object o, final Field field) throws IOException {
		switch (field.typ().typeEnum()) {
		case ARRAY:
			writeArrayField(o, field);
			break;
		case LIST:
			writeListField((List<Object>) o, field);
			break;
		case MAP:
			writeMapField((Map<Object, Object>) o, field);
			break;
		case CLASS:
			writeMGenObjectField((MGenBase) o, field);
			break;
		default:
			throw new UnknownTypeException("Don't know how to write " + o + "("
					+ field.typ().typeEnum() + ") of field " + field);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beginVisit(final MGenBase object, final int nFields) throws IOException {
		if (m_shouldValidate)
			ensureNoMissingReqFields(object);
		beginWrite(object, nFields);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endVisit() throws IOException {
		finishWrite();
	}

	/**
	 * Writers can be configured whether they should validate objects they write
	 * or not before writing them. The default setting is true.
	 * 
	 * @param shouldValidate
	 *            If the writer should validate objects before writing them.
	 */
	public void setShouldValidate(final boolean shouldValidate) {
		m_shouldValidate = shouldValidate;
	}

}
