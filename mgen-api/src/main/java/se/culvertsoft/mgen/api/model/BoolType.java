package se.culvertsoft.mgen.api.model;

import se.culvertsoft.mgen.api.model.impl.BoolTypeImpl;

public interface BoolType extends PrimitiveType {

	public final static BoolType INSTANCE = BoolTypeImpl.INSTANCE;

}
