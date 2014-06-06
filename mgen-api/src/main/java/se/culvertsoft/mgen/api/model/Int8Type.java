package se.culvertsoft.mgen.api.model;

import se.culvertsoft.mgen.api.model.impl.Int8TypeImpl;

public interface Int8Type extends PrimitiveType {

	public final static Int8Type INSTANCE = Int8TypeImpl.INSTANCE;
}
