/*
 * MGenBase.h
 *
 *  Created on: Mar 2, 2014
 *      Author: johan
 */

#ifndef MGENBASE_H_
#define MGENBASE_H_

#include "mgen/metadata/Field.h"
#include "mgen/metadata/FieldSetDepth.h"
#include "mgen/util/hash.h"

namespace mgen {

class MGenBase {
public:

    /************************************************************************
     *
     *
     * - - - - - - - - - - NORMAL METHODS - - - - - - - - - - -
     *
     * **********************************************************************/

    MGenBase() {}
    virtual ~MGenBase() {}

    // Future release: std::string toString() const = 0;
    // Future release: int hashCode() const = 0;

    /************************************************************************
     *
     *
     * - - - - - - - - - - FOR IDENTIFICATION - - - - - - - - - - -
     *
     * **********************************************************************/

    virtual const long long _typeId() const = 0;
    virtual const short _typeId16Bit() const = 0;
    virtual const std::string& _typeId16BitBase64() const = 0;
    virtual const std::string& _typeName() const = 0;

    virtual const std::vector<long long>& _typeIds() const = 0;
    virtual const std::vector<short>& _typeIds16Bit() const = 0;
    virtual const std::vector<std::string>& _typeIds16BitBase64() const = 0;
    virtual const std::string& _typeIds16BitBase64String() const = 0;
    virtual const std::vector<std::string>& _typeNames() const = 0;

    /************************************************************************
     *
     *
     * - - - - - - - - - - OTHER META METHODS - - - - - - - - -
     *
     * **********************************************************************/

    virtual const std::vector<mgen::Field>& _fieldMetadatas() const = 0;

    virtual const mgen::Field * _fieldById(const short fieldId) const = 0;

    virtual const mgen::Field * _fieldByName(const std::string& name) const = 0;

    virtual int _numFieldsSet(const FieldSetDepth depth) const = 0;

    virtual bool _isFieldSet(const mgen::Field& field, const FieldSetDepth depth) const = 0;

    virtual const MGenBase& _setAllFieldsSet(const bool state, const FieldSetDepth depth) = 0;

    virtual bool _equals(const mgen::MGenBase& other) const = 0;

    virtual MGenBase * _deepCopy() const = 0;

    virtual bool _validate(const FieldSetDepth depth) const = 0;

    /**
     *
     * Subtypes are generated with the following additional methods:
     *
     *  template <typename FieldVisitorType>
     *  void _accept(FieldVisitor& visitor);
     *
     *  template <typename FieldVisitorType>
     *  void _accept(FieldVisitor& visitor) const;
     *
     *  template<typename ReaderType, typename ReadContextType>
     *  void _readField(const mgen::Field& field, ReadContextType& context, ReaderType& reader);
     *
     *  --> These methods may be dynamically invoked by readers/writers through the generated ClassRegistry
     *
     *  static mgen::MGenBase * _newInstance(); // For class readers (using ClassRegistries) to create new objects
     *
     */

};

} /* namespace mgen */

#endif /* MGENBASE_H_ */
