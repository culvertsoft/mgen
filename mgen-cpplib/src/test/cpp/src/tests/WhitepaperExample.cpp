#include "TestBase.h"

#include "mgen/serialization/VectorInputStream.h"
#include "mgen/serialization/VectorOutputStream.h"
#include "mgen/serialization/JsonWriter.h"
#include "mgen/serialization/JsonReader.h"

#include "gameworld/types/ClassRegistry.h"
#include "gameworld/types/Dispatcher.h"

/////////////////////////////////////////////////////////////////////

using namespace gameworld::types;
using namespace gameworld::types::basemodule1;

using mgen::VectorOutputStream;
using mgen::VectorInputStream;
using mgen::JsonWriter;
using mgen::JsonReader;
using mgen::VectorOutputStream;
using gameworld::types::ClassRegistry;

BEGIN_TEST_GROUP(WhitepaperExample)

/////////////////////////////////////////////////////////////////////

BEGIN_TEST("Example1")

// Create some objects and set some fields on these. We can use
// generated setter-functions or constructors
	Car car1, car2;
	car1.setBrand("Ford").setTopSpeed(321);
	car2.setBrand("BMW").setTopSpeed(123);
	Car car3(100, "Volvo");
	Car car4(123, "Mercedes");

	// Create a class registry. This object is immutable and thread safe,
	// so you could have one global instance if you wanted to for your
	// entire application
	ClassRegistry classRegistry;

	// We will serialize our objects to this buffer...
	std::vector<char> buffer;

	// ...by wrapping it in an mgen compatible output stream.
	// MGen readers an writers expect data sinks and sources ("streams")
	// with a data read/write API similar to berkeley sockets:
	//      read(char* trg, const int n)
	//      write(const char* src, const int n)
	VectorOutputStream out(buffer);

	// Now we're ready to create a serializer/writer
	JsonWriter<VectorOutputStream, ClassRegistry> writer(out, classRegistry);

	// Write the objects a few times
	writer.writeObject(car1);
	writer.writeObject(car2);
	writer.writeObject(car3);
	writer.writeObject(car4);

	// -- Imagine some code here shuffling objects --
	// -- around, to disk or over network --

	// Then create an mgen compatible InputStream around your data source
	VectorInputStream in(buffer);

	// And create a deserializer/reader
	JsonReader<VectorInputStream, ClassRegistry> reader(in, classRegistry);

	// Now we have some options on how we wan't to read them back.

	// If we don't know at all what kind of objects to expect we
	// should read to heap memory
	mgen::MGenBase * object = reader.readObject();

	// But if we do know what do expect, we can let the reader
	// provide the correct directly
	Car * carBack = reader.readObject<Car>();

	// We could also read it back to a base type pointer like
	// and identify the specific type later
	Vehicle * anyVehicle = reader.readObject<Vehicle>();

	// Now if we're heap-allergic we could also just read it back to the stack
	// But that will discard any potential subtype information
	Car stackCar = reader.readStatic<Car>();

END_TEST

/////////////////////////////////////////////////////////////////////

static int s_nCars = 0;
static int s_nVehicles = 0;
static int s_nEntities = 0;

BEGIN_TEST("Dispatch/Handler")

	// Create some test objects to dispatch
	Car car(123, "Ford");
	Vehicle vehicle(321);
	Item item;

	// Define a custom handler class
	// that exdends the generated Handler class
	class MyHandler: public Handler {
	public:
		void handle(Car& car) {
			s_nCars++;
		}
		void handle(Vehicle& car) {
			s_nVehicles++;
		}
		void handle(Entity& entity) {
			s_nEntities++;
		}
	};

	// Instantiate a handler and dispatch
	// some objects to it
	MyHandler handler;
	dispatch(car, handler);
	dispatch(vehicle, handler);
	dispatch(item, handler);

	ASSERT(s_nCars == 1);
	ASSERT(s_nVehicles == 1);
	ASSERT(s_nEntities == 1);

END_TEST

/////////////////////////////////////////////////////////////////////

END_TEST_GROUP

