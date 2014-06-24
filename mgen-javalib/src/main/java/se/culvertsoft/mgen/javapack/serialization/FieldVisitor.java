package se.culvertsoft.mgen.javapack.serialization;

import java.io.IOException;

import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.javapack.classes.MGenBase;

public interface FieldVisitor {

	public void beginVisit(final MGenBase object, final int nFieldsSet,
			final int nFieldsTotal) throws IOException;

	public void visit(final byte b, final Field field, final boolean isSet)
			throws IOException;

	public void visit(final short s, final Field field, final boolean isSet)
			throws IOException;

	public void visit(final int i, final Field field, final boolean isSet)
			throws IOException;

	public void visit(final long l, final Field field, final boolean isSet)
			throws IOException;

	public void visit(final float f, final Field field, final boolean isSet)
			throws IOException;

	public void visit(final double d, final Field field, final boolean isSet)
			throws IOException;

	public void visit(final String s, final Field field, final boolean isSet)
			throws IOException;

	public void visit(final Object o, final Field field, final boolean isSet)
			throws IOException;

	public void endVisit() throws IOException;

}
