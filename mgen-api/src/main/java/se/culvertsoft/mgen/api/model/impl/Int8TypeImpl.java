package se.culvertsoft.mgen.api.model.impl;

import se.culvertsoft.mgen.api.model.Int8Type;
import se.culvertsoft.mgen.api.model.TypeEnum;

public class Int8TypeImpl extends PrimitiveTypeImpl implements Int8Type {

	private Int8TypeImpl() {
		super(TypeEnum.INT8);
	}

	@Override
	public String fullName() {
		return "int8";
	}

	@Override
	public String shortName() {
		return fullName();
	}

	public final static Int8TypeImpl INSTANCE = new Int8TypeImpl();

	@Override
	public Class<?> doClassOf() {
		return byte.class;
	}

}
