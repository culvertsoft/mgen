/*
 * JsonReader.h
 *
 *  Created on: 29 mar 2014
 *      Author: GiGurra
 */

#ifndef MGEN_JsonREADER_H_
#define MGEN_JsonREADER_H_

#include "mgen/ext/rapidjson/document.h"
#include "mgen/exceptions/StreamCorruptedException.h"
#include "mgen/util/stringutil.h"
#include "mgen/util/BuiltInSerializerUtil.h"
#include "mgen/util/missingfields.h"
#include "mgen/serialization/JsonInputStream.h"

namespace mgen {

/**
 * Internal convenience macro for throwing exceptions with useful error messages.
 * Undef:ed at the end of this header, as it is only intended to be used internally.
 */
#define throw_unexpected_type(expect, actual) \
    throw UnexpectedTypeException(toString("Unexpected type! -> Expected type ").append(toString(expect)).append(" but got type ").append(toString(actual)))

/**
 * A class for reading data streams written in Json. JsonReader objects are 
 * constructed with two type parameters: MGenStreamType and ClassRegistryType. 
 * These specify which type of data input stream is to be read from and what 
 * classes can be marshalled, respectively.
 * 
 * The Json being read can be any Json that matches the data model of the 
 * MGen classes being read - not just Json written by the MGen JsonWriters.
 * However if you want to receive types in polymorphic fields/heterogenous 
 * containers, MGen type metadata is required to be present in the stream, 
 * which is something you can probably expect from the MGen JsonWriters.
 */
template<typename MGenStreamType, typename ClassRegistryType>
class JsonReader {
public:

    typedef rapidjson::Document::GenericValue Node;
    typedef rapidjson::Document::GenericValue::ConstMemberIterator MemberIterator;
    typedef rapidjson::Document::GenericValue::ConstValueIterator ArrayIterator;

    /**
     * Constructs a JsonReader with the given a data input stream (data source)
     * and class registry. A third optional parameter can also be used to specify
     * if this reader should perform extra type checking for incoming data.
     * For situations where metadata on the wire is not actually needed, it will
     * normally be ignored. However with the extraTypeChecking parameter set to true,
     * redundant metadata will also be checked.
     */
    JsonReader(
            MGenStreamType& inputStream, 
            const ClassRegistryType& classRegistry, 
            const bool extraTypeChecking = false) :
                    m_inputStream(inputStream),
                    m_jsonStream(&m_inputStream),
                    m_clsReg(classRegistry),
                    m_extraTypeChecking(extraTypeChecking) {
    }

    /**
     * Primary interface method for reading polymorphic objects from streams. 
     * Returns a read back object (on the heap), or NULL if an object of unknown 
     * type was received (in which case it's just skipped past in the stream).
     * This method requires that type metadata is available on the stream at least 
     * for the root object read back.
     */
    MGenBase * readObject() {
        return readPoly(readDocumentRoot(), false, -1);
    }

    /**
     * Similar to readObject(), this method also checks that the read back
     * object is of a certain type (or base type), as specified with the
     * template parameter.
     *
     * This method does not require that type metadata is available from the stream
     * for the root object read back, in which case it will construct a new object 
     * on the heap of the exact same type as the template parameter.
     */
    template<typename MGenType>
    MGenType * readObject() {
        return (MGenType*) readPoly(readDocumentRoot(), true, MGenType::_type_id);
    }

    /**
     * Similar to readObject<MGenType>(), except that this method reads back 
     * the object directly to the stack and discards any potential subtype 
     * information.
     */
    template<typename MGenType>
    MGenType readStatic() {
        MGenType out;
        read(out, readDocumentRoot());
        return out;
    }

    /**
     * Method called while reading the fields of an object from a data stream.
     * When reading an object from stream, fields may appear in any order depending
     * on the serialization format and also if the developer reorders the field
     * in an object between different code versions. This method is what connects
     * the field in the stream to the field on the object being read.
     */
    template<typename FieldType>
    void readField(const Field& /*field*/, const Node& node, FieldType& v) {
        read(v, node);
    }

