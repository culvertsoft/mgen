package se.culvertsoft.mgen.api.model;

import se.culvertsoft.mgen.api.model.impl.MGenBaseTypeImpl;

public interface MGenBaseType extends Type {
	public static MGenBaseType INSTANCE = new MGenBaseTypeImpl();
}
