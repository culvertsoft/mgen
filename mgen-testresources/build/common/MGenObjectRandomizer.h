#ifndef MGEN_OBJECT_RANDOMIZER_H_
#define MGEN_OBJECT_RANDOMIZER_H_

#include "mgen/classes/MGenBase.h"

namespace mgen {

template<typename ClassRegType>
class ObjectRandomizer {
public:

    ObjectRandomizer(
            const ClassRegType& classRegistry,
            const int maxListSize = 16,
            const int maxStringSize = 8,
            const int maxDepth = 3) :
                    m_classReg(classRegistry),
                    m_maxListSize(maxListSize),
                    m_maxStringSize(maxStringSize),
                    m_maxDepth(maxDepth),
                    m_depth(0),
                    m_randomInt(0) {
    }

    virtual ~ObjectRandomizer() {
    }

    void randomizeObject(mgen::MGenBase& object) {
        m_depth = 0;
        m_classReg.visitObject(object, *this);
    }

    template<typename MGenType>
    void beginVisit(MGenType& object, const int nFieldsSet, const int nFieldsTotal) {
    }

    template<typename T>
    void visit(T& v, const Field& field, const bool isSet) {
        if (m_depth < m_maxDepth || field.isRequired()) {
            m_depth++;
            randomize(v);
            m_depth--;
        }
    }

    void endVisit() {
    }

private:

    int rand() {
        return m_randomInt++;
    }

    int rand(const int max) {
        return rand() % max;
    }

    void randomizePoly(MGenBase& v) {
        m_classReg.visitObject(v, *this);
    }

    template<typename MGenType>
    void randomize(MGenType& v, const MGenBase& /* type_evidence */) {
        v._accept(*this);
    }

    template<typename EnumType>
    void randomize(EnumType& v, const int /* type_evidence */) {
        const std::vector<EnumType>& values = get_enum_values(v);
        v = values[rand(values.size())];
    }

    template<typename MGenTypeOrEnum>
    void randomize(MGenTypeOrEnum& v) {
        randomize(v, v);
    }

    template<typename MGenType>
    void randomize(Polymorphic<MGenType>& v) {
        v.ensureIsSet(true);
        randomizePoly(*v);
    }

    template<typename T>
    void randomize(std::vector<T>& v) {
        if (m_depth < m_maxDepth) {
            m_depth++;
            for (int i = 0; i < m_maxListSize; i++) {
                T t;
                randomize(t);
                v.push_back(t);
            }
            m_depth--;
        }
    }

    template<typename K, typename V>
    void randomize(std::map<K, V>& v) {
        if (m_depth < m_maxDepth) {
            m_depth++;
            for (int i = 0; i < m_maxListSize; i++) {
                K key;
                V value;
                randomize(key);
                randomize(value);
                v[key] = value;
            }
            m_depth--;
        }
    }

    void randomize(bool& v) {
        v = rand(1000) % 2 == 0;
    }

    void randomize(char& v) {
        v = rand(1000) - 500;
    }

    void randomize(short& v) {
        v = rand(100000) - 50000;
    }

    void randomize(int& v) {
        v = rand();
    }

    void randomize(long long& v) {
        v = rand();
    }

    void randomize(float& v) {
        v = 0.0f; // float(rand()) * 0.135;
    }

    void randomize(double& v) {
        v = 0.0f; // = double(rand()) * 0.135;
    }

    void randomize(std::string& v) {
        const int sz = rand(m_maxStringSize) + 1;
        const int range = 122-97;
        v.resize(sz);
        for (int i = 0; i < sz; i++) {
            v[i] = rand(25) + 97;
        }
    }

    ClassRegType m_classReg;
    int m_maxListSize;
    int m_maxStringSize;
    int m_maxDepth;
    int m_depth;
    int m_randomInt;

};

}

#endif
