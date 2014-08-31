package se.culvertsoft.mgen.api.model;

/**
 * Convenience interface for array and list types
 */
public abstract class ListOrArrayType extends Type {

	/**
	 * Gets the type of the elements in this array or list
	 * 
	 * @return The type of the elements in this array or list
	 * 
	 * @throws RuntimeException
	 *             If called inside the compiler
	 */
	public Type elementType() {
		return m_elementType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLinked() {
		return m_elementType.isLinked();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsUserDefinedType() {
		return elementType().containsUserDefinedType();
	}

	/**
	 * Creates a new ListOrArray type. Not to be used directly - Use ListType or
	 * ArrayType instead.
	 * 
	 * @param enm
	 *            The TypeEnum of this type. Must be LIST or ARRAY.
	 * 
	 * @param elementType
	 *            The element type of this ListOrArrayType
	 */
	protected ListOrArrayType(final TypeEnum enm, final Type elementType) {
		super(enm);
		m_elementType = elementType;
	}

	private final Type m_elementType;

}
