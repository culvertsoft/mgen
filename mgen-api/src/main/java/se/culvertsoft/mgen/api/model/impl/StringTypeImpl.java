package se.culvertsoft.mgen.api.model.impl;

import se.culvertsoft.mgen.api.model.StringType;
import se.culvertsoft.mgen.api.model.TypeEnum;

public class StringTypeImpl extends TypeImpl implements StringType {

	private StringTypeImpl() {
		super(TypeEnum.STRING);
	}

	@Override
	public String fullName() {
		return "string";
	}

	@Override
	public String shortName() {
		return fullName();
	}

	@Override
	public String toString() {
		return "string";
	}

	public final static StringTypeImpl INSTANCE = new StringTypeImpl();

	@Override
	public Class<?> doClassOf() {
		return String.class;
	}

	@Override
	public boolean isLinked() {
		return true;
	}

	@Override
	public boolean containsCustomType() {
		return false;
	}

}
