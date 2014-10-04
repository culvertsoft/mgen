#include "TestBase.h"

#include <cmath>
#include <fstream>

#include "gameworld/types/ClassRegistry.h"
#include "mgen/serialization/VectorInputStream.h"
#include "mgen/serialization/JsonReader.h"

/////////////////////////////////////////////////////////////////////

using namespace mgen;
using namespace gameworld::types;
using namespace gameworld::types::basemodule2;

BEGIN_TEST_GROUP(ConfigFileTests)

/////////////////////////////////////////////////////////////////////

BEGIN_TEST("TestHaveDifficultyParameter")

    ClassRegistry classRegistry;
    std::ifstream stream("../src/test/cpp/src/testdata/TestConfigDiff.txt");
    JsonReader<std::ifstream, ClassRegistry> reader(stream, classRegistry);

    const AppConfigarion cfg = reader.readStatic<AppConfigarion>();

    ASSERT(cfg.hasDifficulty());
    ASSERT(!cfg.hasAi_threads());
    ASSERT(!cfg.hasCpu_threshold());
    ASSERT(!cfg.hasHost_game());

    ASSERT(cfg.getDifficulty() == Grade_MEDIUM);

END_TEST

/////////////////////////////////////////////////////////////////////

BEGIN_TEST("TestHaveAllParameters")

    ClassRegistry classRegistry;
    std::ifstream stream("../src/test/cpp/src/testdata/TestConfigAll.txt");
    JsonReader<std::ifstream, ClassRegistry> reader(stream, classRegistry);

    const AppConfigarion cfg = reader.readStatic<AppConfigarion>();

    ASSERT(cfg.hasDifficulty());
    ASSERT(cfg.hasAi_threads());
    ASSERT(cfg.hasCpu_threshold());
    ASSERT(cfg.hasHost_game());

    ASSERT(cfg.getDifficulty() == Grade_HIGH);
    ASSERT(cfg.getAi_threads() == 4);
    ASSERT(cfg.getHost_game() == true);
    ASSERT(std::abs(cfg.getCpu_threshold() - 0.9) < 1e-5);

END_TEST

/////////////////////////////////////////////////////////////////////

END_TEST_GROUP

