package se.culvertsoft.mgen.api.model;

import java.util.List;
import java.util.Set;

public interface CustomType extends Type {

	public long typeId();

	public short typeId16Bit();

	public String typeId16BitBase64();

	public String name();

	public Module module();

	public CustomType superType();

	public boolean hasSuperType();

	public boolean hasSubTypes();

	public List<CustomType> subTypes();

	public List<CustomType> superTypeHierarchy();

	public List<Field> fields();

	public List<Field> getAllFieldsInclSuper();

	public Set<CustomType> getAllReferencedTypesExclSuper();

	public Set<Module> getAllReferencedExtModulesInclSuper();

	public Set<CustomType> getAllReferencedExtTypesInclSuper();

	public Set<CustomType> getDirectDependencies();

}
