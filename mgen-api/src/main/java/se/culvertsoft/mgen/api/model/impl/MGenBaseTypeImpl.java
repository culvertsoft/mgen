package se.culvertsoft.mgen.api.model.impl;

import java.util.Collections;
import java.util.Set;

import se.culvertsoft.mgen.api.model.CustomType;
import se.culvertsoft.mgen.api.model.MGenBaseType;
import se.culvertsoft.mgen.api.model.Module;
import se.culvertsoft.mgen.api.model.TypeEnum;

public class MGenBaseTypeImpl extends TypeImpl implements MGenBaseType {

	public MGenBaseTypeImpl() {
		super(TypeEnum.MGEN_BASE);
	}

	@Override
	public String fullName() {
		return "se.culvertsoft.mgen.javapack.classes.MGenBase";
	}

	@Override
	public String shortName() {
		return "MGenBase";
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
	public Class<?> doClassOf() {
		try {
			return Class.forName(fullName());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean containsMgenCreatedType() {
		return true;
	}
}
