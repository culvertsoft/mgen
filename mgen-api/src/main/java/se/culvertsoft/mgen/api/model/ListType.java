package se.culvertsoft.mgen.api.model;

import java.util.ArrayList;

/**
 * Interface for list types
 */
public class ListType extends ListOrArrayType {

	@Override
	public String fullName() {
		return "list[" + elementType().fullName() + "]";
	}

	@Override
	public String shortName() {
		return "list[" + elementType().shortName() + "]";
	}

	@Override
	public boolean isLinked() {
		return elementType().isLinked();
	}

	@Override
	public Class<?> classOf() {
		return ArrayList.class;
	}

	@Override
	public boolean containsUserDefinedType() {
		return elementType().containsUserDefinedType();
	}

	public ListType(final Type elementType) {
		super(TypeEnum.LIST, elementType);
	}

}
