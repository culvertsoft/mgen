/*
 * EmptyClassRegistry.h
 *
 *  Created on: 10 mar 2014
 *      Author: GiGurra
 */

#ifndef MGEN_EMPTYCLASSREGISTRY_H_
#define MGEN_EMPTYCLASSREGISTRY_H_

#include "mgen/classes/ClassRegistryBase.h"
#include "mgen/exceptions/Exception.h"

namespace mgen {

/**
 * Represents an empty class registry. Used for demo and testing purposes.
 */
class EmptyClassRegistry: public ClassRegistryBase {
public:
    EmptyClassRegistry() {}

    template<typename ContextType, typename ReaderType>
    void readObjectFields(mgen::MGenBase& o, ContextType& /* context */, ReaderType& /*reader*/) const {
        throw mgen::Exception("Cannot call readObjectFields(..) on EmptyClassRegistry");
    }

    template<typename VisitorType>
    void visitObject(mgen::MGenBase& o, VisitorType& /*visitor*/) const {
        throw mgen::Exception("Cannot call visitObject(..) on EmptyClassRegistry");
    }

    template<typename VisitorType>
    void visitObject(const mgen::MGenBase& o, VisitorType& /*visitor*/) const {
        throw mgen::Exception("Cannot call visitObject(..) on EmptyClassRegistry");
    }

    const ClassRegistryEntry * getByIds(const std::vector<short>& typeIds16bit) const { return 0; }
    const ClassRegistryEntry * getByIds(const std::vector<std::string>& typeIds16bitBase64) const { return 0; }

};

} /* namespace mgen */

#endif /* MGEN_EMPTYCLASSREGISTRY_H_ */
