package se.culvertsoft.mgen.api.model;

import java.util.Set;

public interface Type {

	public Class<?> classOf();

	public long typeId();

	public short typeId16Bit();

	public String typeId16BitBase64();

	public TypeEnum typeEnum();

	public boolean isPrimitive();

	public boolean isSimple();

	public String shortName();

	public String fullName();

	public boolean isTypeKnown();

	public boolean containsMgenCreatedType();

	public boolean isMGenCreatedType();

	public byte binaryTypeTag();

	public Set<Module> getAllReferencedModulesInclSuper();

	public Set<CustomType> getAllReferencedTypesInclSuper();

}
