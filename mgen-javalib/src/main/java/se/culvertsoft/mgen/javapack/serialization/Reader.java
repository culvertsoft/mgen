package se.culvertsoft.mgen.javapack.serialization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.javapack.classes.MGenBase;

public interface Reader {

	public boolean readBooleanField(final Field field, final Object context)
			throws IOException;

	public byte readInt8Field(final Field field, final Object context)
			throws IOException;

	public short readInt16Field(final Field field, final Object context)
			throws IOException;

	public int readInt32Field(final Field field, final Object context)
			throws IOException;

	public long readInt64Field(final Field field, final Object context)
			throws IOException;

	public float readFloat32Field(final Field field, final Object context)
			throws IOException;

	public double readFloat64Field(final Field field, final Object context)
			throws IOException;

	public String readStringField(final Field field, final Object context)
			throws IOException;

	public Object readArrayField(final Field field, final Object context)
			throws IOException;

	public ArrayList<?> readListField(final Field field, final Object context)
			throws IOException;

	public HashMap<?, ?> readMapField(final Field field, final Object context)
			throws IOException;

	public MGenBase readMgenObjectField(final Field field, final Object context)
			throws IOException;

	public void handleUnknownField(final Field field, final Object context)
			throws IOException;

	public MGenBase readMGenObject() throws IOException;

}
