/*
 * Polymorphic.h
 *
 *  Created on: 4 mar 2014
 *      Author: GiGurra
 */

#ifndef MGEN_POLYMORPHIC_H_
#define MGEN_POLYMORPHIC_H_

namespace mgen {

template<typename T>
class Polymorphic {
public:

    Polymorphic(T * v = 0, const bool managed = true) :
            m_ptr(v), m_managed(managed) {
    }

    Polymorphic(const Polymorphic& v) :
            m_ptr(v.deepCopy()), m_managed(true) {
    }

    template<typename T2>
    Polymorphic(const Polymorphic<T2>& v) :
            m_ptr(v.deepCopy()), m_managed(true) {
    }

    Polymorphic& operator=(const Polymorphic& v) {
        return this->operator =<T>(v);
    }

    Polymorphic& operator=(T* v) {
        return set<T>(v);
    }

    Polymorphic& operator=(const T* v) {
        return set<T>(v ? v._deepCopy() : 0);
    }

    template<typename T2>
    Polymorphic& operator=(const Polymorphic<T2>& v) {
        return set<T2>(v.deepCopy());
    }

    template<typename T2>
    Polymorphic& set(T2 * v, const bool managed = true) {
        if (m_ptr && m_managed && v != m_ptr) {
            delete m_ptr;
            m_ptr = 0;
        }
        m_ptr = v;
        m_managed = managed;
        return *this;
    }

    Polymorphic& set(T * v, const bool managed = true) {
        return set<T>(v, managed);
    }

    template<typename T2>
    bool operator==(const Polymorphic<T2>& v) const {
        return v.get() ? (m_ptr == v.get() || equals(*v)) : !m_ptr;
    }

    bool operator==(const T& v) const {
        return m_ptr && m_ptr->equals(v);
    }

    ~Polymorphic() {
        if (m_managed && m_ptr) {
            delete m_ptr;
            m_ptr = 0;
        }
    }

    T * operator->() {
        return m_ptr;
    }

    T* deepCopy() const {
        return m_ptr ? (T*) m_ptr->_deepCopy() : 0;
    }

    const T * operator->() const {
        return m_ptr;
    }

    T& operator*() {
        return *m_ptr;
    }

    const T& operator*() const {
        return *m_ptr;
    }

    const T * get() const {
        return m_ptr;
    }

    bool isManaged() const {
        return m_managed;
    }

    T * detach() {
        m_managed = false;
        return m_ptr;
    }

    T * get() {
        return m_ptr;
    }

    void ensureIsSet(const bool state) {
        if (!state) {
            set(0);
        } else if (!m_ptr) {
            set(new T);
        }
    }

private:
    T * m_ptr;
    bool m_managed;

    bool equals(const T& other) const {
        if (m_ptr) {
            return m_ptr->_equals(other);
        } else {
            return false;
        }
    }

};

template<typename T>
bool operator==(const T& a, const mgen::Polymorphic<T>& b) {
    return b == a;
}

}

#endif /* MGEN_POLYMORPHIC_H_ */
