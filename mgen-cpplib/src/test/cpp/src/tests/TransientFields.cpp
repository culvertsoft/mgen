#include "TestBase.h"

#include "trans/testmodel/ClassRegistry.h"
#include "mgen/serialization/BinaryWriter.h"
#include "mgen/serialization/BinaryReader.h"
#include "mgen/serialization/VectorInputStream.h"
#include "mgen/serialization/VectorOutputStream.h"
#include "mgen/serialization/JsonPrettyWriter.h"
#include "mgen/serialization/JsonWriter.h"
#include "mgen/serialization/JsonReader.h"

/////////////////////////////////////////////////////////////////////

using namespace mgen;
using namespace trans::testmodel;

class TransientTestData {
public:

    typedef VectorOutputStream OS;
    typedef VectorInputStream IS;
    typedef ClassRegistry CR;

    CR registry;
    std::vector<char> buffer;
    OS out;
    IS in;

    JsonWriter<OS, CR> jsonWriter;
    JsonWriter<OS, CR> jsonWriterCompact;
    JsonPrettyWriter<OS, CR> jsonPrettyWriter;
    JsonPrettyWriter<OS, CR> jsonPrettyWriterCompact;
    BinaryWriter<OS, CR> binaryWriter;
    BinaryWriter<OS, CR> binaryWriterCompact;

    JsonReader<IS, CR> jsonReader;
    BinaryReader<IS, CR> binaryReader;

    TransientTestData() :
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

BEGIN_TEST_GROUP(TransientFields)

/////////////////////////////////////////////////////////////////////

static int s_nSerializers = 0;

template<typename WriterType, typename ReaderType>
static void haveSerializers(TransientTestData& testData, WriterType& writer, ReaderType& reader) {
    s_nSerializers++;
}

BEGIN_TEST("Have serializers")

    TransientTestData testData;
    ClassRegistry& registry = testData.registry;

    FOREACH_SERIALIZER(haveSerializers);

    ASSERT(s_nSerializers >= 2);

END_TEST

/////////////////////////////////////////////////////////////////////

template<typename WriterType, typename ReaderType>
static void canWriteRead(TransientTestData& testData, WriterType& writer, ReaderType& reader) {

    ClassRegistry& registry = testData.registry;
    const ClassRegistry::EntryMap& entries = registry.entries();

    for (ClassRegistry::EntryMap::const_iterator it = entries.begin(); it != entries.end(); it++) {
        MGenBase * object = it->second.newInstance();
        ASSERT(object);

        object->_setAllFieldsSet(true, DEEP);
        writer.writeObject(*object);

        MGenBase * objectBack = reader.readObject();
        ASSERT(objectBack);

        delete objectBack;
        delete object;
    }

    testData.reset();
}

BEGIN_TEST("Can write/read")

    TransientTestData testData;
    ClassRegistry& registry = testData.registry;

    FOREACH_SERIALIZER(canWriteRead);

END_TEST

/////////////////////////////////////////////////////////////////////

template<typename WriterType, typename ReaderType>
static void checkFieldCount(TransientTestData& testData, WriterType& writer, ReaderType& reader) {

    ClassRegistry& registry = testData.registry;
    const ClassRegistry::EntryMap& entries = registry.entries();

    MyType o;
    o._setAllFieldsSet(true, DEEP);

    writer.writeObject(o);

    MyType oBack = reader.template readStatic<MyType>();

    ASSERT(o._numFieldsSet(SHALLOW, true) > 0);
    ASSERT(oBack._numFieldsSet(SHALLOW, true) > 0);
    ASSERT(o._numFieldsSet(SHALLOW, true) > oBack._numFieldsSet(SHALLOW, true));

    testData.reset();
}

BEGIN_TEST("Check field count")

    TransientTestData testData;
    ClassRegistry& registry = testData.registry;

    FOREACH_SERIALIZER(checkFieldCount);

END_TEST

/////////////////////////////////////////////////////////////////////
END_TEST_GROUP

