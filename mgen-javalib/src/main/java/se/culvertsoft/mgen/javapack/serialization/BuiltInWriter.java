package se.culvertsoft.mgen.javapack.serialization;

import static se.culvertsoft.mgen.javapack.serialization.BuiltInSerializerUtils.ensureNoMissingReqFields;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.javapack.classes.ClassRegistryBase;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.exceptions.UnknownTypeException;
import se.culvertsoft.mgen.javapack.util.StringEncoder;

public abstract class BuiltInWriter implements FieldVisitor {

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

	public abstract void writeObject(final MGenBase object) throws IOException;

	public abstract void beginWrite(final MGenBase object, final int nFields) throws IOException;

	public abstract void finishWrite() throws IOException;

	public abstract void writeBooleanField(final boolean b, final Field field) throws IOException;

	public abstract void writeInt8Field(final byte b, final Field field) throws IOException;

	public abstract void writeInt16Field(final short s, final Field field) throws IOException;

	public abstract void writeInt32Field(final int i, final Field field) throws IOException;

	public abstract void writeInt64Field(final long l, final Field field) throws IOException;

	public abstract void writeFloat32Field(final float f, final Field field) throws IOException;

	public abstract void writeFloat64Field(final double d, final Field field) throws IOException;

	public abstract void writeStringField(final String s, final Field field) throws IOException;

	public abstract void writeEnumField(final Enum<?> e, final Field field) throws IOException;

	public abstract void writeListField(final ArrayList<Object> list, final Field field)
			throws IOException;

	public abstract void writeMapField(final HashMap<Object, Object> map, final Field field)
			throws IOException;

	public abstract void writeArrayField(final Object array, final Field field) throws IOException;

	public abstract void writeMGenObjectField(final MGenBase o, final Field field)
			throws IOException;

	@Override
	public void visit(final boolean b, final Field field) throws IOException {
		writeBooleanField(b, field);
	}

	@Override
	public void visit(final byte o, final Field field) throws IOException {
		writeInt8Field(o, field);
	}

	@Override
	public void visit(final short o, final Field field) throws IOException {
		writeInt16Field(o, field);
	}

	@Override
	public void visit(final int o, final Field field) throws IOException {
		writeInt32Field(o, field);
	}

	@Override
	public void visit(final long o, final Field field) throws IOException {
		writeInt64Field(o, field);
	}

	@Override
	public void visit(final float o, final Field field) throws IOException {
		writeFloat32Field(o, field);
	}

	@Override
	public void visit(final double o, final Field field) throws IOException {
		writeFloat64Field(o, field);
	}

	@Override
	public void visit(final String o, final Field field) throws IOException {
		writeStringField(o, field);
	}

	@Override
	public void visit(final Enum<?> e, final Field field) throws IOException {
		writeEnumField(e, field);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void visit(final Object o, final Field field) throws IOException {
		switch (field.typ().typeEnum()) {
		case ARRAY:
			writeArrayField(o, field);
			break;
		case LIST:
			writeListField((ArrayList<Object>) o, field);
			break;
		case MAP:
			writeMapField((HashMap<Object, Object>) o, field);
			break;
		case UNKNOWN:
		case CUSTOM:
			writeMGenObjectField((MGenBase) o, field);
			break;
		default:
			throw new UnknownTypeException("Don't know how to write " + o + "("
					+ field.typ().typeEnum() + ") of field " + field);
		}
	}

	@Override
	public void beginVisit(final MGenBase object, final int nFields) throws IOException {
		if (m_shouldValidate)
			ensureNoMissingReqFields(object);
		beginWrite(object, nFields);
	}

	@Override
	public void endVisit() throws IOException {
		finishWrite();
	}

	public void setShouldValidate(final boolean shouldValidate) {
		m_shouldValidate = shouldValidate;
	}

}
