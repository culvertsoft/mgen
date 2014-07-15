#ifndef MGEN_OBJECT_RANDOMIZER_H_
#define MGEN_OBJECT_RANDOMIZER_H_

#include "mgen/classes/MGenBase.h"

namespace mgen {

template<typename ClassRegType>
class ObjectRandomizer {
public:

    ObjectRandomizer(const ClassRegType& classRegistry, const int maxDepth = 3) :
                    m_classReg(classRegistry),
                    m_maxDepth(maxDepth) {
    }

    virtual ~ObjectRandomizer() {
    }

    void randomize(mgen::MGenBase& object) {
        m_classReg.visitObject(object, *this);
    }

    template<typename MGenType>
    void beginVisit(MGenType& object, const int nFieldsSet, const int nFieldsTotal) {
        m_depth++;
    }

    template<typename T>
    void visit(T& v, const Field& field, const bool isSet) {
        // TODO implementation
    }

    void endVisit() {
        m_depth--;
    }

private:
    ClassRegType m_classReg;
    int m_maxDepth;
    int m_depth;

};

}

#endif
