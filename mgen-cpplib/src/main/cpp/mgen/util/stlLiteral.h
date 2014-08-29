#ifndef MGEN_STL_LITERAL_H_
#define MGEN_STL_LITERAL_H_

#include <vector>
#include <map>

namespace mgen {

/**
 * Utility class for creating std::vectors in one line.
 */
template<typename T>
class make_vector {
public:

    make_vector() {
    }

    make_vector(const int n) {
        m_data.reserve(n);
    }

    make_vector& operator<<(const T& val) {
        m_data.push_back(val);
        return *this;
    }

    make_vector& add(const T& val) {
        m_data.push_back(val);
        return *this;
    }

    operator std::vector<T>() const {
        return m_data;
    }

    std::vector<T> make() const {
        return m_data;
    }

private:
    std::vector<T> m_data;
};

/**
 * Utility class for creating std::maps in one line.
 */
template<typename K, typename V>
class make_map {
public:

    make_map& operator()(const K& key, const V& value) {
        m_data[key] = value;
        return *this;
    }

    make_map& put(const K& key, const V& value) {
        m_data[key] = value;
        return *this;
    }

    operator std::map<K,V>() const {
        return m_data;
    }

    std::map<K, V> make() const {
        return m_data;
    }

private:
    std::map<K, V> m_data;
};

}

#endif /* MGEN_STL_LITERAL_H_ */
