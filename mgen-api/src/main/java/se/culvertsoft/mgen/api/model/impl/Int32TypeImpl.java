package se.culvertsoft.mgen.api.model.impl;

import se.culvertsoft.mgen.api.model.Int32Type;
import se.culvertsoft.mgen.api.model.TypeEnum;

public class Int32TypeImpl extends PrimitiveTypeImpl implements Int32Type {

	private Int32TypeImpl() {
		super(TypeEnum.INT32);
	}

	@Override
	public String fullName() {
		return "int32";
	}

	@Override
	public String shortName() {
		return fullName();
	}

	public final static Int32TypeImpl INSTANCE = new Int32TypeImpl();

	@Override
	public Class<?> doClassOf() {
		return int.class;
	}

}
