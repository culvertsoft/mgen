package se.culvertsoft.mgen.javapack.serialization;

import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.javapack.classes.ClassRegistryEntry;

public interface ReadErrorListener {

	public void handleDerivedTypeAvailable(
			final ClassRegistryEntry currentType,
			final short[] allTypeIds);

	public void handleTooManyFields(
			final ClassRegistryEntry currentType,
			final int currentFieldCount,
			final int writtenFieldCount);

	public void handleUnknownType(
			final byte readTag,
			final short[] typeIds,
			final Object object);

	public void handleUnknownField(
			final Object context,
			final byte readTag,
			final short[] readIds,
			final Object readObject);

	public void handleUnexpectedElementType(
			final Field field,
			final byte expectElemTypeTag,
			final byte readElemTypeTag,
			final short[] readIds,
			final Object readObject);

	public void handleUnexpectedType(
			final Field field,
			final byte expectTypeTag,
			final byte readTypeTag,
			final short[] readIds,
			final Object readObject);

}
