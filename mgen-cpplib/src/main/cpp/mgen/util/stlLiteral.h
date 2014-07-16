#ifndef MGEN_STL_LITERAL_H_
#define MGEN_STL_LITERAL_H_

#include <vector>
#include <map>

namespace mgen {

template <typename T>
class make_vector {
public:
	make_vector& operator<<(const T& val) {
		m_data.push_back(val);
		return *this;
	}
	
	operator std::vector<T>() const {
		return m_data;
	}
	
private:
	std::vector<T> m_data;
};

template <typename K, typename V>
class make_map {
public:
	make_map& operator()(const K& key, const V& value) {
		m_data[key] = value;
		return *this;
	}
	
	operator std::map<K,V>() const {
		return m_data;
	}
	
private:
	std::map<K,V> m_data;
};

}

#endif /* MGEN_STL_LITERAL_H_ */
