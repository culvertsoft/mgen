/*
 * JSONReader.h
 *
 *  Created on: 29 mar 2014
 *      Author: GiGurra
 */

#ifndef JSONREADER_H_
#define JSONREADER_H_

#include "mgen/classes/MGenBase.h"
#include "mgen/serialization/VarInt.h"
#include "mgen/exceptions/StreamCorruptedException.h"
#include "mgen/classes/ClassRegistryBase.h"
#include "mgen/util/missingfields.h"
#include "mgen/ext/rapidjson/document.h"

namespace mgen {
namespace internal {

#define throw_unexpected_type(expect, actual) throw SerializationException(std::string("JSONReader: unexpected type! -> Expected type ").append(expect).append(" but got type ").append(actual))

template<typename InputStreamType>
class JSONInStream {
public:

    JSONInStream(InputStreamType * stream) :
            m_nRead(0), m_buf(0x00), m_peeked(false), m_stream(stream) {
    }

    char Peek() const {
        if (!m_peeked) {
            m_buf = const_cast<JSONInStream&>(*this).readByte();
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
        throw SerializationException("JSONInStream::PutBegin(): BUG: Should not be called!");
    }

    void Put(const char c) {
        throw SerializationException("JSONInStream::Put(): BUG: Should not be called!");
    }

    size_t PutEnd(char * begin) {
        throw SerializationException("JSONInStream::PutEnd(): BUG: Should not be called!");
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
class JSONReader {
public:

    typedef rapidjson::Document::GenericValue Node;
    typedef rapidjson::Document::GenericValue::ConstMemberIterator MemberIterator;
    typedef rapidjson::Document::GenericValue::ConstValueIterator ArrayIterator;

    JSONReader(Stream& inputStream, const Registry& classRegistry) :
            m_inputStream(inputStream), m_jsonStream(&m_inputStream), m_classRegistry(classRegistry) {
    }

    template<typename FieldType>
    void readField(const Field& /*field*/, const Node& node, FieldType& v) {
        read(v, node);
    }

    void handleUnknownField(const std::string& fieldId, const Node& node) {
        // TODO: Implement error handling for unknown fields
    }

    void handleUnknownField(const short /*fieldId*/, const Node& node) {
        // TODO: Implement error handling for unknown fields
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

        if (!object._validate(SHALLOW)) {

            const std::string missingFieldsString = missingfields::requiredAsString(object);

            throw SerializationException(
                    std::string("mgen::Reader::readFields(..) failed: Missing required fields ").append(
                            missingFieldsString).append(" for object of type: ").append(
                            object._typeName()));
        }

    }

    MGenBase * readMgenObject() {
        const rapidjson::Document& doc = m_rapidJsonDocument.ParseStream<
                rapidjson::kParseDefaultFlags>(m_jsonStream);
        if (!doc.HasParseError()) {
            const Node& node = doc;
            return readMgenObject(node);
        } else {
            throw StreamCorruptedException(
                    std::string("JSONReader::readMgenObject(): Could not parse json, reason: ").append(
                            doc.GetParseError()));
        }
    }

private:

    MGenBase * readMgenObject(
            const Node& node,
            const long long constraintTypeId = -1,
            MGenBase * object = 0) {

        if (node.IsNull())
            return 0;

        const ClassRegistryEntry * classRegistryEntry = readMGenBaseHeader(node);

        if (classRegistryEntry) {

            if (classRegistryEntry == mgen::ClassRegistryEntry::NULL_ENTRY())
                return 0;

            if (constraintTypeId != -1 && !classRegistryEntry->isInstanceOfTypeId(constraintTypeId))
                return 0;

            if (!object)
                object = classRegistryEntry->newInstance();

            m_classRegistry.readObjectFields(*object, node, *this);
            return object;

        } else {
            return 0;
        }

    }

    const ClassRegistryEntry * readMGenBaseHeader(const Node& node) {

        if (node.IsNull())
            return 0;

        const Node& v = node["__t"];
        if (v.IsArray()) {

            const int nTypeIds = v.Size();

            std::vector<std::string> typeIdsBase64(nTypeIds);
            for (int i = 0; i < nTypeIds; i++)
                read(typeIdsBase64[i], v[i]);

            const ClassRegistryEntry * entry = m_classRegistry.getByTypeIds16BitBase64(typeIdsBase64);
            if (entry)
                return entry;

        } else {
            // TODO: write out object string/implement toString
            throw SerializationException(
                    std::string("JSONReader.h::readMGenObject: Node is missing \"__t\" field."));
        }

        return 0;

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
            v.set((T*) readMgenObject(node, T::_type_id));
        } else if (node.IsNull()) { // TODO: What? Ignore? Zero length? Throw?
        } else {
            throw_unexpected_type(T::_type_name(), "something_wrong");
        }
    }

    void read(MGenBase& v, const Node& node) {
        if (node.IsObject()) {
            readMgenObject(node, v._typeId(), &v);
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
    internal::JSONInStream<Stream> m_jsonStream;
    rapidjson::Document m_rapidJsonDocument;
    const Registry& m_classRegistry;

};

#undef throw_unexpected_type

template<typename Stream, typename Registry>
inline JSONReader<Stream, Registry> * new_JSONReader(
        Stream& inputStream,
        const Registry& classRegistry) {
    return new JSONReader<Stream, Registry>(inputStream, classRegistry);
}

} /* namespace mgen */

#endif /* JSONREADER_H_ */
