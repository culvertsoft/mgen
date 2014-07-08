package se.culvertsoft.mgen.api.model.impl;

import java.util.Collections;
import java.util.Set;

import se.culvertsoft.mgen.api.model.CustomType;
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

	@SuppressWarnings("unchecked")
	@Override
	public Set<CustomType> referencedTypes() {
		return Collections.EMPTY_SET;
	}

	@Override
	public boolean containsCustomType() {
		return false;
	}

}
