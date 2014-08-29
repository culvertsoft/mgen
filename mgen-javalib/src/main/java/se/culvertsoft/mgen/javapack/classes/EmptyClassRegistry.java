package se.culvertsoft.mgen.javapack.classes;

/**
 * Represents an empty class registry. Used for demo and testing purposes.
 */
public class EmptyClassRegistry extends ClassRegistryBase {

	@Override
	public ClassRegistryEntry getByTypeIds16Bit(short[] ids) {
		return null;
	}

	@Override
	public ClassRegistryEntry getByTypeIds16BitBase64(String[] ids) {
		return null;
	}

}
