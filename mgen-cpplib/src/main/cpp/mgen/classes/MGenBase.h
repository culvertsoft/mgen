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

    // Keep flat or wrap with some 'Type' object?
    virtual const short _typeHash16bit() const = 0;
    virtual const int _typeHash32bit() const = 0;
    virtual const std::string& _typeName() const = 0;
    virtual const std::string& _typeHash16bitBase64() const = 0;
    virtual const std::string& _typeHash32bitBase64() const = 0;

    // Keep flat or wrap with some 'Type' objects?
    virtual const std::vector<short>& _typeHashes16bit() const = 0;
    virtual const std::vector<int>& _typeHashes32bit() const = 0;
    virtual const std::vector<std::string>& _typeNames() const = 0;
    virtual const std::vector<std::string>& _typeHashes16bitBase64() const = 0;
    virtual const std::vector<std::string>& _typeHashes32bitBase64() const = 0;

    /************************************************************************
     *
     *
     * - - - - - - - - - - FIELD DATA EXTRACTORS/SETTERS - - - - - - - - -
     *
     * **********************************************************************/

    virtual const std::vector<mgen::Field>& _fields() const = 0;

    virtual const mgen::Field * _fieldBy16BitHash(const short hash) const = 0;

    virtual const mgen::Field * _fieldBy32BitHash(const int hash) const = 0;

    virtual const mgen::Field * _fieldByName(const std::string& name) const = 0;

    virtual int _nFieldsSet(const FieldSetDepth depth) const = 0;

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