    /**
     * When reading objects from a data stream, new fields may have been added 
     * to the data model without us having generated source code for it yet.
     * New/Unknown fields are simply discarded and skipped past in the stream.
     * This callback is meant to be overloaded should you want to deal with
     * unknown fields in a different manner.
     */
    template<typename arg>
    void handleUnknownField(const arg& fieldId, const Node& node) {
    }

    /**
     * Invoked dynamically through the generated ClassRegistry object. This method
     * is required to support reading of polymorphic objects from data streams.
     * The requirement comes from C++'s inability to natively support double
     * dynamic dispatch, which we get around by emitting our own 'vtables'
     * in generated ClassRegistry code.
     */
    template<typename ClassType>
    void readFields(ClassType& object, const Node& node) {

        for (MemberIterator it = node.MemberBegin(); it != node.MemberEnd(); it++) {
            const Node& nameNode = it->name;
            const std::string& name = nameNode.GetString();
            const Field * field = object._fieldByName(name);
            if (field) {
                object._readField(field->id(), it->value, *this);
            } else {
                handleUnknownField(name, it->value);
            }
        }

        missingfields::ensureNoMissingFields(object);
    }

private:

    /**
     * Internal method for reading a polymorphic object from a data stream.
     */
    MGenBase * readPoly(const Node& node, const bool constrained, const long long expectTypeId) {

        if (node.IsNull())
            return 0;

        const std::string& ids = readIds(node);

        const ClassRegistryEntry * entry = serialutil::getCompatibleEntry(m_clsReg, ids, constrained, expectTypeId);

        if (entry)
            return serialutil::readObjInternal(*this, m_clsReg, node, 0, *entry);

        return 0;

    }

    /**
     * Internal method for reading type ids from a '__t' field on a JSON 
     * node being read back to an MGen object.
     */
    const std::string readIds(const Node& node) {
        const Node& v = node["__t"];
        if (v.IsString()) {
            return std::string(v.GetString(), v.GetStringLength());
        }
        else {
            static const std::string emptyString;
            return emptyString;
        }
    }

    /**
     * Internal method for reading a list in a data stream.
     */
    template<typename T>
    void read(std::vector<T>& v, const Node& node) {
        if (node.IsArray()) {
            const int n = node.Size();
            v.resize(n);
            for (int i = 0; i < n; i++)
                read(v[i], node[i]);
        } else if (node.IsNull()) { // TODO: What? Ignore? Zero length? Throw?
        } else {
            throw_unexpected_type("array", get_rapidjson_type_name(node.GetType()));
        }
    }

    /**
     * Internal method for converting json object keys to required types (e.g. numeric types).
     * This is required since JSON only supports strings as map keys/object field names.
     */
    template<typename K>
    void readMapKey(K& out, const Node& node) {
        std::string keyString;
        read(keyString, node);
        out = fromString<K>(keyString);
    }

    /**
     * Internal method for reading a map in a data stream.
     */
    template<typename K, typename V>
    void read(std::map<K, V>& v, const Node& node) {
		if (node.IsObject()) {
			v.clear();
            for (MemberIterator it = node.MemberBegin(); it != node.MemberEnd(); it++) {
                K key;
                readMapKey(key, it->name);
                read(v[key], it->value);
            }
        } else if (node.IsNull()) { // TODO: What? Ignore? Zero length? Throw?
        } else {
            throw_unexpected_type("map", get_rapidjson_type_name(node.GetType()));
        }
    }

    /**
     * Internal method for reading a polymorphic MGen object in a data stream.
     */
    template<typename MGenType>
    void read(Polymorphic<MGenType>& object, const Node& node) {
        if (node.IsObject()) {
            object.set((MGenType*) readPoly(node, true, MGenType::_type_id));
        } else if (node.IsNull()) { // TODO: What? Ignore? Zero length? Throw?
        } else {
            throw_unexpected_type(MGenType::_type_name(), get_rapidjson_type_name(node.GetType()));
        }
    }

    /**
     * Internal method for reading an enum in a data stream.
     */
    template<typename EnumType>
    void read(EnumType& e, const int /* type_evidence */, const Node& node) {
        std::string str;
        read(str, node);
        e = get_enum_value(e, str);
    }

