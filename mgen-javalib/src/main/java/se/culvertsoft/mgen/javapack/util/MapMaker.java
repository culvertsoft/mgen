package se.culvertsoft.mgen.javapack.util;

import java.util.HashMap;

/**
 * Internal utility class for creating maps with contents in one line. Intended
 * to be used only in generated code.
 * 
 * @param <K>
 *            The key type of the map to be made
 * 
 * @param <V>
 *            The value type of the map to be made
 */
public class MapMaker<K, V> {

	public MapMaker(final int n) {
		m_map = new HashMap<K, V>(n);
	}

	public MapMaker<K, V> put(final K key, final V value) {
		m_map.put(key, value);
		return this;
	}

	public HashMap<K, V> make() {
		return m_map;
	}

	private final HashMap<K, V> m_map;

}
