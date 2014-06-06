package se.culvertsoft.mgen.api.model.impl;

import se.culvertsoft.mgen.api.model.Int64Type;
import se.culvertsoft.mgen.api.model.TypeEnum;

public class Int64TypeImpl extends PrimitiveTypeImpl implements Int64Type {

	private Int64TypeImpl() {
		super(TypeEnum.INT64);
	}

	@Override
	public String fullName() {
		return "int64";
	}

	@Override
	public String shortName() {
		return fullName();
	}

	public final static Int64TypeImpl INSTANCE = new Int64TypeImpl();

	@Override
	public Class<?> doClassOf() {
		return long.class;
	}

}
