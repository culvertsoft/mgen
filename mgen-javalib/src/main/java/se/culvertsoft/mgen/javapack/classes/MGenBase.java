package se.culvertsoft.mgen.javapack.classes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.api.util.Hasher;
import se.culvertsoft.mgen.javapack.metadata.FieldSetDepth;
import se.culvertsoft.mgen.javapack.serialization.FieldVisitor;
import se.culvertsoft.mgen.javapack.serialization.Reader;

public abstract class MGenBase {

	/************************************************************************
	 * 
	 * 
	 * - - - - - - - - - - CLASSIC POJO METHODS - - - - - - - - - - -
	 * 
	 * **********************************************************************/

	public abstract String toString();

	public abstract boolean equals(final Object other);

	public abstract int hashCode();

	public abstract MGenBase deepCopy();

	/************************************************************************
	 * 
	 * 
	 * - - - - - - - - - - FOR IDENTIFICATION - - - - - - - - - - -
	 * 
	 * **********************************************************************/

	public abstract short _typeHash16bit();

	public abstract int _typeHash32bit();

	public abstract String _typeName();

	public abstract short[] _typeHashes16bit();

	public abstract int[] _typeHashes32bit();

	public abstract Collection<String> _typeHashes16bitBase64();

	public abstract Collection<String> _typeHashes32bitBase64();

	public abstract Collection<String> _typeNames();

	/************************************************************************
	 * 
	 * 
	 * - - - - - - - - - - FIELD DATA EXTRACTORS/SETTERS - - - - - - - - -
	 * 
	 * **********************************************************************/

	public abstract Collection<Field> _fields();

	public abstract Field _fieldBy16BitHash(final short hash);

	public abstract Field _fieldBy32BitHash(final int hash);

	public Field _fieldByName(final String memberName) {
		return memberName != null ? _fieldBy16BitHash(Hasher
				.static_16bit(memberName)) : null;
	}

	public abstract int _nFieldsSet(final FieldSetDepth fieldSetDepth);

	public abstract boolean _isFieldSet(
			final Field field,
			final FieldSetDepth depth);

	public abstract MGenBase _setAllFieldsSet(
			final boolean state,
			final FieldSetDepth depth);

	public abstract boolean _validate(final FieldSetDepth depth);

	public abstract void _accept(final FieldVisitor visitor) throws IOException;

	public abstract boolean _readField(
			final Field field,
			final Object context,
			final Reader reader) throws IOException;

	public List<Field> _missingRequiredFields() {
		final ArrayList<Field> missingFields = new ArrayList<Field>();
		for (final Field field : _fields()) {
			if (field.isRequired()
					&& !_isFieldSet(field, FieldSetDepth.SHALLOW)) {
				missingFields.add(field);
			}
		}
		return missingFields;
	}

}
