#include "TestBase.h"

#include "gameworld/types/ClassRegistry.h"
#include "mgen/serialization/MemInputStream.h"
#include "mgen/serialization/VectorOutputStream.h"
#include "mgen/serialization/JsonPrettyWriter.h"
#include "mgen/serialization/JsonReader.h"

/////////////////////////////////////////////////////////////////////

using namespace gameworld::types;
using namespace gameworld::types::basemodule1;
BEGIN_TEST_GROUP(MemInput)

/////////////////////////////////////////////////////////////////////


BEGIN_TEST("Test Mem Input")

	ClassRegistry classRegistry;

	std::vector<char> buffer;

	mgen::VectorOutputStream outStream(buffer);
	mgen::JsonPrettyWriter<mgen::VectorOutputStream, ClassRegistry> writer(outStream, classRegistry);

	Car car;
	car._setAllFieldsSet(true, mgen::DEEP);
	writer.writeObject(car);

	std::string stringInputBuf(buffer.data(), buffer.size());
	mgen::MemInputStream inStream1(buffer);
	mgen::MemInputStream inStream2(buffer.data(), buffer.size());
	mgen::MemInputStream inStream3(stringInputBuf);

	mgen::JsonReader<mgen::MemInputStream, ClassRegistry> reader1(inStream1, classRegistry);
	mgen::JsonReader<mgen::MemInputStream, ClassRegistry> reader2(inStream2, classRegistry);
	mgen::JsonReader<mgen::MemInputStream, ClassRegistry> reader3(inStream3, classRegistry);

	Car car1a = reader1.readStatic<Car>();
	Car car2a = reader2.readStatic<Car>();
	Car car3a = reader3.readStatic<Car>();

	inStream1.setInput(buffer);
	inStream2.setInput(buffer.data(), buffer.size());
	inStream3.setInput(stringInputBuf);

	Car car1b = reader1.readStatic<Car>();
	Car car2b = reader2.readStatic<Car>();
	Car car3b = reader3.readStatic<Car>();

	ASSERT(car == car1a);
	ASSERT(car == car2a);
	ASSERT(car == car3a);

	ASSERT(car == car1b);
	ASSERT(car == car2b);
	ASSERT(car == car3b);

END_TEST

/////////////////////////////////////////////////////////////////////

END_TEST_GROUP

