package se.culvertsoft.mgen.api.model.impl;

import se.culvertsoft.mgen.api.model.BoolType;
import se.culvertsoft.mgen.api.model.TypeEnum;

public class BoolTypeImpl extends PrimitiveTypeImpl implements BoolType {

	private BoolTypeImpl() {
		super(TypeEnum.BOOL);
	}

	@Override
	public String fullName() {
		return "bool";
	}

	@Override
	public String shortName() {
		return fullName();
	}

	public final static BoolTypeImpl INSTANCE = new BoolTypeImpl();

	@Override
	public Class<?> doClassOf() {
		return boolean.class;
	}

}
