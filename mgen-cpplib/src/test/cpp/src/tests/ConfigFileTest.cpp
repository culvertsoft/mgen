#include "TestBase.h"

#include <fstream>

#include "gameworld/types/ClassRegistry.h"
#include "mgen/serialization/VectorInputStream.h"
#include "mgen/serialization/JsonReader.h"

/////////////////////////////////////////////////////////////////////

static std::vector<char> readFile(const std::string& fileName) {
    std::ifstream f(fileName.c_str(), std::ios::binary);
    return std::vector<char>(std::istreambuf_iterator<char>(f), std::istreambuf_iterator<char>());
}

/////////////////////////////////////////////////////////////////////

using namespace mgen;
using namespace gameworld::types;
using namespace gameworld::types::basemodule2;

BEGIN_TEST_GROUP(ConfigFileTests)

/////////////////////////////////////////////////////////////////////

BEGIN_TEST("TestHaveDifficultyParameter")

    ClassRegistry classRegistry;

    const std::vector<char> diffData = readFile("../src/test/cpp/src/testdata/TestConfigDiff.txt");
    ASSERT(!diffData.empty());

    VectorInputStream stream(diffData);
    JsonReader<VectorInputStream, ClassRegistry> reader(stream, classRegistry);

    const AppConfigarion cfg = reader.readStatic<AppConfigarion>();

    ASSERT(cfg.hasDifficulty());
    ASSERT(!cfg.hasAi_threads());
    ASSERT(!cfg.hasCpu_threshold());
    ASSERT(!cfg.hasHost_game());

END_TEST

/////////////////////////////////////////////////////////////////////

BEGIN_TEST("TestHaveAllParameters")

    ClassRegistry classRegistry;

    const std::vector<char> diffData = readFile("../src/test/cpp/src/testdata/TestConfigAll.txt");
    ASSERT(!diffData.empty());

    VectorInputStream stream(diffData);
    JsonReader<VectorInputStream, ClassRegistry> reader(stream, classRegistry);

    const AppConfigarion cfg = reader.readStatic<AppConfigarion>();

    ASSERT(cfg.hasDifficulty());
    ASSERT(cfg.hasAi_threads());
    ASSERT(cfg.hasCpu_threshold());
    ASSERT(cfg.hasHost_game());

END_TEST

/////////////////////////////////////////////////////////////////////

END_TEST_GROUP

