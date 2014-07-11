/*
 * ObjectManipulation.cpp
 *
 *  Created on: 2 jul 2014
 *      Author: GiGurra
 */

#include "TestBase.h"

#include "gameworld/types/ClassRegistry.h"

#include "mgen/classes/EmptyClassRegistry.h"

#include "mgen/serialization/BinaryWriter.h"
#include "mgen/serialization/BinaryReader.h"
#include "mgen/serialization/VectorInputStream.h"
#include "mgen/serialization/VectorOutputStream.h"
#include "mgen/serialization/JsonPrettyWriter.h"
#include "mgen/serialization/JsonWriter.h"
#include "mgen/serialization/JsonReader.h"


/////////////////////////////////////////////////////////////////////

typedef gameworld::types::basemodule1::Car TestType;
typedef gameworld::types::ClassRegistry ClassRegType;
typedef mgen::EmptyClassRegistry EmptyClassRegType;

struct PerfTestData {
    TestType entity;
    gameworld::types::ClassRegistry registry;
    mgen::EmptyClassRegistry emptyRegistry;
    PerfTestData() {
        entity.getPositioningMutable().setAcceleration(new gameworld::types::basemodule1::VectorR3);
        entity.setId(123);
        entity.setBrand("Skoda");
        entity.setTopSpeed(1337);
        entity.getPositioningMutable().setPosition(gameworld::types::basemodule1::VectorR3(3, 2, 1, gameworld::types::basemodule1::kind_UNKNOWN)).setVelocity(
                gameworld::types::basemodule1::VectorR3(3, 2, 1, gameworld::types::basemodule1::kind_UNKNOWN)).setAcceleration(
                gameworld::types::basemodule1::VectorR3(1, 2, 3, gameworld::types::basemodule1::kind_UNKNOWN));
    }
};

BEGIN_TEST_GROUP(Performance)

/////////////////////////////////////////////////////////////////////

BEGIN_TEST("Binary")

    LOG("");

    PerfTestData testData;
    TestType& entity = testData.entity;
    ClassRegType& registry = testData.registry;
    EmptyClassRegType& emptyRegistry = testData.emptyRegistry;

    // Create output/input streams for serialization
    std::vector<char> buffer;
    mgen::VectorOutputStream out(buffer);
    mgen::VectorInputStream in(buffer);
    mgen::VectorInputStream inNoClasses(buffer);

    // Create our serializers
    mgen::BinaryWriter<mgen::VectorOutputStream, gameworld::types::ClassRegistry> writer(out, registry, true);
    mgen::BinaryReader<mgen::VectorInputStream, gameworld::types::ClassRegistry> reader(in, registry);
    mgen::BinaryReader<mgen::VectorInputStream, mgen::EmptyClassRegistry> readerNoClasses(inNoClasses, emptyRegistry);

    const double t0 = getCurTimeSeconds();
    const int n = 100000;

    long long sum = 0;
    for (int i = 0; i < n; i++) {
        // write it
        buffer.clear();
        inNoClasses.reset();
        writer.writeObject(entity);
        readerNoClasses.readObject();
        sum += (long long) buffer.size();
    }

    const double t1 = getCurTimeSeconds();
    const double dt = t1 - t0;
    const double MB = double(sum) / 1024.0 / 1024.0;
    const double MB_per_sec = MB / dt;
    const int msgs_per_sec = int(double(n) / dt);
    std::cout << "    MB/s (W+R): " << MB_per_sec << std::endl;
    std::cout << "    msgs/s: " << msgs_per_sec << std::endl;

    // Read it
    const TestType * entityBack = reader.readObject<TestType>();

    ASSERT(entityBack);
    ASSERT(entity == *entityBack);

    // Check typed reading
    writer.writeObject(entity);
    ASSERT(entity == reader.readStatic<TestType>());

END_TEST

/////////////////////////////////////////////////////////////////////

BEGIN_TEST("Json")

    LOG("");

    PerfTestData testData;
    TestType& entity = testData.entity;
    ClassRegType& registry = testData.registry;
    EmptyClassRegType& emptyRegistry = testData.emptyRegistry;

    // Create output/input streams for serialization
    std::vector<char> buffer;
    mgen::VectorOutputStream out(buffer);
    mgen::VectorInputStream in(buffer);
    mgen::VectorInputStream inNoClasses(buffer);

    // Create our serializers
    mgen::JsonPrettyWriter<mgen::VectorOutputStream, gameworld::types::ClassRegistry> writer(out, registry, true);
    mgen::JsonReader<mgen::VectorInputStream, gameworld::types::ClassRegistry> reader(in, registry);
    mgen::JsonReader<mgen::VectorInputStream, mgen::EmptyClassRegistry> readerNoClasses(inNoClasses, emptyRegistry);

    writer.writeObject(entity);
    writer.writeObject(entity);

    const std::string json(buffer.data(), buffer.size());

    // Check typed reading
    writer.writeObject(entity);
    writer.writeObject(entity);
    ASSERT(entity == reader.readStatic<TestType>());
    ASSERT(entity == reader.readStatic<TestType>());

    const TestType * back1 = reader.readObject<TestType>();
    const TestType * back2 = reader.readObject<TestType>();

    ASSERT(back1);
    ASSERT(back2);

    ASSERT(*back1 == entity);
    ASSERT(*back2 == entity);

    buffer.clear();
    writer.writeObject(*back2);

    const double t0 = getCurTimeSeconds();
    const int n = 10000;

    long long sum = 0;
    for (int i = 0; i < n; i++) {
        // write it
        buffer.clear();
        inNoClasses.reset();
        writer.writeObject(entity);
        readerNoClasses.readObject();
        sum += (long long) buffer.size();
    }

    const double t1 = getCurTimeSeconds();
    const double dt = t1 - t0;
    const double MB = double(sum) / 1024.0 / 1024.0;
    const double MB_per_sec = MB / dt;
    const int msgs_per_sec = int(double(n) / dt);
    std::cout << "    MB/s (W+R): " << MB_per_sec << std::endl;
    std::cout << "    msgs/s: " << msgs_per_sec << std::endl;

END_TEST

/////////////////////////////////////////////////////////////////////

END_TEST_GROUP

