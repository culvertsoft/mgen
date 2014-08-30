package se.culvertsoft.mgen.api.model;

import java.util.ArrayList;

/**
 * Interface for list types
 */
public class ListType extends ListOrArrayType {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String fullName() {
		return "list[" + elementType().fullName() + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String shortName() {
		return "list[" + elementType().shortName() + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLinked() {
		return elementType().isLinked();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> classOf() {
		return ArrayList.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsUserDefinedType() {
		return elementType().containsUserDefinedType();
	}

	public ListType(final Type elementType) {
		super(TypeEnum.LIST, elementType);
	}

}
