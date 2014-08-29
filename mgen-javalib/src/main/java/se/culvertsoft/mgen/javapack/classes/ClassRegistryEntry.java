package se.culvertsoft.mgen.javapack.classes;

import se.culvertsoft.mgen.api.model.RuntimeClassType;

/**
 * A ClassRegistryEntry represents a way to get information about a generated
 * class. It also provides a generic way for creating object instances of that
 * class.
 * 
 * Generally speaking, ClassRegistryEntry instances are only intended to be
 * created by generated code.
 */
public class ClassRegistryEntry {

	/**
	 * Creates a new ClassRegistryEntry for a generated class.
	 * 
	 * @param typeId
	 *            The 64 bit type id of the class
	 * @param typeIds
	 *            The 64 bit type ids of the class
	 * @param className
	 *            The fully qualified name of the class
	 * @param ctor
	 *            An interface for default constructing new instances of the
	 *            class
	 */
	public ClassRegistryEntry(
			final long typeId,
			final long[] typeIds,
			final String className,
			final Ctor ctor) {
		m_cls = lkupClass(className);
		m_typeId = typeId;
		m_typeIds = typeIds;
		m_clsName = className;
		m_ctor = ctor;
		m_type = new RuntimeClassType(m_clsName, typeId);
	}

	/**
	 * Calls the registered default construction interfaces and returns the new
	 * instance.
	 * 
	 * @return A new object instance of the class that this entry represents.
	 */
	public MGenBase construct() {
		return m_ctor.create();
	}

	/**
	 * A method for getting the 64 bit type id of this entry.
	 * 
	 * @return The 64 bit type id of this entry.
	 */
	public long typeId() {
		return m_typeId;
	}

	/**
	 * A method for getting the 64 bit type ids of this entry's class hierarchy.
	 * 
	 * @return The 64 bit type ids of this entry's class hierarchy.
	 */
	public long[] typeIds() {
		return m_typeIds;
	}

	/**
	 * A method for getting the qualified class name entry.
	 * 
	 * @return The qualified class name entry.
	 */
	public String clsName() {
		return m_clsName;
	}

	/**
	 * A method for getting the default construction interface for this entry.
	 * 
	 * @return The default construction interface for this entry.
	 */
	public Ctor ctor() {
		return m_ctor;
	}

	/**
	 * A method for getting the class of a ClassRegistryEntry.
	 * 
	 * @return The java Class object of the class that this ClassRegistryEntry
	 *         represents.
	 */
	public Class<?> cls() {
		return m_cls;
	}

	/**
	 * A method for getting runtime meta data information on a
	 * ClassRegistryEntry.
	 * 
	 * @return Runtime meta data information on a ClassRegistryEntry
	 */
	public RuntimeClassType typ() {
		return m_type;
	}

	/**
	 * A way to determine if this ClassRegistryEntry represents a sub class of
	 * the provided 64 bit type id. This is useful when reading objects from
	 * streams and wanting to verify that the read objects are of the correct
	 * type to match the field they are being read to.
	 * 
	 * @param typeId
	 *            The 64 bit type id of the potential base class
	 * 
	 * @return If this class is the same or a subclass as that represented by
	 *         typeId.
	 */
	public boolean isInstanceOfTypeId(final long typeId) {
		for (int i = m_typeIds.length - 1; i >= 0; i--) {
			if (m_typeIds[i] == typeId) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Internal convenience method for finding a class by name without checked
	 * exceptions.
	 * 
	 * @param className
	 *            The fully qualified name of the class.
	 * 
	 * @return The class
	 */
	private static Class<?> lkupClass(final String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Failed to init class " + className, e);
		}
	}

	private final Class<?> m_cls;
	private final long m_typeId;
	private final long[] m_typeIds;
	private final String m_clsName;
	private final Ctor m_ctor;
	private final RuntimeClassType m_type;

}