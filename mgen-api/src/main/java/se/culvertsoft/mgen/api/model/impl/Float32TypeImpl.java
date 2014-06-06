package se.culvertsoft.mgen.api.model.impl;

import se.culvertsoft.mgen.api.model.Float32Type;
import se.culvertsoft.mgen.api.model.TypeEnum;

public class Float32TypeImpl extends PrimitiveTypeImpl implements Float32Type {

	private Float32TypeImpl() {
		super(TypeEnum.FLOAT32);
	}

	@Override
	public String fullName() {
		return "float32";
	}

	@Override
	public String shortName() {
		return fullName();
	}

	public final static Float32TypeImpl INSTANCE = new Float32TypeImpl();

	@Override
	public Class<?> doClassOf() {
		return float.class;
	}

}
