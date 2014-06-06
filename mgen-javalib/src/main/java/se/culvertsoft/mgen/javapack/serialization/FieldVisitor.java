package se.culvertsoft.mgen.javapack.serialization;

import java.io.IOException;

import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.javapack.classes.MGenBase;

public interface FieldVisitor {

	public void beginVisit(final MGenBase parent, final int nFieldsSet)
			throws IOException;

	public void visit(final byte o, final Field field, final boolean isSet)
			throws IOException;

	public void visit(final short o, final Field field, final boolean isSet)
			throws IOException;

	public void visit(final int o, final Field field, final boolean isSet)
			throws IOException;

	public void visit(final long o, final Field field, final boolean isSet)
			throws IOException;

	public void visit(final float o, final Field field, final boolean isSet)
			throws IOException;

	public void visit(final double o, final Field field, final boolean isSet)
			throws IOException;

	public void visit(final String o, final Field field, final boolean isSet)
			throws IOException;

	public void visit(final Object o, final Field field, final boolean isSet)
			throws IOException;

	public void endVisit() throws IOException;

}
