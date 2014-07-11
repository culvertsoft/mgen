package se.culvertsoft.mgen.api.model.impl;

import se.culvertsoft.mgen.api.model.SimpleType;
import se.culvertsoft.mgen.api.model.TypeEnum;

public abstract class SimpleTypeImpl extends TypeImpl implements SimpleType {

	public SimpleTypeImpl(final TypeEnum typeEnum) {
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
