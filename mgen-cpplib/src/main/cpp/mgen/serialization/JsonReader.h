/*
 * JsonReader.h
 *
 *  Created on: 29 mar 2014
 *      Author: GiGurra
 */

#ifndef JsonREADER_H_
#define JsonREADER_H_

#include "mgen/classes/MGenBase.h"
#include "mgen/serialization/VarInt.h"
#include "mgen/exceptions/StreamCorruptedException.h"
#include "mgen/classes/ClassRegistryBase.h"
#include "mgen/util/missingfields.h"
#include "mgen/ext/rapidjson/document.h"
#include "mgen/util/missingfields.h"
#include "mgen/util/BuiltInSerializerUtil.h"

namespace mgen {
namespace internal {

#define throw_unexpected_type(expect, actual) throw SerializationException(std::string("JsonReader: unexpected type! -> Expected type ").append(expect).append(" but got type ").append(actual))

template<typename InputStreamType>
class JsonInStream {
public:

    JsonInStream(InputStreamType * stream) :
            m_nRead(0), m_buf(0x00), m_peeked(false), m_stream(stream) {
    }

    char Peek() const {
        if (!m_peeked) {
            m_buf = const_cast<JsonInStream&>(*this).readByte();
            m_peeked = true;
        }
        return m_buf;
    }

    char Take() {
        m_nRead++;
        if (m_peeked) {
            m_peeked = false;
            return m_buf;
        } else {
            return readByte();
        }
    }

    std::size_t Tell() {
        return m_nRead;
    }

    char * PutBegin() {
        throw SerializationException("JsonInStream::PutBegin(): BUG: Should not be called!");
    }

    void Put(const char c) {
        throw SerializationException("JsonInStream::Put(): BUG: Should not be called!");
    }

    size_t PutEnd(char * begin) {
        throw SerializationException("JsonInStream::PutEnd(): BUG: Should not be called!");
    }

private:

    char readByte() {
        char out;
        m_stream->read(&out, 1);
        return out;
    }

    int m_nRead;
    mutable char m_buf;
    mutable bool m_peeked;
    InputStreamType * m_stream;

};

} /* namespace internal */

template<typename Stream, typename Registry>
class JsonReader {
public:

    typedef rapidjson::Document::GenericValue Node;
    typedef rapidjson::Document::GenericValue::ConstMemberIterator MemberIterator;
    typedef rapidjson::Document::GenericValue::ConstValueIterator ArrayIterator;

    JsonReader(Stream& inputStream, const Registry& classRegistry) :
            m_inputStream(inputStream), m_jsonStream(&m_inputStream), m_clsReg(classRegistry) {
    }

    MGenBase * readObject() {
        const rapidjson::Document& doc = m_rapidJsonDocument.ParseStream<
                rapidjson::kParseDefaultFlags>(m_jsonStream);
        if (!doc.HasParseError()) {
            const Node& node = doc;
            return readMgenObject(node, false, -1);
        } else {
            throw StreamCorruptedException(
                    std::string("JsonReader::readMgenObject(): Could not parse json, reason: ").append(
                            doc.GetParseError()));
        }
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

    MGenBase * readMgenObject(
            const Node& node,
            const bool constrained,
            const long long expectTypeId,
            MGenBase * object = 0) {

        const std::vector<std::string> ids = readIds(node);

        if (!ids.empty()) {

            const ClassRegistryEntry * entry = serialutil::getCompatibleEntry(
                    m_clsReg,
                    ids,
                    constrained,
                    expectTypeId);

            if (entry)
                object = serialutil::readObjInternal(*this, m_clsReg, node, object, *entry);

        }

        return object;

    }

    const std::vector<std::string> readIds(const Node& node) {
        static const std::vector<std::string> emptyIds(0);

        if (node.IsNull())
            return emptyIds;

        const Node& v = node["__t"];
        if (v.IsString()) {

            const char * str = v.GetString();
            const int nTypeIds = v.GetStringLength() / 3;

            std::vector<std::string> ids(nTypeIds);

            for (int i = 0; i < nTypeIds; i++)
                ids[i].assign(str + i * 3, 3);

            return ids;

        } else {
            throw SerializationException(
                    std::string("JsonReader.h::readMGenObject: Node is missing \"__t\" field."));
        }

        return emptyIds;

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
            throw_unexpected_type("array", "something_wrong");
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
            throw_unexpected_type("map", "something_wrong");
        }
    }

    template<typename T>
    void read(Polymorphic<T>& v, const Node& node) {
        if (node.IsObject()) {
            v.set((T*) readMgenObject(node, true, T::_type_id));
        } else if (node.IsNull()) { // TODO: What? Ignore? Zero length? Throw?
        } else {
            throw_unexpected_type(T::_type_name(), "something_wrong");
        }
    }

    void read(MGenBase& v, const Node& node) {
        if (node.IsObject()) {
            readMgenObject(node, true, v._typeId(), &v);
        } else if (node.IsNull()) { // TODO: What? Ignore? Zero length? Throw?
        } else {
            throw_unexpected_type(v._typeName(), "something_wrong");
        }
    }

    void read(bool& v, const Node& node) {
        if (node.IsBool()) {
            v = node.GetBool();
        } else {
            throw_unexpected_type("bool", "something_wrong");
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
            throw_unexpected_type("string", "something_wrong");
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

    Stream& m_inputStream;
    internal::JsonInStream<Stream> m_jsonStream;
    rapidjson::Document m_rapidJsonDocument;
    const Registry& m_clsReg;

};

#undef throw_unexpected_type

} /* namespace mgen */

#endif /* JsonREADER_H_ */
