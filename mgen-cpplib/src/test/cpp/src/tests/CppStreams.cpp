#include "TestBase.h"

#include <cmath>
#include <fstream>

#include "gameworld/types/ClassRegistry.h"
#include "mgen/serialization/JsonReader.h"
#include "mgen/serialization/JsonPrettyWriter.h"
#include "mgen/serialization/IstreamInputStream.h"
#include "mgen/serialization/OstreamOutputStream.h"

/////////////////////////////////////////////////////////////////////

using namespace mgen;
using namespace gameworld::types;
using namespace gameworld::types::basemodule2;

BEGIN_TEST_GROUP(CppStreamsTests)

/////////////////////////////////////////////////////////////////////

BEGIN_TEST("ReadWriteFile_std")

    ClassRegistry classRegistry;

    std::fstream iStream("../src/test/cpp/src/testdata/TestConfigDiff.txt");

    JsonReader<std::fstream, ClassRegistry> reader(iStream, classRegistry);

    const AppConfigarion cfg = reader.readStatic<AppConfigarion>();

    ASSERT(cfg.hasDifficulty());
    ASSERT(!cfg.hasAi_threads());
    ASSERT(!cfg.hasCpu_threshold());
    ASSERT(!cfg.hasHost_game());

    ASSERT(cfg.getDifficulty() == Grade_MEDIUM);

    std::fstream oStream("tempfile.txt", std::fstream::out);
    JsonPrettyWriter<std::fstream, ClassRegistry> writer(oStream, classRegistry);
    writer.writeObject(cfg);
    oStream.close();

    std::fstream iStream2("tempfile.txt");
    JsonReader<std::fstream, ClassRegistry> reader2(iStream2, classRegistry);
    const AppConfigarion cfg2 = reader2.readStatic<AppConfigarion>();

    ASSERT(cfg == cfg2);

END_TEST

/////////////////////////////////////////////////////////////////////

BEGIN_TEST("ReadWriteFile_wrapped")

    ClassRegistry classRegistry;

    std::fstream std_iStream("../src/test/cpp/src/testdata/TestConfigDiff.txt");
    IstreamInputStream iStream(std_iStream);

    JsonReader<IstreamInputStream, ClassRegistry> reader(iStream, classRegistry);

    const AppConfigarion cfg = reader.readStatic<AppConfigarion>();

    ASSERT(cfg.hasDifficulty());
    ASSERT(!cfg.hasAi_threads());
    ASSERT(!cfg.hasCpu_threshold());
    ASSERT(!cfg.hasHost_game());

    ASSERT(cfg.getDifficulty() == Grade_MEDIUM);

    std::fstream std_oStream("tempfile2.txt", std::fstream::out);
    OstreamOutputStream oStream(std_oStream);
    JsonPrettyWriter<OstreamOutputStream, ClassRegistry> writer(oStream, classRegistry);
    writer.writeObject(cfg);
    std_oStream.close();

    std::fstream std_iStream2("tempfile2.txt");
    IstreamInputStream iStream2(std_iStream2);
    JsonReader<IstreamInputStream, ClassRegistry> reader2(iStream2, classRegistry);
    const AppConfigarion cfg2 = reader2.readStatic<AppConfigarion>();

    ASSERT(cfg == cfg2);

END_TEST

/////////////////////////////////////////////////////////////////////

END_TEST_GROUP

