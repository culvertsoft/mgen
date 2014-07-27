#ifndef MGEN_INTEGRATIONTESTS_COMMON_H_
#define MGEN_INTEGRATIONTESTS_COMMON_H_

#include <vector>
#include <string>
#include <fstream>
#include <iostream>

#include "mgen/classes/EmptyClassRegistry.h"

#include "mgen/serialization/BinaryWriter.h"
#include "mgen/serialization/BinaryReader.h"
#include "mgen/serialization/VectorInputStream.h"
#include "mgen/serialization/VectorOutputStream.h"
#include "mgen/serialization/JsonPrettyWriter.h"
#include "mgen/serialization/JsonWriter.h"
#include "mgen/serialization/JsonReader.h"

#include "MGenObjectRandomizer.h"

#undef NDEBUG
#include <cassert>

inline void writeToFile(const std::string& fileName, const std::vector<char>& data) {
    std::ofstream file(fileName.c_str(), std::ios::binary);
    file.write(data.data(), data.size());
}

#define MK_MGEN_OUTPUT(name, ClassRegType, WriterType, compact) \
    WriterType<mgen::VectorOutputStream, ClassRegType> name##Writer(outputstream, classRegistry, compact);

#define MK_MGEN_INPUT(name, ClassRegType, ReaderType) \
    ReaderType<mgen::VectorInputStream, ClassRegType> name##Reader(inputstream, classRegistry);

#define MK_MGEN_INPUT_EMPTY(name, ClassRegType, ReaderType) \
    ReaderType<mgen::VectorInputStream, ClassRegType> name##Reader##Empty(inputstream, emptyClassRegistry);

#define SETUP_WRITERS_AND_READERS(ClassRegType) \
    const mgen::EmptyClassRegistry emptyClassRegistry; \
    const ClassRegType classRegistry; \
    std::vector<char> buffer; \
    mgen::VectorOutputStream outputstream(buffer); \
    mgen::VectorInputStream inputstream(buffer); \
    MK_MGEN_OUTPUT(json, ClassRegType, mgen::JsonWriter, false) \
    MK_MGEN_OUTPUT(jsonCompact, ClassRegType, mgen::JsonWriter, true) \
    MK_MGEN_OUTPUT(jsonPretty, ClassRegType, mgen::JsonPrettyWriter, false) \
    MK_MGEN_OUTPUT(jsonPrettyCompact, ClassRegType, mgen::JsonPrettyWriter, true) \
    MK_MGEN_OUTPUT(binary, ClassRegType, mgen::BinaryWriter, false) \
    MK_MGEN_OUTPUT(binaryCompact, ClassRegType, mgen::BinaryWriter, true) \
    MK_MGEN_INPUT(json, ClassRegType, mgen::JsonReader) \
    MK_MGEN_INPUT(binary, ClassRegType, mgen::BinaryReader) \
    MK_MGEN_INPUT_EMPTY(json, mgen::EmptyClassRegistry, mgen::JsonReader) \
    MK_MGEN_INPUT_EMPTY(binary, mgen::EmptyClassRegistry, mgen::BinaryReader)

#define FOR_EACH_SERIALIZER(f) \
    f(classRegistry, buffer, outputstream, inputstream, "json", jsonWriter, jsonReader, false); \
    f(classRegistry, buffer, outputstream, inputstream, "jsonCompact", jsonCompactWriter, jsonReader, true); \
    f(classRegistry, buffer, outputstream, inputstream, "jsonPretty", jsonPrettyWriter, jsonReader, false); \
    f(classRegistry, buffer, outputstream, inputstream, "jsonPrettyCompact", jsonPrettyCompactWriter, jsonReader, true); \
    f(classRegistry, buffer, outputstream, inputstream, "binary", binaryWriter, binaryReader, false); \
    f(classRegistry, buffer, outputstream, inputstream, "binaryCompact", binaryCompactWriter, binaryReader, true);

