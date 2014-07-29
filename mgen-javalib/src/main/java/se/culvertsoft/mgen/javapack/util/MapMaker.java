package se.culvertsoft.mgen.javapack.util;

import java.util.HashMap;

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
