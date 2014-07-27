#include "TestBase.h"

#include <cmath>
#include <fstream>

#include "gameworld/types/ClassRegistry.h"
#include "mgen/serialization/VectorInputStream.h"
#include "mgen/serialization/VectorOutputStream.h"
#include "mgen/serialization/JsonReader.h"
#include "mgen/serialization/JsonPrettyWriter.h"

/////////////////////////////////////////////////////////////////////

static std::vector<char> readFile(const std::string& fileName) {
    std::ifstream f(fileName.c_str(), std::ios::binary);
    return std::vector<char>(std::istreambuf_iterator<char>(f), std::istreambuf_iterator<char>());
}

static void writeToFile(const std::vector<char>& buffer, const std::string& fileName) {
    std::ofstream f(fileName.c_str(), std::ios::binary);
    f.write(buffer.data(), buffer.size());
}

static const std::string s_fileName = "../src/test/cpp/src/testdata/SpecialCharacters.txt";
static const std::string s_writtenString = "abc123åäö";

/////////////////////////////////////////////////////////////////////

using namespace mgen;
using namespace gameworld::types;
using namespace gameworld::types::basemodule2;

BEGIN_TEST_GROUP(UtfTests)

/////////////////////////////////////////////////////////////////////

BEGIN_TEST("SaveJson")

    ClassRegistry classRegistry;
    StringContainer container;
    container.setStr(s_writtenString);

    std::vector<char> buffer;
    VectorOutputStream stream(buffer);
    JsonPrettyWriter<VectorOutputStream, ClassRegistry> writer(stream, classRegistry);

    writer.writeObject(container);
    writeToFile(buffer, s_fileName);

    ASSERT(s_writtenString.length() > 10);
    ASSERT(s_writtenString.length() == s_writtenString.size());

END_TEST

/////////////////////////////////////////////////////////////////////

BEGIN_TEST("LoadJson")

    ClassRegistry classRegistry;

    const std::vector<char> buffer = readFile(s_fileName);
    VectorInputStream stream(buffer);
    JsonReader<VectorInputStream, ClassRegistry> reader(stream, classRegistry);

    StringContainer container = reader.readStatic<StringContainer>();

    ASSERT(s_writtenString == container.getStr());

END_TEST

/////////////////////////////////////////////////////////////////////

END_TEST_GROUP

