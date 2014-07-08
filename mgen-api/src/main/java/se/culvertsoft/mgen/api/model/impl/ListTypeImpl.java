package se.culvertsoft.mgen.api.model.impl;

import java.util.ArrayList;
import java.util.Set;

import se.culvertsoft.mgen.api.model.CustomType;
import se.culvertsoft.mgen.api.model.ListType;
import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.api.model.TypeEnum;

public class ListTypeImpl extends TypeImpl implements ListType {

	private Type m_elementType;

	public ListTypeImpl(final Type elementType) {
		super(TypeEnum.LIST);
		m_elementType = elementType;
	}

	public ListTypeImpl(final String writtenElemType) {
		super(TypeEnum.LIST);
		m_elementType = new UnlinkedCustomType(writtenElemType, -1);
	}

	public Type elementType() {
		return m_elementType;
	}

	public void setElementType(final Type elementType) {
		m_elementType = elementType;
	}

	@Override
	public String fullName() {
		return "list[" + m_elementType.fullName() + "]";
	}

	@Override
	public String shortName() {
		return fullName();
	}

	@Override
	public boolean isLinked() {
		return m_elementType.isLinked();
	}

	@Override
	public Set<CustomType> referencedTypes() {
		return m_elementType.referencedTypes();
	}

	@Override
	public Class<?> doClassOf() {
		return ArrayList.class;
	}

	@Override
	public boolean containsCustomType() {
		return elementType().containsCustomType();
	}

}
