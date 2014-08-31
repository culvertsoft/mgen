package se.culvertsoft.mgen.javapack.classes;

import java.util.Collection;
import java.util.HashMap;

/**
 * Base class for all MGen class registries. When generating classes for an MGen
 * project, a class registry is also generated. The class registry provides
 * class lookups from names and type ids.
 */
public abstract class ClassRegistryBase {

	/**
	 * Gets a class registry entry by 64 bit type id.
	 * 
	 * @param typeId
	 *            The 64 bit type id of the class to find
	 * 
	 * @return The found entry, or null
	 */
	public ClassRegistryEntry getById(final long typeId) {
		return m_typeId2Entry.get(typeId);
	}

	/**
	 * Gets a class registry entry by qualified class name
	 * (name.space.ClassName).
	 * 
	 * @param fullClassName
	 *            The fully qualified class name of the class to find
	 * 
	 * @return The found entry, or null
	 */
	public ClassRegistryEntry getByName(final String fullClassName) {
		return m_name2Entry.get(fullClassName);
	}

	/**
	 * Gets class registry entry by java class object.
	 * 
	 * @param cls
	 *            The java class that the requested entry represents
	 * 
	 * @return The found entry, or null
	 */
	public ClassRegistryEntry getByClass(final Class<?> cls) {
		return m_cls2Entry.get(cls);
	}

	/**
	 * Returns the internal lookup-table from 64 bit type ids to class registry
	 * entries. This can be used to iterate over registered classes, especially
	 * useful for testing.
	 * 
	 * @return The internal lookup-table from 64 bit type ids to class registry
	 *         entries
	 */
	public Collection<ClassRegistryEntry> entries() {
		return m_name2Entry.values();
	}

	/**
	 * Gets a class registry entry by 16 bit type ids. This method is entirely
	 * implemented in generated class registries.
	 * 
	 * @param ids
	 *            The 16 bit type ids of the requested type
	 * 
	 * @return The found entry, or null
	 */
	public abstract ClassRegistryEntry getByTypeIds16Bit(final short[] ids);

	/**
	 * Gets a class registry entry by 16 bit type ids, in base 64 format. This
	 * method is entirely implemented in generated class registries.
	 * 
	 * @param ids
	 *            The 16 bit type ids in base 64 format of the requested type
	 * 
	 * @return The found entry, or null
	 */
	public abstract ClassRegistryEntry getByTypeIds16BitBase64(
			final String[] ids);

	/**
	 * Constructor to be used only by generated subclasses/generated
	 * ClassRegistry classes.
	 */
	protected ClassRegistryBase() {
		m_typeId2Entry = new HashMap<Long, ClassRegistryEntry>();
		m_name2Entry = new HashMap<String, ClassRegistryEntry>();
		m_cls2Entry = new HashMap<Class<?>, ClassRegistryEntry>();
	}

	/**
	 * Function for adding a new entry to the lookup tables. Only to be used by
	 * generated subclasses/generated ClassRegistry classes.
	 * 
	 * @param entry
	 *            The ClassRegistryEntry to add
	 */
	protected void add(final ClassRegistryEntry entry) {
		m_typeId2Entry.put(entry.typeId(), entry);
		m_name2Entry.put(entry.clsName(), entry);
		m_cls2Entry.put(entry.cls(), entry);
	}

	private final HashMap<Long, ClassRegistryEntry> m_typeId2Entry;
	private final HashMap<String, ClassRegistryEntry> m_name2Entry;
	private final HashMap<Class<?>, ClassRegistryEntry> m_cls2Entry;

}
