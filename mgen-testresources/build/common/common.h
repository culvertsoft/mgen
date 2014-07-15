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

#define MK_MGEN_OUTPUT(ClassRegType, WriterType, compact) \
    const ClassRegType classRegistry_out; \
    std::vector<char> buffer; \
    mgen::VectorOutputStream outputstream(buffer); \
    WriterType<mgen::VectorOutputStream, ClassRegType> writer(outputstream, classRegistry_out, compact);

#define MK_MGEN_INPUT(ClassRegType, ReaderType) \
    const ClassRegType classRegistry_in; \
    mgen::VectorInputStream inputstream(buffer); \
    ReaderType<mgen::VectorInputStream, ClassRegType> reader(inputstream, classRegistry_in);

#endif
