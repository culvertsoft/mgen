#ifndef MGEN_TESTRESOURCES_COMMON_H_
#define MGEN_TESTRESOURCES_COMMON_H_

#include <vector>
#include <string>
#include <fstream>

#include "mgen/serialization/BinaryWriter.h"
#include "mgen/serialization/BinaryReader.h"
#include "mgen/serialization/VectorInputStream.h"
#include "mgen/serialization/VectorOutputStream.h"
#include "mgen/serialization/JsonPrettyWriter.h"
#include "mgen/serialization/JsonWriter.h"
#include "mgen/serialization/JsonReader.h"

inline void writeToFile(const std::string& fileName, const std::vector<char>& data) {
    std::ofstream file(fileName.c_str(), std::ios::binary);
    file.write(data.data(), data.size());
}

#define MK_MGEN_OUTPUT(name, ClassRegType, WriterType, compact) \
    WriterType<mgen::VectorOutputStream, ClassRegType> name##Writer(outputstream, classRegistry, compact);

#define MK_MGEN_INPUT(name, ClassRegType, ReaderType) \
    ReaderType<mgen::VectorInputStream, ClassRegType> name##Reader(inputstream, classRegistry);

#define SETUP_WRITERS_AND_READERS(ClassRegType) \
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
    MK_MGEN_INPUT(binary, ClassRegType, mgen::BinaryReader)

#define FOR_EACH_SERIALIZER(f) \
    f(classRegistry, buffer, outputstream, inputstream, "json", jsonWriter, jsonReader, false); \
    f(classRegistry, buffer, outputstream, inputstream, "jsonCompact", jsonCompactWriter, jsonReader, true); \
    f(classRegistry, buffer, outputstream, inputstream, "jsonPretty", jsonPrettyWriter, jsonReader, false); \
    f(classRegistry, buffer, outputstream, inputstream, "jsonPrettyCompact", jsonPrettyCompactWriter, jsonReader, true); \
    f(classRegistry, buffer, outputstream, inputstream, "binary", binaryWriter, binaryReader, false); \
    f(classRegistry, buffer, outputstream, inputstream, "binaryCompact", binaryCompactWriter, binaryReader, true);

#endif
