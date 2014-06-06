package se.culvertsoft.mgen.api.model.impl;

import se.culvertsoft.mgen.api.model.Float64Type;
import se.culvertsoft.mgen.api.model.TypeEnum;

public class Float64TypeImpl extends PrimitiveTypeImpl implements Float64Type {

	private Float64TypeImpl() {
		super(TypeEnum.FLOAT64);
	}

	@Override
	public String fullName() {
		return "float64";
	}

	@Override
	public String shortName() {
		return fullName();
	}

	public final static Float64TypeImpl INSTANCE = new Float64TypeImpl();

	@Override
	public Class<?> doClassOf() {
		return double.class;
	}

}
