/*
 * EmptyClassRegistry.h
 *
 *  Created on: 10 mar 2014
 *      Author: GiGurra
 */

#ifndef EMPTYCLASSREGISTRY_H_
#define EMPTYCLASSREGISTRY_H_

#include "mgen/classes/ClassRegistryBase.h"
#include "mgen/exceptions/Exception.h"

namespace mgen {

class EmptyClassRegistry: public ClassRegistryBase {
public:
    EmptyClassRegistry() {}

    template<typename ContextType, typename ReaderType>
    void readObjectFields(mgen::MGenBase& o, ContextType& /* context */, ReaderType& /*reader*/) const {
        switch (o._typeHash16bit()) {
        case 0: // To make VC stop warning about no case
        default: // should not happen...INCORRECT USAGE!
            throw mgen::Exception(
                    std::string(
                            "gameworld::types::ClassRegistry::readObjectFields: Incorrect usage. Class '").append(
                            o._typeName()).append(" not registered."));
        }
    }

    template<typename VisitorType>
    void visitObject(mgen::MGenBase& o, VisitorType& /*visitor*/) const {
        switch (o._typeHash16bit()) {
        case 0: // To make VC stop warning about no case
        default: // should not happen...INCORRECT USAGE!
            throw mgen::Exception(
                    std::string(
                            "gameworld::types::ClassRegistry::visitObject: Incorrect usage. Class '").append(
                            o._typeName()).append(" not registered."));
        }
    }

    template<typename VisitorType>
    void visitObject(const mgen::MGenBase& o, VisitorType& /*visitor*/) const {
        switch (o._typeHash16bit()) {
        case 0: // To make VC stop warning about no case
        default: // should not happen...INCORRECT USAGE!
            throw mgen::Exception(
                    std::string(
                            "gameworld::types::ClassRegistry::visitObject: Incorrect usage. Class '").append(
                            o._typeName()).append(" not registered."));
        }
    }

};

} /* namespace mgen */

#endif /* EMPTYCLASSREGISTRY_H_ */
