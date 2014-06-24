#include "mgen/serialization/BinaryWriter.h"
#include "mgen/serialization/BinaryReader.h"
#include "mgen/serialization/VectorInputStream.h"
#include "mgen/serialization/VectorOutputStream.h"
#include "mgen/serialization/JSONWriter.h"
#include "mgen/serialization/JSONReader.h"

#include "gameworld/types/ClassRegistry.h"

#include "mgen/classes/EmptyClassRegistry.h"

#include "OsTime.h"

#include <iostream>

#ifdef _MSC_VER
#include <conio.h>
#endif

template<typename ClassRegType, typename TestType>
void testBinary(
        ClassRegType& registry,
        mgen::EmptyClassRegistry& emptyRegistry,
        const TestType& entity) {

    // Create output/input streams for serialization
    std::vector<char> buffer;
    mgen::VectorOutputStream out(buffer);
    mgen::VectorInputStream in(buffer);
    mgen::VectorInputStream inNoClasses(buffer);

    // Create our serializers
    mgen::BinaryWriter<mgen::VectorOutputStream, gameworld::types::ClassRegistry> writer(out, registry);
    mgen::BinaryReader<mgen::VectorInputStream, gameworld::types::ClassRegistry> reader(in, registry);
    mgen::BinaryReader<mgen::VectorInputStream, mgen::EmptyClassRegistry> readerNoClasses(inNoClasses, emptyRegistry);

    const double t0 = getCurTimeSeconds();
    const int n = 100000;

    long long sum = 0;
    for (int i = 0; i < n; i++) {
        // write it
        buffer.clear();
        inNoClasses.reset();
        writer.writeMgenObject(entity);
        readerNoClasses.readMgenObject();
        sum += (long long) buffer.size();
    }

    const double t1 = getCurTimeSeconds();
    const double dt = t1 - t0;
    const double MB = double(sum) / 1024.0 / 1024.0;
    const double MB_per_sec = MB / dt;
    const int msgs_per_sec = int(double(n) / dt);
    std::cout << "BINARY performance " << (sum / 1024) << " kB" << std::endl;
    std::cout << "  MB/s: " << MB_per_sec << std::endl;
    std::cout << "  msgs/s: " << msgs_per_sec << std::endl;

    // Read it
    TestType * entityBack = static_cast<TestType*>(reader.readMgenObject());

    std::cout << "object: " << entityBack << ", typeName: " << entityBack->_typeName() << std::endl;

    std::cout << "EqualityCheck: " << (entity == *entityBack) << std::endl;
    std::cout << "brand: " << (entity.getBrand() == entityBack->getBrand()) << std::endl;
    std::cout << "getId: " << (entity.getId() == entityBack->getId()) << std::endl;
    std::cout << "getNWheels: " << (entity.getNWheels() == entityBack->getNWheels()) << std::endl;
    std::cout << "getTopSpeed: " << (entity.getTopSpeed() == entityBack->getTopSpeed()) << std::endl;
    std::cout << "getPositioning: " << (entity.getPositioning() == entityBack->getPositioning()) << std::endl;
    std::cout << "getAcceleration: " << (entity.getPositioning().getAcceleration() == entityBack->getPositioning().getAcceleration()) << std::endl;
    std::cout << "getPosition: " << (entity.getPositioning().getPosition() == entityBack->getPositioning().getPosition()) << std::endl;
    std::cout << "getVelocity: " << (entity.getPositioning().getVelocity() == entityBack->getPositioning().getVelocity()) << std::endl;

    std::cout << "acc1ptr: " << entity.getPositioning().getAcceleration().get() << std::endl;

}

template<typename ClassRegType, typename TestType>
void testJSON(
        ClassRegType& registry,
        mgen::EmptyClassRegistry& emptyRegistry,
        const TestType& entity) {

    // Create output/input streams for serialization
    std::vector<char> buffer;
    mgen::VectorOutputStream out(buffer);
    mgen::VectorInputStream in(buffer);
    mgen::VectorInputStream inNoClasses(buffer);

    // Create our serializers
    mgen::JSONWriter<mgen::VectorOutputStream, gameworld::types::ClassRegistry> writer(out, registry);
    mgen::JSONReader<mgen::VectorInputStream, gameworld::types::ClassRegistry> reader(in, registry);
    mgen::JSONReader<mgen::VectorInputStream, mgen::EmptyClassRegistry> readerNoClasses(inNoClasses, emptyRegistry);

    writer.writeMgenObject(entity);
    writer.writeMgenObject(entity);

    const std::string json(buffer.data(), buffer.size());

    std::cout << "JSON: " << std::endl;
    std::cout << json << std::endl;

    const TestType * back1 = static_cast<TestType*>(reader.readMgenObject());
    const TestType * back2 = static_cast<TestType*>(reader.readMgenObject());

    std::cout << "Read back ptr: " << back1 << std::endl;
    std::cout << "Read back ptr: " << back2 << std::endl;

    std::cout << "back1 == entity: " << (*back1 == entity) << std::endl;
    std::cout << "back2 == entity: " << (*back2 == entity) << std::endl;
    
    buffer.clear();
    writer.writeMgenObject(*back2);
    const std::string json2(buffer.data(), buffer.size());
        
    std::cout << "JSON2: " << std::endl;
    std::cout << json2 << std::endl;
    
    const double t0 = getCurTimeSeconds();
    const int n = 10000;

    long long sum = 0;
    for (int i = 0; i < n; i++) {
        // write it
        buffer.clear();
        inNoClasses.reset();
        writer.writeMgenObject(entity);
        readerNoClasses.readMgenObject();
        sum += (long long) buffer.size();
    }

    const double t1 = getCurTimeSeconds();
    const double dt = t1 - t0;
    const double MB = double(sum) / 1024.0 / 1024.0;
    const double MB_per_sec = MB / dt;
    const int msgs_per_sec = int(double(n) / dt);
    std::cout << "JSON performance " << (sum / 1024) << " kB" << std::endl;
    std::cout << "  MB/s: " << MB_per_sec << std::endl;
    std::cout << "  msgs/s: " << msgs_per_sec << std::endl;

}

int main() {

    typedef gameworld::types::basemodule1::Car TestType;

    // Instantiate a (code generated) class registry
    gameworld::types::ClassRegistry registry;
    mgen::EmptyClassRegistry emptyRegistry;

    // Make an object and set some fields on it
    TestType entity;
    entity.getPositioningMutable().setAcceleration(new gameworld::types::basemodule1::VectorR3);
    entity.setId(123);
    entity.setBrand("Skoda");
    entity.setTopSpeed(1337);
    entity.getPositioningMutable().setPosition(gameworld::types::basemodule1::VectorR3(3, 2, 1)).setVelocity(
            gameworld::types::basemodule1::VectorR3(3, 2, 1)).setAcceleration(
            gameworld::types::basemodule1::VectorR3(1, 2, 3));

    std::cout << "posSet: " << entity._isPositioningSet(mgen::DEEP) << std::endl;
    std::cout << "valid: " << entity._validate(mgen::DEEP) << std::endl;

    testJSON(registry, emptyRegistry, entity);

    testBinary(registry, emptyRegistry, entity);

#ifdef _MSC_VER
    _getch();
#endif

    return 0;
}
