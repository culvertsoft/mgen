package se.culvertsoft.mgen.javapack.serialization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.exceptions.UnexpectedTypeException;

/**
 * Interface for all MGen object readers. This interface is use to facilitate
 * "reverse visitation" of an object. That is visiting an incoming data stream
 * and assigning field values to objects read back.
 */
public interface Reader {

	/**
	 * Read API for users. This method reads an MGen object from the underlying
	 * data input source.
	 * 
	 * @return The MGen object read from the stream, or null if the type of the
	 *         object on the stream was unknown (Readers then skip past the
	 *         object).
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 */
	public MGenBase readObject() throws IOException;

	/**
	 * Read API for users. This method should be called to initiate the read of
	 * an MGen object from a Reader. This method also takes a type parameter
	 * that the Reader should respect and cast the read back object to. This
	 * method can also be used to read objects that are written to the stream
	 * without type meta data information.
	 * 
	 * @return The MGen object read from the stream.
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream,
	 *             such as reaching EOF before expected.
	 * 
	 * @throws UnexpectedTypeException
	 *             if the object read from the stream is not of specified type T
	 */
	public <T extends MGenBase> T readObject(final Class<T> typ) throws IOException;

	/**
	 * Method called to read an enum field from a stream.
	 * 
	 * Reverse visitor method when visiting a data input stream. Called by the
	 * object being visited. The logic is: Readers read streams and for each
	 * field detected in the stream, the corresponding field on the object being
	 * read back is visited. This is achieved by the reader calling the object's
	 * readField(..)-method with the field id that was read from the stream. The
	 * object then calls back to _this_ read-function with the field type meta
	 * data required to read the actual value.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            An optional parameter that may be passed to the Reader
	 * 
	 * @return The value read
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream
	 */
	public Enum<?> readEnumField(final Field field, final Object context) throws IOException;

	/**
	 * Method called to read a boolean field from a stream.
	 * 
	 * Reverse visitor method when visiting a data input stream. Called by the
	 * object being visited. The logic is: Readers read streams and for each
	 * field detected in the stream, the corresponding field on the object being
	 * read back is visited. This is achieved by the reader calling the object's
	 * readField(..)-method with the field id that was read from the stream. The
	 * object then calls back to _this_ read-function with the field type meta
	 * data required to read the actual value.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            An optional parameter that may be passed to the Reader
	 * 
	 * @return The value read
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream
	 */
	public boolean readBooleanField(final Field field, final Object context) throws IOException;

	/**
	 * Method called to read an int8 field from a stream.
	 * 
	 * Reverse visitor method when visiting a data input stream. Called by the
	 * object being visited. The logic is: Readers read streams and for each
	 * field detected in the stream, the corresponding field on the object being
	 * read back is visited. This is achieved by the reader calling the object's
	 * readField(..)-method with the field id that was read from the stream. The
	 * object then calls back to _this_ read-function with the field type meta
	 * data required to read the actual value.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            An optional parameter that may be passed to the Reader
	 * 
	 * @return The value read
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream
	 */
	public byte readInt8Field(final Field field, final Object context) throws IOException;

	/**
	 * Method called to read an int16 field from a stream.
	 * 
	 * Reverse visitor method when visiting a data input stream. Called by the
	 * object being visited. The logic is: Readers read streams and for each
	 * field detected in the stream, the corresponding field on the object being
	 * read back is visited. This is achieved by the reader calling the object's
	 * readField(..)-method with the field id that was read from the stream. The
	 * object then calls back to _this_ read-function with the field type meta
	 * data required to read the actual value.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            An optional parameter that may be passed to the Reader
	 * 
	 * @return The value read
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream
	 */
	public short readInt16Field(final Field field, final Object context) throws IOException;

	/**
	 * Method called to read an int32 field from a stream.
	 * 
	 * Reverse visitor method when visiting a data input stream. Called by the
	 * object being visited. The logic is: Readers read streams and for each
	 * field detected in the stream, the corresponding field on the object being
	 * read back is visited. This is achieved by the reader calling the object's
	 * readField(..)-method with the field id that was read from the stream. The
	 * object then calls back to _this_ read-function with the field type meta
	 * data required to read the actual value.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            An optional parameter that may be passed to the Reader
	 * 
	 * @return The value read
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream
	 */
	public int readInt32Field(final Field field, final Object context) throws IOException;

	/**
	 * Method called to read an int64 field from a stream.
	 * 
	 * Reverse visitor method when visiting a data input stream. Called by the
	 * object being visited. The logic is: Readers read streams and for each
	 * field detected in the stream, the corresponding field on the object being
	 * read back is visited. This is achieved by the reader calling the object's
	 * readField(..)-method with the field id that was read from the stream. The
	 * object then calls back to _this_ read-function with the field type meta
	 * data required to read the actual value.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            An optional parameter that may be passed to the Reader
	 * 
	 * @return The value read
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream
	 */
	public long readInt64Field(final Field field, final Object context) throws IOException;

