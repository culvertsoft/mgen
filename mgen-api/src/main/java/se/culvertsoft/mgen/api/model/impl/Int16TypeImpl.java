package se.culvertsoft.mgen.api.model.impl;

import se.culvertsoft.mgen.api.model.Int16Type;
import se.culvertsoft.mgen.api.model.TypeEnum;

public class Int16TypeImpl extends PrimitiveTypeImpl implements Int16Type {

	private Int16TypeImpl() {
		super(TypeEnum.INT16);
	}

	@Override
	public String fullName() {
		return "int16";
	}

	@Override
	public String shortName() {
		return fullName();
	}

	public final static Int16TypeImpl INSTANCE = new Int16TypeImpl();

	@Override
	public Class<?> doClassOf() {
		return short.class;
	}

}
