package se.culvertsoft.mgen.api.model.impl;

import se.culvertsoft.mgen.api.model.PrimitiveType;
import se.culvertsoft.mgen.api.model.TypeEnum;

public abstract class PrimitiveTypeImpl extends TypeImpl implements PrimitiveType {

	public PrimitiveTypeImpl(final TypeEnum typeEnum) {
		super(typeEnum);
	}

	@Override
	public boolean isLinked() {
		return true;
	}

	@Override
	public boolean containsCustomType() {
		return false;
	}

}
