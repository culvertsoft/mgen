package se.culvertsoft.mgen.api.model.impl;

import java.util.ArrayList;
import java.util.Set;

import se.culvertsoft.mgen.api.model.CustomType;
import se.culvertsoft.mgen.api.model.ListType;
import se.culvertsoft.mgen.api.model.Module;
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
		m_elementType = new UnknownCustomTypeImpl(writtenElemType, -1);
	}

	public Type elementType() {
		return m_elementType;
	}

	public void setElementType(final Type elementType) {
		m_elementType = elementType;
		resetHashCaches();
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
	public boolean isTypeKnown() {
		return m_elementType.isTypeKnown();
	}

	@Override
	public Set<Module> getAllReferencedModulesInclSuper() {
		return m_elementType.getAllReferencedModulesInclSuper();
	}

	@Override
	public Set<CustomType> getAllReferencedTypesInclSuper() {
		return m_elementType.getAllReferencedTypesInclSuper();
	}

	@Override
	public Class<?> doClassOf() {
		return ArrayList.class;
	}

	@Override
	public boolean containsMgenCreatedType() {
		return elementType().containsMgenCreatedType();
	}

}
