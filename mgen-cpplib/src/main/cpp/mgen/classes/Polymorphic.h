/*
 * Polymorphic.h
 *
 *  Created on: 4 mar 2014
 *      Author: GiGurra
 */

#ifndef MGEN_POLYMORPHIC_H_
#define MGEN_POLYMORPHIC_H_

namespace mgen {

/**
 * By-value polymorphic reference container and memory manager. 
 * Used by generated classes for fields flagged 'polymorphic'. This class
 * ensures no manual memory management is necessary unless explicitly requested.
 *
 * This class does NOT implement and form of resource sharing. A copy construction 
 * of a Polymorphic<T> WILL trigger a deep copy of the contents within - so it does NOT
 * act like a shared_ptr or require special thread management.
 */
template<typename T>
class Polymorphic {
public:

    /**
     * Constructs a new Polymorphic wrapper around the given pointer. 
     */
    explicit Polymorphic(T * v, const bool managed) :
                    m_ptr(v),
                    m_managed(managed) {
    }

    /**
     * Constructs a new Polymorphic wrapper around the given pointer. 
     */
    explicit Polymorphic(T * v) :
        m_ptr(v),
        m_managed(true) {
    }

    /**
     * Constructs a new Polymorphic wrapper around the given pointer. 
     */
    explicit Polymorphic() :
        m_ptr(0),
        m_managed(true) {
    }

    /**
     * Copy constructor which guarantees that the source object is deeply copied.
     */
    Polymorphic(const Polymorphic& v) :
                    m_ptr(v.deepCopy()),
                    m_managed(true) {
    }

    /**
     * Copy constructor which guarantees that the source object is deeply copied.
     */
    template<typename T2>
    Polymorphic(const Polymorphic<T2>& v) :
                    m_ptr(v.deepCopy()),
                    m_managed(true) {
    }

    /**
     * Assignment operator which guarantees that the source object is deeply copied.
     * Destructs and previously managed resource.
     */
    Polymorphic& operator=(const Polymorphic& v) {
        return this->operator =<T>(v);
    }

    /**
     * Assignment operator which guarantees that the source object is deeply copied. 
     * Destructs and previously managed resource.
     */
    template<typename T2>
    Polymorphic& operator=(const Polymorphic<T2>& v) {
        return set<T2>(v.deepCopy());
    }

    /**
     * Assignment operator which wraps the provided pointer without copying. Destructs and
     * previously managed resource.
     */
    Polymorphic& operator=(T* v) {
        return set<T>(v);
    }

    /**
     * Assignment which wraps the provided pointer without copying. Destructs and
     * previously managed resource.
     */
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

    /**
     * Assignment which wraps the provided pointer without copying. Destructs and
     * previously managed resource.
     */
    Polymorphic& set(T * v, const bool managed = true) {
        return set<T>(v, managed);
    }

    /**
     * Comparison operator which deeply and polymorphicly checks for equality.
     */
    template<typename T2>
    bool operator==(const Polymorphic<T2>& v) const {
        return v.get() ? (m_ptr == v.get() || equals(*v)) : !m_ptr;
    }

    /**
     * Comparison operator which deeply and polymorphicly checks for equality.
     */
    bool operator==(const T& v) const {
        return m_ptr && m_ptr->equals(v);
    }

    /**
     * Destroys any managed resource.
     */
    ~Polymorphic() {
        if (m_managed && m_ptr) {
            delete m_ptr;
            m_ptr = 0;
        }
    }

    /**
     * Deeply and polymorphicly copies the resource contained within. The copy 
     * will be heap allocated and will need to be managed and deleted eventually.
     */
    T* deepCopy() const {
        return m_ptr ? (T*) m_ptr->_deepCopy() : 0;
    }
    
    /**
     * Convenience overload of -> operator to be able to use this object
     * with simple pointer syntax.
     */
    T * operator->() {
        return m_ptr;
    }

    /**
     * Convenience overload of -> operator to be able to use this object
     * with simple pointer syntax.
     */
    const T * operator->() const {
        return m_ptr;
    }

    /**
     * Convenience overload of * operator to be able to use this object
     * with simple pointer syntax.
     */
    T& operator*() {
        return *m_ptr;
    }

    /**
     * Convenience overload of * operator to be able to use this object
     * with simple pointer syntax.
     */
    const T& operator*() const {
        return *m_ptr;
    }

    /**
     * Returns the pointer contained.
     */
    T * get() {
        return m_ptr;
    }
    
    /**
     * Returns the pointer contained.
     */
    const T * get() const {
        return m_ptr;
    }

    /**
     * Returns if the pointer contained will be deleted when this Polymorphic is destructed.
     */
    bool isManaged() const {
        return m_managed;
    }

    /**
     * Stops this Polymorphic object from being responsible for destructing the pointer 
     * contained within. Not used by generated classes but may be manually used when 
     * preserving resources without copying is explicitly required. You will need to manage
     * the resource manually.
     */
    T * detach() {
        m_managed = false;
        return m_ptr;
    }

    /**
     * Convenience method which will either release the pointer within or set it to a new
     * object instance, depending on the provided state parameter.
     */
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
