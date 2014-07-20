package se.culvertsoft.mgen.javapack.classes;

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