	/**
	 * Method called to read a float32 field from a stream.
	 * 
	 * Reverse visitor method when visiting a data input stream. Called by the
	 * object being visited. The logic is: Readers read streams and for each
	 * field detected in the stream, the corresponding field on the object being
	 * read back is visited. This is achieved by the reader calling the object's
	 * readField(..)-method with the field id that was read from the stream. The
	 * object then calls back to _this_ read-function with the field type meta
	 * data required to read the actual value.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            An optional parameter that may be passed to the Reader
	 * 
	 * @return The value read
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream
	 */
	public float readFloat32Field(final Field field, final Object context) throws IOException;

	/**
	 * Method called to read a float64 field from a stream.
	 * 
	 * Reverse visitor method when visiting a data input stream. Called by the
	 * object being visited. The logic is: Readers read streams and for each
	 * field detected in the stream, the corresponding field on the object being
	 * read back is visited. This is achieved by the reader calling the object's
	 * readField(..)-method with the field id that was read from the stream. The
	 * object then calls back to _this_ read-function with the field type meta
	 * data required to read the actual value.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            An optional parameter that may be passed to the Reader
	 * 
	 * @return The value read
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream
	 */
	public double readFloat64Field(final Field field, final Object context) throws IOException;

	/**
	 * Method called to read a string field from a stream.
	 * 
	 * Reverse visitor method when visiting a data input stream. Called by the
	 * object being visited. The logic is: Readers read streams and for each
	 * field detected in the stream, the corresponding field on the object being
	 * read back is visited. This is achieved by the reader calling the object's
	 * readField(..)-method with the field id that was read from the stream. The
	 * object then calls back to _this_ read-function with the field type meta
	 * data required to read the actual value.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            An optional parameter that may be passed to the Reader
	 * 
	 * @return The string read
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream
	 */
	public String readStringField(final Field field, final Object context) throws IOException;

	/**
	 * Method called to read an array field from a stream.
	 * 
	 * Reverse visitor method when visiting a data input stream. Called by the
	 * object being visited. The logic is: Readers read streams and for each
	 * field detected in the stream, the corresponding field on the object being
	 * read back is visited. This is achieved by the reader calling the object's
	 * readField(..)-method with the field id that was read from the stream. The
	 * object then calls back to _this_ read-function with the field type meta
	 * data required to read the actual value.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            An optional parameter that may be passed to the Reader
	 * 
	 * @return The array read
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream
	 */
	public Object readArrayField(final Field field, final Object context) throws IOException;

	/**
	 * Method called to read a list field from a stream.
	 * 
	 * Reverse visitor method when visiting a data input stream. Called by the
	 * object being visited. The logic is: Readers read streams and for each
	 * field detected in the stream, the corresponding field on the object being
	 * read back is visited. This is achieved by the reader calling the object's
	 * readField(..)-method with the field id that was read from the stream. The
	 * object then calls back to _this_ read-function with the field type meta
	 * data required to read the actual value.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            An optional parameter that may be passed to the Reader
	 * 
	 * @return The list read
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream
	 */
	public ArrayList<?> readListField(final Field field, final Object context) throws IOException;

	/**
	 * Method called to read a map field from a stream.
	 * 
	 * Reverse visitor method when visiting a data input stream. Called by the
	 * object being visited. The logic is: Readers read streams and for each
	 * field detected in the stream, the corresponding field on the object being
	 * read back is visited. This is achieved by the reader calling the object's
	 * readField(..)-method with the field id that was read from the stream. The
	 * object then calls back to _this_ read-function with the field type meta
	 * data required to read the actual value.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            An optional parameter that may be passed to the Reader
	 * 
	 * @return The map read
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream
	 */
	public HashMap<?, ?> readMapField(final Field field, final Object context) throws IOException;

	/**
	 * Method called to read an MGen object field from a stream.
	 * 
	 * Reverse visitor method when visiting a data input stream. Called by the
	 * object being visited. The logic is: Readers read streams and for each
	 * field detected in the stream, the corresponding field on the object being
	 * read back is visited. This is achieved by the reader calling the object's
	 * readField(..)-method with the field id that was read from the stream. The
	 * object then calls back to _this_ read-function with the field type meta
	 * data required to read the actual value.
	 * 
	 * @param field
	 *            The field to read
	 * 
	 * @param context
	 *            An optional parameter that may be passed to the Reader
	 * 
	 * @return The MGen object read
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream
	 */
	public MGenBase readMgenObjectField(final Field field, final Object context) throws IOException;

	/**
	 * Method called when an unknown field (new field or old removed field) is
	 * detected in an input stream.
	 * 
	 * Reverse visitor method when visiting a data input stream. Called by the
	 * object being visited. The logic is: Readers read streams and for each
	 * field detected in the stream, the corresponding field on the object being
	 * read back is visited. This is achieved by the reader calling the object's
	 * readField(..)-method with the field id that was read from the stream. The
	 * object then calls back to _this_ handleUnknownField-function with the
	 * field type meta data required to read the actual value.
	 * 
	 * @param field
	 *            null
	 * 
	 * @param context
	 *            An optional parameter that may be passed to the Reader
	 * 
	 * @throws IOException
	 *             If an IOException occurs on the underlying data input stream
	 */
	public void handleUnknownField(final Field field, final Object context) throws IOException;

}
