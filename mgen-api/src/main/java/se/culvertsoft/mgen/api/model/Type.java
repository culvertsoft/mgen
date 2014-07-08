package se.culvertsoft.mgen.api.model;

import java.util.Set;

public interface Type {

	public Class<?> classOf();

	public TypeEnum typeEnum();
	
	public byte typeTag();

	public String shortName();

	public String fullName();

	public boolean isLinked();

	public boolean containsCustomType();

	public Set<CustomType> referencedTypes();

}
