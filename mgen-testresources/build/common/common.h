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

void writeToFile(const std::string& fileName, const std::vector<char>& data) {
    std::ofstream file(fileName.c_str(), std::ios::binary);
    file.write(data.data(), data.size());
}

#endif
