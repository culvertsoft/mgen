package se.culvertsoft.mgen.api.model;

import se.culvertsoft.mgen.api.model.impl.Float64TypeImpl;

public interface Float64Type extends PrimitiveType {
	public final static Float64Type INSTANCE = Float64TypeImpl.INSTANCE;
}
