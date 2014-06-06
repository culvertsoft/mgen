package se.culvertsoft.mgen.api.model.impl;

import java.util.Collections;
import java.util.Set;

import se.culvertsoft.mgen.api.model.CustomType;
import se.culvertsoft.mgen.api.model.Module;
import se.culvertsoft.mgen.api.model.SimpleType;
import se.culvertsoft.mgen.api.model.TypeEnum;

public abstract class SimpleTypeImpl extends TypeImpl implements SimpleType {

	public SimpleTypeImpl(final TypeEnum typeEnum) {
		super(typeEnum);
	}

	@Override
	public boolean isSimple() {
		return true;
	}

	@Override
	public boolean isTypeKnown() {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Module> getAllReferencedModulesInclSuper() {
		return Collections.EMPTY_SET;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<CustomType> getAllReferencedTypesInclSuper() {
		return Collections.EMPTY_SET;
	}

	@Override
	public boolean containsMgenCreatedType() {
		return false;
	}

}
