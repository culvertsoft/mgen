package se.culvertsoft.mgen.api.model.impl;

import se.culvertsoft.mgen.api.model.PrimitiveType;
import se.culvertsoft.mgen.api.model.TypeEnum;

public abstract class PrimitiveTypeImpl extends SimpleTypeImpl implements
		PrimitiveType {

	public PrimitiveTypeImpl(final TypeEnum typeEnum) {
		super(typeEnum);
	}

	@Override
	public boolean isPrimitive() {
		return true;
	}

}
