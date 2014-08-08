package se.culvertsoft.mgen.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A module represents a set of types, similar to a java package or c++
 * namespace.
 */
public class Module {

	/**
	 * Retrieves the settings map for this module. The settings are created
	 * from: <command line args> + <project file settings> + <module file
	 * settings>
	 * 
	 * @return The settings for this module
	 */
	public Map<String, String> settings() {
		return m_settings;
	}

	/**
	 * Returns the path of this module, i.e. the java package or c++ namespace
	 * of this module.
	 * 
	 * @return The module path of this module.
	 */
	public String path() {
		return m_path;
	}

	/**
	 * The written file path used to reference this module in its parent project
	 * file.
	 * 
	 * @return The written file path to this module. Returns null if not
	 *         applicable.
	 */
	public String filePath() {
		return m_filePath;
	}

	/**
	 * The absolute file path calculated from \filePath().
	 * 
	 * @return The absolute file path to this module. Returns null if not
	 *         applicable.
	 */
	public String absoluteFilePath() {
		return m_absoluteFilePath;
	}

	/**
	 * Returns the classes defined within this module.
	 * 
	 * @return The classes defined within this module.
	 */
	public List<ClassType> classes() {
		return m_classes;
	}

	/**
	 * Returns the enumerations defined within this module.
	 * 
	 * @return The enumerations defined within this module.
	 */
	public List<EnumType> enums() {
		return m_enums;
	}

	/**
	 * Returns the parent project of this module.
	 * 
	 * @return The parent project of this module.
	 */
	public Project parent() {
		return m_parent;
	}

	/**
	 * Replaces the classes of this module.
	 */
	public void setClasses(final List<ClassType> classes) {
		m_classes.clear();
		addClasses(classes);
	}

	/**
	 * Adds classes to this module.
	 */
	public void addClasses(final List<ClassType> classes) {
		m_classes.addAll(classes);
	}

	/**
	 * Adds a class to this module.
	 */
	public void addClass(final ClassType c) {
		m_classes.add(c);
	}

	/**
	 * Replaces the enums of this module.
	 */
	public void setEnums(final List<EnumType> enums) {
		m_enums.clear();
		addEnums(enums);
	}

	/**
	 * Adds new enums to this module.
	 */
	public void addEnums(final List<EnumType> enums) {
		m_enums.addAll(enums);
	}

	/**
	 * Add a new enum to this module.
	 */
	public void addEnum(final EnumType e) {
		m_enums.add(e);
	}

	public Module(
			final String path,
			final String filePath,
			final String absoluteFilePath,
			final Map<String, String> settings,
			final Project parent) {
		m_path = path;
		m_filePath = filePath;
		m_absoluteFilePath = absoluteFilePath;
		m_settings = settings;
		m_classes = new ArrayList<ClassType>();
		m_enums = new ArrayList<EnumType>();
		m_parent = parent;
	}

	private final String m_path;
	private final String m_filePath;
	private final String m_absoluteFilePath;
	private final Map<String, String> m_settings;
	private final ArrayList<ClassType> m_classes;
	private final ArrayList<EnumType> m_enums;
	private final Project m_parent;

}
