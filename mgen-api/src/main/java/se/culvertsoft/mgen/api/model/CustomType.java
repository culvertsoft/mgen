package se.culvertsoft.mgen.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import se.culvertsoft.mgen.api.model.impl.CustomTypeImpl;

public interface CustomType extends Type {

	public String name();

	public Module module();

	public Type superType();

	public boolean hasSuperType();

	public boolean hasSubTypes();
	
	public int localTypeId();

	public List<CustomType> subTypes();

	public List<Type> superTypeHierarchy();

	public List<Field> fields();

	public List<Field> getAllFieldsInclSuper();

	public Set<CustomType> getAllReferencedTypesExclSuper();

	public Set<Module> getAllReferencedExtModulesInclSuper();

	public Set<CustomType> getAllReferencedExtTypesInclSuper();

	public Set<CustomType> getDirectDependencies();

	public static CustomType INSTANCE = new CustomTypeImpl(-1, "",
			Module.INSTANCE, null, new ArrayList<Field>());

}
