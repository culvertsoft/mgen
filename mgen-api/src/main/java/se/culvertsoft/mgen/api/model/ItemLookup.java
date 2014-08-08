package se.culvertsoft.mgen.api.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.exceptions.AnalysisException;

public class ItemLookup {

	/**
	 * Looks up a type (enum or class) by name. The supplied name can be either
	 * the fully qualified class name or just the short name of the class.
	 * 
	 * If multiple classes match a short name an AnalysisException will be
	 * thrown if none of them are declared in the same module as the parameter
	 * referencedFrom.
	 * 
	 * If no constant is found this function throws an AnalysisException.
	 * 
	 * 
	 * @param name
	 *            The full or short name of the class or enum to look for
	 * 
	 * @param referencedFrom
	 *            The ClassType where this type is referenced from
	 * 
	 * @return The type found
	 * 
	 * @throws AnalysisException
	 *             if no match is found
	 */
	public UserDefinedType getType(final String name, final ClassType referencedFrom) {

		{
			final UserDefinedType t = m_typeLookupFullName.get(name);
			if (t != null)
				return t;
		}

		final List<UserDefinedType> matches = m_typeLookupShortName.get(name);
		if (matches != null && !matches.isEmpty()) {
			if (matches.size() == 1) {
				return matches.get(0);
			} else {
				for (final UserDefinedType t : matches) {
					if (t.module() == referencedFrom.module()) {
						return t;
					}
				}
				throw new AnalysisException("Ambigously referenced type " + name + " from class "
						+ referencedFrom + ". Candidates are " + matches);
			}
		}

		throw new AnalysisException("Could not find any matching type named " + name
				+ ", as referenced from class " + referencedFrom.fullName());
	}

	/**
	 * Looks up a static class constant by name. The supplied name can be either
	 * the fully qualified constant name (se.culvertsoft.MyClass.MY_CONSTANT),
	 * the short qualified name (MyClass.MY_CONSTANT) or just the short constant
	 * name (MY_CONSTANT).
	 * 
	 * If multiple constants match a short name or short qualified name an
	 * AnalysisException will be thrown if none of them are declared in the same
	 * module as the parameter referencedFrom.
	 * 
	 * If no constant is found this function throws an AnalysisException.
	 * 
	 * 
	 * @param name
	 *            The name of the constant to find
	 * 
	 * @param referencedFrom
	 *            The ClassType where this constant is referenced from
	 * 
	 * @return The Constant found
	 * 
	 * @throws AnalysisException
	 *             if no match is found
	 */
	public Constant getConstant(final String name, final ClassType referencedFrom) {

		{
			final Constant c = m_constantLookupFullName.get(name);
			if (c != null)
				return c;
		}

		{
			final List<Constant> matches = m_constantLookupQualShortName.get(name);
			if (matches != null && !matches.isEmpty()) {
				if (matches.size() == 1) {
					return matches.get(0);
				} else {
					for (final Constant c : matches) {
						if (c.parent() == referencedFrom) {
							return c;
						}
					}
					throw new AnalysisException("Ambigously referenced constant " + name
							+ " from class " + referencedFrom + ". Candidates are " + matches);
				}
			}
		}

		{
			final List<Constant> matches = m_constantLookupShortName.get(name);
			if (matches != null && !matches.isEmpty()) {
				if (matches.size() == 1) {
					return matches.get(0);
				} else {
					for (final Constant c : matches) {
						if (c.parent() == referencedFrom) {
							return c;
						}
					}
					throw new AnalysisException("Ambigously referenced constant " + name
							+ " from class " + referencedFrom + ". Candidates are " + matches);
				}
			}
		}

		throw new AnalysisException("Could not find any matching constant named " + name
				+ ", as referenced from class " + referencedFrom.fullName());

	}

	/**
	 * Gets all the classes defined within this project.
	 * 
	 * @return All classes defined within this project
	 */
	public List<ClassType> getClasses() {
		return m_classes;
	}

	/**
	 * Gets all the enumerations defined within this project.
	 * 
	 * @return All the enumerations defined within this project.
	 */
	public List<EnumType> getEnums() {
		return m_enums;
	}

	public ItemLookup(final Iterable<UserDefinedType> types, final Iterable<Constant> constants) {
		for (final UserDefinedType t : types) {
			m_typeLookupFullName.put(t.fullName(), t);
			getOrCreateList(m_typeLookupShortName, t.shortName()).add(t);
			switch (t.typeEnum()) {
			case CLASS:
				m_classes.add((ClassType) t);
				break;
			case ENUM:
				m_enums.add((EnumType) t);
				break;
			default:
				break;
			}
		}
		for (final Constant c : constants) {
			m_constantLookupFullName.put(c.fullName(), c);
			getOrCreateList(m_constantLookupQualShortName, c.qualifiedShortName()).add(c);
			getOrCreateList(m_constantLookupShortName, c.shortName()).add(c);
		}
	}

	private <T> List<T> getOrCreateList(final Map<String, List<T>> map, final String key) {

		if (map.containsKey(key))
			return map.get(key);

		final List<T> newList = new ArrayList<T>();
		map.put(key, newList);
		return newList;
	}

	private final List<ClassType> m_classes = new ArrayList<ClassType>();
	private final List<EnumType> m_enums = new ArrayList<EnumType>();

	private final Map<String, List<UserDefinedType>> m_typeLookupShortName = new LinkedHashMap<String, List<UserDefinedType>>();
	private final Map<String, UserDefinedType> m_typeLookupFullName = new LinkedHashMap<String, UserDefinedType>();

	private final Map<String, List<Constant>> m_constantLookupShortName = new LinkedHashMap<String, List<Constant>>();
	private final Map<String, List<Constant>> m_constantLookupQualShortName = new LinkedHashMap<String, List<Constant>>();
	private final Map<String, Constant> m_constantLookupFullName = new LinkedHashMap<String, Constant>();

}
