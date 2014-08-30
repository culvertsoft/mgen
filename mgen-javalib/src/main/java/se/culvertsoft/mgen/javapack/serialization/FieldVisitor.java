package se.culvertsoft.mgen.javapack.serialization;

import java.io.IOException;

import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.javapack.classes.MGenBase;

/**
 * Base interface of all MGen object visitors. It is used to visit MGen objects
 * and their fields. All generated MGen classes have _accept(..) methods which
 * takes FieldVisitor instances as arguments. The object then calls back this
 * visitor for each field selected for visitation.
 */
public interface FieldVisitor {

	/**
	 * Called when starting to visit an MGen object and its fields.
	 * 
	 * @param object
	 *            The object that is being visited
	 * 
	 * @param nFields
	 *            The number of fields that will be visited
	 * 
	 * @throws IOException
	 *             FieldVisitor instances are usually serializers (writers), and
	 *             thus write to an underlying data output stream. This
	 *             exception declaration is to pass on any IOExceptions thrown
	 *             when writing to that underlying data output stream.
	 */
	public void beginVisit(final MGenBase object, final int nFields) throws IOException;

	/**
	 * Called to visit a boolean field
	 * 
	 * @param b
	 *            The field value
	 * 
	 * @param field
	 *            The field type metadata
	 * 
	 * @throws IOException
	 *             FieldVisitor instances are usually serializers (writers), and
	 *             thus write to an underlying data output stream. This
	 *             exception declaration is to pass on any IOExceptions thrown
	 *             when writing to that underlying data output stream.
	 */
	public void visit(final boolean b, final Field field) throws IOException;

	/**
	 * Called to visit an int8 field
	 * 
	 * @param b
	 *            The field value
	 * 
	 * @param field
	 *            The field type metadata
	 * 
	 * @throws IOException
	 *             FieldVisitor instances are usually serializers (writers), and
	 *             thus write to an underlying data output stream. This
	 *             exception declaration is to pass on any IOExceptions thrown
	 *             when writing to that underlying data output stream.
	 */
	public void visit(final byte b, final Field field) throws IOException;

	/**
	 * Called to visit an int16 field
	 * 
	 * @param s
	 *            The field value
	 * 
	 * @param field
	 *            The field type metadata
	 * 
	 * @throws IOException
	 *             FieldVisitor instances are usually serializers (writers), and
	 *             thus write to an underlying data output stream. This
	 *             exception declaration is to pass on any IOExceptions thrown
	 *             when writing to that underlying data output stream.
	 */
	public void visit(final short s, final Field field) throws IOException;

	/**
	 * Called to visit an int32 field
	 * 
	 * @param i
	 *            The field value
	 * 
	 * @param field
	 *            The field type metadata
	 * 
	 * @throws IOException
	 *             FieldVisitor instances are usually serializers (writers), and
	 *             thus write to an underlying data output stream. This
	 *             exception declaration is to pass on any IOExceptions thrown
	 *             when writing to that underlying data output stream.
	 */
	public void visit(final int i, final Field field) throws IOException;

	/**
	 * Called to visit an int64 field
	 * 
	 * @param l
	 *            The field value
	 * 
	 * @param field
	 *            The field type metadata
	 * 
	 * @throws IOException
	 *             FieldVisitor instances are usually serializers (writers), and
	 *             thus write to an underlying data output stream. This
	 *             exception declaration is to pass on any IOExceptions thrown
	 *             when writing to that underlying data output stream.
	 */
	public void visit(final long l, final Field field) throws IOException;

	/**
	 * Called to visit a float32 field
	 * 
	 * @param f
	 *            The field value
	 * 
	 * @param field
	 *            The field type metadata
	 * 
	 * @throws IOException
	 *             FieldVisitor instances are usually serializers (writers), and
	 *             thus write to an underlying data output stream. This
	 *             exception declaration is to pass on any IOExceptions thrown
	 *             when writing to that underlying data output stream.
	 */
	public void visit(final float f, final Field field) throws IOException;

	/**
	 * Called to visit a float64 field
	 * 
	 * @param d
	 *            The field value
	 * 
	 * @param field
	 *            The field type metadata
	 * 
	 * @throws IOException
	 *             FieldVisitor instances are usually serializers (writers), and
	 *             thus write to an underlying data output stream. This
	 *             exception declaration is to pass on any IOExceptions thrown
	 *             when writing to that underlying data output stream.
	 */
	public void visit(final double d, final Field field) throws IOException;

	/**
	 * Called to visit a string field
	 * 
	 * @param s
	 *            The field value
	 * 
	 * @param field
	 *            The field type metadata
	 * 
	 * @throws IOException
	 *             FieldVisitor instances are usually serializers (writers), and
	 *             thus write to an underlying data output stream. This
	 *             exception declaration is to pass on any IOExceptions thrown
	 *             when writing to that underlying data output stream.
	 */
	public void visit(final String s, final Field field) throws IOException;

	/**
	 * Called to visit an enum field
	 * 
	 * @param e
	 *            The field value
	 * 
	 * @param field
	 *            The field type metadata
	 * 
	 * @throws IOException
	 *             FieldVisitor instances are usually serializers (writers), and
	 *             thus write to an underlying data output stream. This
	 *             exception declaration is to pass on any IOExceptions thrown
	 *             when writing to that underlying data output stream.
	 */
	public void visit(final Enum<?> e, final Field field) throws IOException;

	/**
	 * Called to visit a list, array, map or MGen object field
	 * 
	 * @param o
	 *            The field value
	 * 
	 * @param field
	 *            The field type metadata
	 * 
	 * @throws IOException
	 *             FieldVisitor instances are usually serializers (writers), and
	 *             thus write to an underlying data output stream. This
	 *             exception declaration is to pass on any IOExceptions thrown
	 *             when writing to that underlying data output stream.
	 */
	public void visit(final Object o, final Field field) throws IOException;

	/**
	 * Called after all the fields of an object have been visited.
	 */
	public void endVisit() throws IOException;

}
