/*
 * ObjectManipulation.cpp
 *
 *  Created on: 2 jul 2014
 *      Author: GiGurra
 */

#include "TestBase.h"
#include "gameworld/types/ClassRegistry.h"

using namespace gameworld::types;
using namespace gameworld::types::basemodule1;

struct ManipTestData {
};

BEGIN_TEST_GROUP(ObjectManipulation)

/////////////////////////////////////////////////////////////////////

BEGIN_TEST("Create some objects")

    Car car;
    Vehicle vehicle;
    ASSERT(vehicle == car);

END_TEST

/////////////////////////////////////////////////////////////////////

BEGIN_TEST("Delete some objects")

    ClassRegistry registry;
    const ClassRegistry::EntryMap& entries = registry.entries();

    for (ClassRegistry::EntryMap::const_iterator it = entries.begin(); it != entries.end(); it++) {
        mgen::MGenBase * object = it->second.newInstance();
        ASSERT(object);
        delete object;
    }

END_TEST

/////////////////////////////////////////////////////////////////////

BEGIN_TEST("Compare some objects")

    ClassRegistry registry;
    const ClassRegistry::EntryMap& entries = registry.entries();

    for (ClassRegistry::EntryMap::const_iterator it = entries.begin(); it != entries.end(); it++) {
        mgen::MGenBase * object1 = it->second.newInstance();
        mgen::MGenBase * object2 = it->second.newInstance();
        ASSERT(object1);
        ASSERT(object2);
        ASSERT(object1 != object2);
        ASSERT(object1->_equals(*object2));
        delete object1;
        delete object2;
    }

END_TEST

/////////////////////////////////////////////////////////////////////

END_TEST_GROUP

