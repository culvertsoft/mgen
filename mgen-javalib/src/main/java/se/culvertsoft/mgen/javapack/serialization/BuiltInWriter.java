package se.culvertsoft.mgen.javapack.serialization;

import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import se.culvertsoft.mgen.api.exceptions.SerializationException;
import se.culvertsoft.mgen.api.exceptions.UnknownTypeException;
import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.javapack.classes.ClassRegistry;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.metadata.FieldSetDepth;

public abstract class BuiltInWriter implements Writer {

	private final ClassRegistry m_classRegistry;
	protected final DataOutput stream;

	public BuiltInWriter(
			final DataOutput stream,
			final ClassRegistry classRegistry) {
		m_classRegistry = classRegistry;
		this.stream = stream;
	}

	public abstract void beginWrite(
			final MGenBase object,
			final int nFieldsSet, 
			final int nFieldsTotal) throws IOException;

	public abstract void writeBooleanField(
			final boolean b,
			final Field field,
			final boolean isSet) throws IOException;

	public abstract void writeInt8Field(
			final byte b,
			final Field field,
			final boolean isSet) throws IOException;

	public abstract void writeInt16Field(
			final short s,
			final Field field,
			final boolean isSet) throws IOException;

	public abstract void writeInt32Field(
			final int i,
			final Field field,
			final boolean isSet) throws IOException;

	public abstract void writeInt64Field(
			final long l,
			final Field field,
			final boolean isSet) throws IOException;

	public abstract void writeFloat32Field(
			final float f,
			final Field field,
			final boolean isSet) throws IOException;

	public abstract void writeFloat64Field(
			final double d,
			final Field field,
			final boolean isSet) throws IOException;

	public abstract void writeStringField(
			final String s,
			final Field field,
			final boolean isSet) throws IOException;

	public abstract void writeListField(
			final ArrayList<Object> list,
			final Field field,
			final boolean isSet) throws IOException;

	public abstract void writeMapField(
			final HashMap<Object, Object> map,
			final Field field,
			final boolean isSet) throws IOException;

	public abstract void writeArrayField(
			final Object array,
			final Field field,
			final boolean isSet) throws IOException;

	public abstract void writeMGenObjectField(
			final MGenBase o,
			final Field field,
			final boolean isSet) throws IOException;

	public abstract void finishWrite() throws IOException;

	@Override
	public void visit(final byte o, final Field field, final boolean isSet)
			throws IOException {
		writeInt8Field(o, field, isSet);
	}

	@Override
	public void visit(final short o, final Field field, final boolean isSet)
			throws IOException {
		writeInt16Field(o, field, isSet);
	}

	@Override
	public void visit(final int o, final Field field, final boolean isSet)
			throws IOException {
		writeInt32Field(o, field, isSet);
	}

	@Override
	public void visit(final long o, final Field field, final boolean isSet)
			throws IOException {
		writeInt64Field(o, field, isSet);
	}

	@Override
	public void visit(final float o, final Field field, final boolean isSet)
			throws IOException {
		writeFloat32Field(o, field, isSet);
	}

	@Override
	public void visit(final double o, final Field field, final boolean isSet)
			throws IOException {
		writeFloat64Field(o, field, isSet);
	}

	@Override
	public void visit(final String o, final Field field, final boolean isSet)
			throws IOException {
		writeStringField(o, field, isSet);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void visit(final Object o, final Field field, final boolean isSet)
			throws IOException {
		switch (field.typ().typeEnum()) {
		case ARRAY:
			writeArrayField(o, field, isSet);
			break;
		case LIST:
			writeListField((ArrayList<Object>) o, field, isSet);
			break;
		case MAP:
			writeMapField((HashMap<Object, Object>) o, field, isSet);
			break;
		case UNKNOWN:
		case CUSTOM:
			writeMGenObjectField((MGenBase) o, field, isSet);
			break;
		default:
			throw new UnknownTypeException("Don't know how to write " + o + "("
					+ field.typ().typeEnum() + ") of field " + field);
		}
	}

	@Override
	public void beginVisit(final MGenBase object, final int nFieldsSet, final int nFieldsTotal)
			throws IOException {
		beginWrite(object, nFieldsSet, nFieldsTotal);
	}

	@Override
	public void endVisit() throws IOException {
		finishWrite();
	}

	protected ClassRegistry registry() {
		return m_classRegistry;
	}

	public abstract void writeMGenObject(final MGenBase object)
			throws IOException;

	protected void validateThrow(final MGenBase o) {
		if (o != null && !o._validate(FieldSetDepth.SHALLOW)) {
			throw new SerializationException("MGenWriter.validate failed: A "
					+ o.getClass()
					+ " does not have all required fields set, missing: "
					+ o._missingRequiredFields());
		}
	}

}
