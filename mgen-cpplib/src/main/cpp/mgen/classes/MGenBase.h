/*
 * MGenBase.h
 *
 *  Created on: Mar 2, 2014
 *      Author: johan
 */

#ifndef MGEN_MGENBASE_H_
#define MGEN_MGENBASE_H_

#include "mgen/metadata/Field.h"

namespace mgen {

/**
 * MGenBase is the base class of all classes generated with MGen.
 * This base class provides the interface methods required to facilitate
 * object visitation, dynamic double dispatch and serialization.
 */
class MGenBase {
public:


    /**
     * Gets the 64 bit type id of this class.
     */
    virtual const long long _typeId() const = 0;
    
    /**
     * Gets the 16 bit type id of this class. 16 bit type ids
     * are only guaranteed to be unique among classes with the same
     * super type.
     */
    virtual const short _typeId16Bit() const = 0;
    
    /**
     * Gets the base64 representation of the 16 bit type id of this class. 
     */
    virtual const std::string& _typeId16BitBase64() const = 0;
    
    /**
     * Gets the qualified type name of this class (name.space.ClassName).
     */
    virtual const std::string& _typeName() const = 0;

    /**
     * Gets all the 64 bit type ids of this class (including the type ids 
     * of its super types).
     */
    virtual const std::vector<long long>& _typeIds() const = 0;
    
    /**
     * Gets all the 16 bit type ids of this class (including the type 
     * ids of its super types).
     */
    virtual const std::vector<short>& _typeIds16Bit() const = 0;
    
    /**
     * Gets all the 16 bit type ids of this class (including the type 
     * ids of its super types) in base 64 representation.
     */
    virtual const std::vector<std::string>& _typeIds16BitBase64() const = 0;
    
    /**
     * Gets all the 16 bit type ids of this class (including the type 
     * ids of its super types) in base 64 representation, concatenated
     * to a single string.
     */
    virtual const std::string& _typeIds16BitBase64String() const = 0;
    
    /**
     * Gets all the qualified type names of this class (including the names 
     * of its super types).
     */
    virtual const std::vector<std::string>& _typeNames() const = 0;

    /**
     * Gets metadata of all the fields of this class and its super types.
     */
    virtual const std::vector<mgen::Field>& _fieldMetadatas() const = 0;

    /**
     * Gets a field metadata object by field id.
     */
    virtual const mgen::Field * _fieldById(const short fieldId) const = 0;

    /**
     * Gets a field metadata object by field name.
     */
    virtual const mgen::Field * _fieldByName(const std::string& name) const = 0;

    /**
     * Counts the number of fields currently set on this object instance. 
     * What counts as a field being set depends on the depth parameter provided.
     * A DEEP check will ensure that fields are valid (see _validate()) deeply
     * if they are to be counted. 
     */
    virtual int _numFieldsSet(const FieldSetDepth depth, const bool includeTransient) const = 0;

    /**
     * Checks if a field is set on this object instance. What counts as a 
     * field being set depends on the depth parameter provided. A DEEP check 
     * will ensure that fields are valid (see _validate()) deeply if they 
     * are to be counted. 
     */
    virtual bool _isFieldSet(const mgen::Field& field, const FieldSetDepth depth) const = 0;

    /**
     * Convenience method (mostly for testing), which forces all fields on this 
     * object instance to be set, either deeply or shallowly.
     */
    virtual const MGenBase& _setAllFieldsSet(const bool state, const FieldSetDepth depth) = 0;

    /**
     * Aside from generated classes being provided with a generated == operator, 
     * a virtual _equals(..) method is also generated which checks equality
     * polymorphicly.
     */
    virtual bool _equals(const mgen::MGenBase& other) const = 0;

    /**
     * Aside from being copy-constructible in normal way, a _deepCopy() method is
     * also generated to facilitate deep copies of polymorphic references. This is
     * required by the mgen::Polymorphic<..> container to ensure it can be 
     * copy-constructed properly.
     */
    virtual MGenBase * _deepCopy() const = 0;

    /**
     * This method validates that all the fields marked required for this class 
     * are set on this object instance. The check can be performed DEEP or SHALLOW
     * depending on the provided parameter.
     */
    virtual bool _validate(const FieldSetDepth depth) const = 0;

    /**
     * Subtypes are also generated with several additional methods, such as:
     *
	 *  template<typename ReaderType, typename ReadContextType>
	 *  void _readField(const short fieldId, ReadContextType& context, ReaderType& reader) {
     *
	 *  template<typename VisitorType>
	 *  void _accept(VisitorType& visitor, const mgen::FieldVisitSelection selection) const {
     *
	 *  template<typename VisitorType>
	 *  void _accept(VisitorType& visitor, const mgen::FieldVisitSelection selection) {
     *
     *  --> These methods may be dynamically invoked by through the generated ClassRegistry
     */

    MGenBase() {}
    virtual ~MGenBase() {}

    // Future release: std::string toString() const = 0;
    // Future release: int hashCode() const = 0;

};

} /* namespace mgen */

#endif /* MGEN_MGENBASE_H_ */
