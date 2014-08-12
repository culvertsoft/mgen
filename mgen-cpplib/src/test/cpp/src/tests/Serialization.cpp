/*
 * Serialization.cpp
 *
 *  Created on: 3 jul 2014
 *      Author: GiGurra
 */

#include "TestBase.h"

#include "gameworld/types/ClassRegistry.h"
#include "mgen/serialization/BinaryWriter.h"
#include "mgen/serialization/BinaryReader.h"
#include "mgen/serialization/VectorInputStream.h"
#include "mgen/serialization/VectorOutputStream.h"
#include "mgen/serialization/JsonPrettyWriter.h"
#include "mgen/serialization/JsonWriter.h"
#include "mgen/serialization/JsonReader.h"

/////////////////////////////////////////////////////////////////////

using namespace gameworld::types;
using namespace gameworld::types::basemodule1;

class SerializationTestData {
public:

    typedef mgen::VectorOutputStream OS;
    typedef mgen::VectorInputStream IS;
    typedef ClassRegistry CR;

    CR registry;
    std::vector<char> buffer;
    OS out;
    IS in;

    mgen::JsonWriter<OS, CR> jsonWriter;
    mgen::JsonWriter<OS, CR> jsonWriterCompact;
    mgen::JsonPrettyWriter<OS, CR> jsonPrettyWriter;
    mgen::JsonPrettyWriter<OS, CR> jsonPrettyWriterCompact;
    mgen::BinaryWriter<OS, CR> binaryWriter;
    mgen::BinaryWriter<OS, CR> binaryWriterCompact;

    mgen::JsonReader<IS, CR> jsonReader;
    mgen::BinaryReader<IS, CR> binaryReader;

    SerializationTestData() :
                    out(buffer),
                    in(buffer),
                    jsonWriter(out, registry),
                    jsonWriterCompact(out, registry, true),
                    jsonPrettyWriter(out, registry),
                    jsonPrettyWriterCompact(out, registry, true),
                    binaryWriter(out, registry),
                    binaryWriterCompact(out, registry, true),
                    jsonReader(in, registry),
                    binaryReader(in, registry) {
    }

    void reset() {
        out.reset();
        in.reset();
    }

};

#define FOREACH_SERIALIZER(f) { \
    f(testData, testData.jsonWriter, testData.jsonReader); \
    f(testData, testData.jsonWriterCompact, testData.jsonReader); \
    f(testData, testData.jsonPrettyWriter, testData.jsonReader); \
    f(testData, testData.jsonPrettyWriterCompact, testData.jsonReader); \
    f(testData, testData.binaryWriter, testData.binaryReader); \
    f(testData, testData.binaryWriterCompact, testData.binaryReader); \
}

BEGIN_TEST_GROUP(Serialization)

/////////////////////////////////////////////////////////////////////

static int s_nSerializers = 0;

template<typename WriterType, typename ReaderType>
static void haveSerializers(SerializationTestData& testData, WriterType& writer, ReaderType& reader) {
    s_nSerializers++;
}

BEGIN_TEST("Have serializers")

    SerializationTestData testData;
    ClassRegistry& registry = testData.registry;

    FOREACH_SERIALIZER(haveSerializers);

    ASSERT(s_nSerializers >= 2);

END_TEST

/////////////////////////////////////////////////////////////////////

BEGIN_TEST("Can validate")

    SerializationTestData testData;
    ClassRegistry& registry = testData.registry;
    const ClassRegistry::EntryMap& entries = registry.entries();

    int nInvalidObjects = 0;

    for (ClassRegistry::EntryMap::const_iterator it = entries.begin(); it != entries.end(); it++) {
        mgen::MGenBase * object = it->second.newInstance();
        ASSERT(object != 0);

        if (!object->_validate(mgen::SHALLOW)) {
            nInvalidObjects++;

            object->_setAllFieldsSet(true, mgen::SHALLOW);
            ASSERT(object->_validate(mgen::SHALLOW));

            object->_setAllFieldsSet(false, mgen::SHALLOW);
            ASSERT(!object->_validate(mgen::SHALLOW));

            object->_setAllFieldsSet(true, mgen::DEEP);
            ASSERT(object->_validate(mgen::DEEP));

            object->_setAllFieldsSet(false, mgen::DEEP);
            ASSERT(!object->_validate(mgen::DEEP));

        }

        delete object;
    }

    ASSERT(nInvalidObjects >= 3);

END_TEST

/////////////////////////////////////////////////////////////////////

template<typename WriterType, typename ReaderType>
static void canWriteRead(SerializationTestData& testData, WriterType& writer, ReaderType& reader) {

    ClassRegistry& registry = testData.registry;
    const ClassRegistry::EntryMap& entries = registry.entries();

    for (ClassRegistry::EntryMap::const_iterator it = entries.begin(); it != entries.end(); it++) {
        mgen::MGenBase * object = it->second.newInstance();
        ASSERT(object != 0);

        object->_setAllFieldsSet(true, mgen::DEEP);
        writer.writeObject(*object);

        mgen::MGenBase * objectBack = reader.readObject();
        ASSERT(objectBack != 0);

        ASSERT(object->_equals(*objectBack));

        delete objectBack;
        delete object;
    }

    testData.reset();
}

BEGIN_TEST("Can write/read")

    SerializationTestData testData;
    ClassRegistry& registry = testData.registry;

    FOREACH_SERIALIZER(canWriteRead);

END_TEST

/////////////////////////////////////////////////////////////////////

template<typename WriterType, typename ReaderType>
static void testEnums(SerializationTestData& testData, WriterType& writer, ReaderType& reader) {

    ClassRegistry& registry = testData.registry;
    const ClassRegistry::EntryMap& entries = registry.entries();

    VectorR3 v1, v2;
    ASSERT(v1 == v2);

    v1.setKind(kind_pretty);
    ASSERT(v1 != v2);

    v2.setKind(kind_ugly);
    ASSERT(v1 != v2);

    v1.setKind(kind_pretty);
    v2.setKind(kind_pretty);
    ASSERT(v1 == v2);

    v2.setKind(kind_ugly);
    ASSERT(v1 != v2);
    writer.writeObject(v1);
    writer.writeObject(v2);

    VectorR3 v1back = reader.template readStatic<VectorR3>();
    VectorR3 v2back = reader.template readStatic<VectorR3>();

    ASSERT(v1 == v1back);
    ASSERT(v2 == v2back);
    ASSERT(v1 != v2);

    testData.reset();
}

BEGIN_TEST("Enums")

    SerializationTestData testData;
    ClassRegistry& registry = testData.registry;

    FOREACH_SERIALIZER(testEnums);

END_TEST

/////////////////////////////////////////////////////////////////////

END_TEST_GROUP