#define FOR_EACH_SERIALIZER_EMPTY_CLASSREG(f) \
    f(classRegistry, buffer, outputstream, inputstream, "json", jsonWriter, jsonReaderEmpty, false); \
    f(classRegistry, buffer, outputstream, inputstream, "jsonCompact", jsonCompactWriter, jsonReaderEmpty, true); \
    f(classRegistry, buffer, outputstream, inputstream, "jsonPretty", jsonPrettyWriter, jsonReaderEmpty, false); \
    f(classRegistry, buffer, outputstream, inputstream, "jsonPrettyCompact", jsonPrettyCompactWriter, jsonReaderEmpty, true); \
    f(classRegistry, buffer, outputstream, inputstream, "binary", binaryWriter, binaryReaderEmpty, false); \
    f(classRegistry, buffer, outputstream, inputstream, "binaryCompact", binaryCompactWriter, binaryReaderEmpty, true);

template<typename ClassRegType, typename OutStreamType, typename InStreamType, typename WriterType, typename ReaderType>
inline void mkEmptyObjects(
        const ClassRegType& classRegistry,
        std::vector<char>& buffer,
        OutStreamType& outputStream,
        InStreamType& inputStream,
        const std::string& serializerName,
        WriterType& writer,
        ReaderType& reader,
        const bool isCompact) {

    const typename ClassRegType::EntryMap& entries = classRegistry.entries();

    std::cout << "Running mkEmptyObjects for " << serializerName << " serializer" << std::endl;
    for (typename ClassRegType::EntryMap::const_iterator it = entries.begin(); it != entries.end(); it++) {
        mgen::MGenBase * instance = it->second.newInstance();
        instance->_setAllFieldsSet(true, mgen::DEEP);
        writer.writeObject(*instance);
        std::cout << "  " << instance->_typeName() << std::endl;
        mgen::MGenBase * instanceBack = reader.readObject();
        assert(instance->_equals(*instanceBack));
        delete instanceBack;
        delete instance;
    }

    writeToFile(std::string("../data_generated/emptyObjects_").append(serializerName).append(".data"), buffer);

    outputStream.reset();
    inputStream.reset();
    buffer.clear();

}

template<typename ClassRegType, typename OutStreamType, typename InStreamType, typename WriterType, typename ReaderType>
inline void mkRandomObjects(
        const ClassRegType& classRegistry,
        std::vector<char>& buffer,
        OutStreamType& outputStream,
        InStreamType& inputStream,
        const std::string& serializerName,
        WriterType& writer,
        ReaderType& reader,
        const bool isCompact) {

    mgen::ObjectRandomizer<ClassRegType> randomizer(classRegistry);

    const typename ClassRegType::EntryMap& entries = classRegistry.entries();

    std::cout << "Running mkRandomObjects for " << serializerName << " serializer" << std::endl;
    for (typename ClassRegType::EntryMap::const_iterator it = entries.begin(); it != entries.end(); it++) {
        mgen::MGenBase * instance = it->second.newInstance();
        instance->_setAllFieldsSet(true, mgen::DEEP);
        randomizer.randomizeObject(*instance);
        writer.writeObject(*instance);
        std::cout << "  " << instance->_typeName() << std::endl;
        mgen::MGenBase * instanceBack = reader.readObject();
        assert(instance->_equals(*instanceBack));
        delete instanceBack;
        delete instance;
    }

    writeToFile(std::string("../data_generated/randomizedObjects_").append(serializerName).append(".data"), buffer);

    outputStream.reset();
    inputStream.reset();
    buffer.clear();

}

template<typename ClassRegType, typename OutStreamType, typename InStreamType, typename WriterType, typename ReaderType>
inline void skipAllObjects(
        const ClassRegType& classRegistry,
        std::vector<char>& buffer,
        OutStreamType& outputStream,
        InStreamType& inputStream,
        const std::string& serializerName,
        WriterType& writer,
        ReaderType& reader,
        const bool isCompact) {

    mgen::ObjectRandomizer<ClassRegType> randomizer(classRegistry);

    const typename ClassRegType::EntryMap& entries = classRegistry.entries();

    std::cout << "Running skipAllObjects for " << serializerName << " serializer" << std::endl;
    for (typename ClassRegType::EntryMap::const_iterator it = entries.begin(); it != entries.end(); it++) {
        mgen::MGenBase * instance = it->second.newInstance();
        instance->_setAllFieldsSet(true, mgen::DEEP);
        randomizer.randomizeObject(*instance);
        writer.writeObject(*instance);
        std::cout << "  " << instance->_typeName() << std::endl;
        mgen::MGenBase * instanceBack = reader.readObject();
        assert(!instanceBack);
        delete instance;
    }

    outputStream.reset();
    inputStream.reset();
    buffer.clear();

}

#endif
