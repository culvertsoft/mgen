#include "TestBase.h"

#include "gameworld/types/ClassRegistry.h"
#include "mgen/serialization/BinaryWriter.h"
#include "mgen/serialization/BinaryReader.h"
#include "mgen/serialization/VectorInputStream.h"
#include "mgen/serialization/VectorOutputStream.h"
#include "mgen/serialization/JsonPrettyWriter.h"
#include "mgen/serialization/JsonWriter.h"
#include "mgen/serialization/JsonReader.h"

#define FLOAT_APPRIX_EQ(a,b) ( fabs((a) - (b)) <= fabs(a * 1e-5))

/////////////////////////////////////////////////////////////////////

using namespace gameworld::types;
using namespace gameworld::types::basemodule1;

class ExtremeValuesTestData {
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

    ExtremeValuesTestData() :
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

BEGIN_TEST_GROUP(ExtremeValues)

/////////////////////////////////////////////////////////////////////

static int s_nSerializers = 0;

template<typename WriterType, typename ReaderType>
static void haveSerializers(ExtremeValuesTestData& testData, WriterType& writer, ReaderType& reader) {
    s_nSerializers++;
}

BEGIN_TEST("Have serializers")

    ExtremeValuesTestData testData;
    ClassRegistry& registry = testData.registry;

    FOREACH_SERIALIZER(haveSerializers);

    ASSERT(s_nSerializers >= 2);

END_TEST

/////////////////////////////////////////////////////////////////////

template<typename WriterType, typename ReaderType>
static void testFixedPoint(ExtremeValuesTestData& testData, WriterType& writer, ReaderType& reader) {

    Car car, carBack;
    car._setAllFieldsSet(true, mgen::DEEP);

    car.setId(LONG_LONG_MAX);
    writer.writeObject(car);
    carBack = reader.template readStatic<Car>();
    ASSERT(carBack.getId() == LONG_LONG_MAX);
    testData.reset();

    car.setId(LONG_LONG_MIN);
    writer.writeObject(car);
    carBack = reader.template readStatic<Car>();
    ASSERT(carBack.getId() == LONG_LONG_MIN);
    testData.reset();

    car.setId(0);
    writer.writeObject(car);
    carBack = reader.template readStatic<Car>();
    ASSERT(carBack.getId() == 0);
    testData.reset();

    car.setId(1);
    writer.writeObject(car);
    carBack = reader.template readStatic<Car>();
    ASSERT(carBack.getId() == 1);
    testData.reset();

    car.setId(-1);
    writer.writeObject(car);
    carBack = reader.template readStatic<Car>();
    ASSERT(carBack.getId() == -1);
    testData.reset();

    car.setTopSpeed(INT_MAX);
    writer.writeObject(car);
    carBack = reader.template readStatic<Car>();
    ASSERT(carBack.getTopSpeed() == INT_MAX);
    testData.reset();

    car.setTopSpeed(INT_MIN);
    writer.writeObject(car);
    carBack = reader.template readStatic<Car>();
    ASSERT(carBack.getTopSpeed() == INT_MIN);
    testData.reset();

    car.setTopSpeed(0);
    writer.writeObject(car);
    carBack = reader.template readStatic<Car>();
    ASSERT(carBack.getTopSpeed() == 0);
    testData.reset();

    car.setTopSpeed(1);
    writer.writeObject(car);
    carBack = reader.template readStatic<Car>();
    ASSERT(carBack.getTopSpeed() == 1);
    testData.reset();

    car.setTopSpeed(-1);
    writer.writeObject(car);
    carBack = reader.template readStatic<Car>();
    ASSERT(carBack.getTopSpeed() == -1);
    testData.reset();

}

BEGIN_TEST("Fixed point")

    ExtremeValuesTestData testData;
    ClassRegistry& registry = testData.registry;
    const ClassRegistry::EntryMap& entries = registry.entries();

    FOREACH_SERIALIZER(testFixedPoint);

END_TEST

/////////////////////////////////////////////////////////////////////

template<typename WriterType, typename ReaderType>
static void testFloatingPoint(ExtremeValuesTestData& testData, WriterType& writer, ReaderType& reader) {

    Car car, carBack;
    car._setAllFieldsSet(true, mgen::DEEP);

    car.getPositioningMutable().getVelocityMutable().setY(0.0f);
    writer.writeObject(car);
    carBack = reader.template readStatic<Car>();
    ASSERT(carBack.getPositioning().getVelocity().getY() == 0.0f);
    testData.reset();

    car.getPositioningMutable().getVelocityMutable().setY(1.5f);
    writer.writeObject(car);
    carBack = reader.template readStatic<Car>();
    ASSERT(FLOAT_APPRIX_EQ(carBack.getPositioning().getVelocity().getY(), 1.5f));
    testData.reset();

    car.getPositioningMutable().getVelocityMutable().setY(-1.5f);
    writer.writeObject(car);
    carBack = reader.template readStatic<Car>();
    ASSERT(FLOAT_APPRIX_EQ(carBack.getPositioning().getVelocity().getY(), -1.5f));
    testData.reset();

    car.getPositioningMutable().getVelocityMutable().setZ(0.0);
    writer.writeObject(car);
    carBack = reader.template readStatic<Car>();
    ASSERT(FLOAT_APPRIX_EQ(carBack.getPositioning().getVelocity().getZ(), 0.0));
    testData.reset();

    car.getPositioningMutable().getVelocityMutable().setZ(1.5);
    writer.writeObject(car);
    carBack = reader.template readStatic<Car>();
    ASSERT(FLOAT_APPRIX_EQ(carBack.getPositioning().getVelocity().getZ(), 1.5));
    testData.reset();

    car.getPositioningMutable().getVelocityMutable().setZ(-1.5);
    writer.writeObject(car);
    carBack = reader.template readStatic<Car>();
    ASSERT(FLOAT_APPRIX_EQ(carBack.getPositioning().getVelocity().getZ(), -1.5));
    testData.reset();

}

BEGIN_TEST("Floating point")

    ExtremeValuesTestData testData;
    ClassRegistry& registry = testData.registry;
    const ClassRegistry::EntryMap& entries = registry.entries();

    FOREACH_SERIALIZER(testFloatingPoint);

END_TEST

/////////////////////////////////////////////////////////////////////

END_TEST_GROUP

