/*
 * JsonReader.h
 *
 *  Created on: 29 mar 2014
 *      Author: GiGurra
 */

#ifndef JsonREADER_H_
#define JsonREADER_H_

#include "mgen/ext/rapidjson/document.h"
#include "mgen/classes/ClassRegistryBase.h"
#include "mgen/util/BuiltInSerializerUtil.h"
#include "mgen/exceptions/StreamCorruptedException.h"
#include "JsonInputStream.h"

namespace mgen {

#define S(x) toString(x)

#define throw_unexpected_type(expect, actual) \
    throw UnexpectedTypeException(S("Unexpected type! -> Expected type ").append(S(expect)).append(" but got type ").append(S(actual)))

template<typename MGenStreamType, typename ClassRegistryType>
class JsonReader {
public:

    typedef rapidjson::Document::GenericValue Node;
    typedef rapidjson::Document::GenericValue::ConstMemberIterator MemberIterator;
    typedef rapidjson::Document::GenericValue::ConstValueIterator ArrayIterator;

    JsonReader(MGenStreamType& inputStream, const ClassRegistryType& classRegistry, const bool excessiveTypeChecking =
            false) :
                    m_inputStream(inputStream),
                    m_jsonStream(&m_inputStream),
                    m_clsReg(classRegistry),
                    m_excessiveTypeChecking(excessiveTypeChecking) {
    }

    MGenBase * readObject() {
        return readPoly(readDocumentRoot(), false, -1);
    }

    template<typename MGenType>
    MGenType * readObject() {
        return (MGenType*) readPoly(readDocumentRoot(), true, MGenType::_type_id);
    }

    template<typename MGenType>
    MGenType readStatic() {
        MGenType out;
        read(out, readDocumentRoot());
        return out;
    }

    template<typename FieldType>
    void readField(const Field& /*field*/, const Node& node, FieldType& v) {
        read(v, node);
    }

    template<typename arg>
    void handleUnknownField(const arg& fieldId, const Node& node) {
    }

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

    MGenBase * readPoly(const Node& node, const bool constrained, const long long expectTypeId) {

        if (node.IsNull())
            return 0;

        const std::vector<std::string> ids = readIds(node);

        const ClassRegistryEntry * entry = serialutil::getCompatibleEntry(m_clsReg, ids, constrained, expectTypeId);

        if (entry)
            return serialutil::readObjInternal(*this, m_clsReg, node, 0, *entry);

        return 0;

    }

    const std::vector<std::string> readIds(const Node& node) {
        static const std::vector<std::string> emptyIds(0);

        const Node& v = node["__t"];
        if (v.IsString()) {

            const char * str = v.GetString();
            const int nTypeIds = v.GetStringLength() / 3;

            std::vector<std::string> ids(nTypeIds);

            for (int i = 0; i < nTypeIds; i++)
                ids[i].assign(str + i * 3, 3);

            return ids;

        } else {
            return emptyIds;
        }

    }

    template<typename T>
    void read(std::vector<T>& v, const Node& node) {
        if (node.IsArray()) {
            const int n = node.Size();
            v.resize(n);
            for (int i = 0; i < n; i++)
                read(v[i], node[i]);
        } else if (node.IsNull()) { // TODO: What? Ignore? Zero length? Throw?
        } else {
            throw_unexpected_type("array", "something_else");
        }
    }

    template<typename K, typename V>
    void read(std::map<K, V>& v, const Node& node) {
        if (node.IsObject()) {
            for (MemberIterator it = node.MemberBegin(); it != node.MemberEnd(); it++) {
                K key;
                read(key, it->name);
                read(v[key], it->value);
            }
        } else if (node.IsNull()) { // TODO: What? Ignore? Zero length? Throw?
        } else {
            throw_unexpected_type("map", "something_else");
        }
    }

    template<typename MGenType>
    void read(Polymorphic<MGenType>& object, const Node& node) {
        if (node.IsObject()) {
            object.set((MGenType*) readPoly(node, true, MGenType::_type_id));
        } else if (node.IsNull()) { // TODO: What? Ignore? Zero length? Throw?
        } else {
            throw_unexpected_type(MGenType::_type_name(), "something_else");
        }
    }

    template<typename EnumType>
    void read(EnumType& e, const int /* type_evidence */, const Node& node) {
        std::string str;
        read(str, node);
        e = get_enum_value(e, str);
    }

    template<typename MGenType>
    void read(MGenType& object, const MGenBase& /* type_evidence */, const Node& node) {
        if (node.IsObject()) {
            if (m_excessiveTypeChecking)
                serialutil::checkExpType(m_clsReg, &object, readIds(node));
            readFields(object, node);
        } else if (node.IsNull()) { // TODO: What? Ignore? Zero length? Throw?
        } else {
            throw_unexpected_type(object._typeName(), "something_else");
        }
    }

    template<typename MGenTypeOrEnum>
    void read(MGenTypeOrEnum& v, const Node& node) {
        read(v, v, node);
    }

    void read(bool& v, const Node& node) {
        if (node.IsBool()) {
            v = node.GetBool();
        } else {
            throw_unexpected_type("bool", "something_else");
        }
    }

    void read(char & v, const Node& node) {
        v = readFixedPointNumber<char>(node);
    }
    void read(short & v, const Node& node) {
        v = readFixedPointNumber<short>(node);
    }
    void read(int& v, const Node& node) {
        v = readFixedPointNumber<int>(node);
    }
    void read(long long& v, const Node& node) {
        v = readFixedPointNumber<long long>(node);
    }
    void read(float & v, const Node& node) {
        v = readFloatingPointNumber<float>(node);
    }
    void read(double & v, const Node& node) {
        v = readFloatingPointNumber<double>(node);
    }

    void read(std::string& v, const Node& node) {
        if (node.IsString()) {
            v = node.GetString();
        } else if (node.IsNull()) { // TODO: What? Ignore? Zero length? Throw?
        } else {
            throw_unexpected_type("string", "something_else");
        }
    }

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
            throw_unexpected_type("fixed_point_number", "something_else");
        }
        return T();
    }

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
            throw_unexpected_type("floating_point_number", "something_else");
        }
        return T();
    }

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
    const bool m_excessiveTypeChecking;

};

#undef throw_unexpected_type

#undef S

} /* namespace mgen */

#endif /* JsonREADER_H_ */
