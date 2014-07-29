package se.culvertsoft.mgen.javapack.util;

import java.util.ArrayList;

public class ListMaker<T> {

	public ListMaker(final int n) {
		m_list = new ArrayList<T>(n);
	}

	public ListMaker<T> add(final T v) {
		m_list.add(v);
		return this;
	}

	public ArrayList<T> make() {
		return m_list;
	}

	private final ArrayList<T> m_list;

}
