package se.culvertsoft.mgen.javapack.serialization;

import static se.culvertsoft.mgen.javapack.serialization.BuiltInSerializerUtils.ensureNoMissingReqFields;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.HashMap;

import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.javapack.classes.ClassRegistry;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.exceptions.UnknownTypeException;

public abstract class BuiltInWriter implements FieldVisitor {

	static protected final Charset charset = Charset.forName("UTF8");
	protected final CharsetEncoder stringEncoder;

	protected final ClassRegistry m_classRegistry;
	protected final DataOutput m_stream;

	public BuiltInWriter(
			final DataOutput stream,
			final ClassRegistry classRegistry) {
		m_classRegistry = classRegistry;
		m_stream = stream;
		stringEncoder = charset
				.newEncoder()
				.onMalformedInput(CodingErrorAction.REPLACE)
				.onUnmappableCharacter(CodingErrorAction.REPLACE);
	}

	public abstract void beginWrite(
			final MGenBase object,
			final int nFieldsSet,
			final int nFieldsTotal) throws IOException;

	public abstract void writeBooleanField(final boolean b, final Field field)
			throws IOException;

	public abstract void writeInt8Field(final byte b, final Field field)
			throws IOException;

	public abstract void writeInt16Field(final short s, final Field field)
			throws IOException;

	public abstract void writeInt32Field(final int i, final Field field)
			throws IOException;

	public abstract void writeInt64Field(final long l, final Field field)
			throws IOException;

	public abstract void writeFloat32Field(final float f, final Field field)
			throws IOException;

	public abstract void writeFloat64Field(final double d, final Field field)
			throws IOException;

	public abstract void writeStringField(final String s, final Field field)
			throws IOException;

	public abstract void writeListField(
			final ArrayList<Object> list,
			final Field field) throws IOException;

	public abstract void writeMapField(
			final HashMap<Object, Object> map,
			final Field field) throws IOException;

	public abstract void writeArrayField(final Object array, final Field field)
			throws IOException;

	public abstract void writeMGenObjectField(
			final MGenBase o,
			final Field field) throws IOException;

	public abstract void finishWrite() throws IOException;

	@Override
	public void visit(final byte o, final Field field, final boolean isSet)
			throws IOException {
		if (isSet)
			writeInt8Field(o, field);
	}

	@Override
	public void visit(final short o, final Field field, final boolean isSet)
			throws IOException {
		if (isSet)
			writeInt16Field(o, field);
	}

	@Override
	public void visit(final int o, final Field field, final boolean isSet)
			throws IOException {
		if (isSet)
			writeInt32Field(o, field);
	}

	@Override
	public void visit(final long o, final Field field, final boolean isSet)
			throws IOException {
		writeInt64Field(o, field);
	}

	@Override
	public void visit(final float o, final Field field, final boolean isSet)
			throws IOException {
		if (isSet)
			writeFloat32Field(o, field);
	}

	@Override
	public void visit(final double o, final Field field, final boolean isSet)
			throws IOException {
		if (isSet)
			writeFloat64Field(o, field);
	}

	@Override
	public void visit(final String o, final Field field, final boolean isSet)
			throws IOException {
		if (isSet)
			writeStringField(o, field);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void visit(final Object o, final Field field, final boolean isSet)
			throws IOException {
		if (isSet) {
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
				throw new UnknownTypeException("Don't know how to write " + o
						+ "(" + field.typ().typeEnum() + ") of field " + field);
			}
		}
	}

	@Override
	public void beginVisit(
			final MGenBase object,
			final int nFieldsSet,
			final int nFieldsTotal) throws IOException {
		ensureNoMissingReqFields(object);
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

	protected ByteBuffer encodeString(final String s) {
		try {
			return stringEncoder.encode(CharBuffer.wrap(s));
		} catch (CharacterCodingException x) {
			throw new Error(x); // Can't happen
		}
	}
}