    /**
     * Internal method for reading an MGen object of statically known class.
     */
    template<typename MGenType>
    void read(MGenType& object, const MGenBase& /* type_evidence */, const Node& node) {
        if (node.IsNull()) {
            missingfields::ensureNoMissingFields(object);
        } else if (node.IsObject()) {
            if (m_extraTypeChecking)
                serialutil::checkExpType(m_clsReg, &object, readIds(node));
            readFields(object, node);
        } else if (node.IsNull()) { // TODO: What? Ignore? Zero length? Throw?
        } else {
            throw_unexpected_type(object._typeName(), get_rapidjson_type_name(node.GetType()));
        }
    }

    /**
     * Internal method for reading an MGen object or an enum.
     */
    template<typename MGenTypeOrEnum>
    void read(MGenTypeOrEnum& v, const Node& node) {
        read(v, v, node);
    }

    /**
     * Internal method for reading a bool.
     */
    void read(bool& v, const Node& node) {
        if (node.IsBool()) {
            v = node.GetBool();
        } else {
            throw_unexpected_type("bool", get_rapidjson_type_name(node.GetType()));
        }
    }
    
    /**
     * Internal method for reading an int8.
     */
    void read(char & v, const Node& node) { v = readFixedPointNumber<char>(node); }
    
    /**
     * Internal method for reading an int16.
     */
    void read(short & v, const Node& node) { v = readFixedPointNumber<short>(node); }
    
    /**
     * Internal method for reading an int32.
     */
    void read(int& v, const Node& node) { v = readFixedPointNumber<int>(node); }
    
    /**
     * Internal method for reading an int64.
     */
    void read(long long& v, const Node& node) { v = readFixedPointNumber<long long>(node); }
    
    /**
     * Internal method for reading an float32.
     */
    void read(float & v, const Node& node) { v = readFloatingPointNumber<float>(node); }
    
    /**
     * Internal method for reading an float64.
     */
    void read(double & v, const Node& node) { v = readFloatingPointNumber<double>(node); }

    /**
     * Internal method for reading a string.
     */
    void read(std::string& v, const Node& node) {
        if (node.IsString()) {
            v = node.GetString();
        } else if (node.IsNull()) { // TODO: What? Ignore? Zero length? Throw?
        } else {
            throw_unexpected_type("string", get_rapidjson_type_name(node.GetType()));
        }
    }

    /**
     * Internal convenience method for reading a fixed point number.
     */
    template<typename T>
    T readFixedPointNumber(const Node& node) {
        if (node.IsNumber()) {
            if (node.IsInt64())
                return (T) node.GetInt64();
            else if (node.IsInt())
                return (T) node.GetInt();
            else if (node.IsDouble())
                return (T) node.GetDouble();
        } else {
            throw_unexpected_type("fixed_point_number", get_rapidjson_type_name(node.GetType()));
        }
        return T();
    }

    /**
     * Internal convenience method for reading a floating point number.
     */
    template<typename T>
    T readFloatingPointNumber(const Node& node) {
        if (node.IsNumber()) {
            if (node.IsDouble())
                return (T) node.GetDouble();
            else if (node.IsInt64())
                return (T) node.GetInt64();
            else if (node.IsInt())
                return (T) node.GetInt();
        } else {
            throw_unexpected_type("floating_point_number", get_rapidjson_type_name(node.GetType()));
        }
        return T();
    }

    /**
     * Internal convenience method for parsing the json object to read.
     */
    const rapidjson::Document& readDocumentRoot() {
        const rapidjson::Document& node = m_rapidJsonDocument.ParseStream<rapidjson::kParseDefaultFlags>(m_jsonStream);
        if (!node.HasParseError()) {
            return node;
        } else {
            throw StreamCorruptedException(
                    std::string("JsonReader::readDocumentRoot(): Could not parse json, reason: ").append(
                            node.GetParseError()));
        }
    }

    MGenStreamType& m_inputStream;
    internal::JsonInStream<MGenStreamType> m_jsonStream;
    rapidjson::Document m_rapidJsonDocument;
    const ClassRegistryType& m_clsReg;
    const bool m_extraTypeChecking;

};

#undef throw_unexpected_type

} /* namespace mgen */

#endif /* MGEN_JsonREADER_H_ */
