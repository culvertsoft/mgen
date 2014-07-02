/*
 * TestBase.h
 *
 *  Created on: 2 jul 2014
 *      Author: GiGurra
 */

#ifndef TESTBASE_H_
#define TESTBASE_H_

/*************************************
 *
 *
 *          INCLUDES
 *
 ************************************/

#include <iostream>
#include "OsTime.h"
#include "mgen/util/stringutil.h"
#include "tut/tut.hpp"
#include "tut/tut_reporter.hpp"

/*************************************
 *
 *
 *          TEST SETUP MACROS
 *
 ************************************/

#define BEGIN_TEST_GROUP(grpName) \
namespace tut { \
struct grpName##TestDataDummy {}; \
typedef test_group<grpName##TestDataDummy> TestGroupType; \
typedef TestGroupType::object testObject; \
TestGroupType grpName##TestGroup(STRINGIFY(grpName)" tests"); \
int grpName##zero = __COUNTER__; \
template<> \
template<> \
void testObject::test<__COUNTER__>() { \
}

#define BEGIN_TEST(tstName) \
template<> \
template<> \
void testObject::test<__COUNTER__>() { \
    set_test_name(STRINGIFY(tstName)); \
    LOG(""); \
    LOG_CONTINUE("  Running test " << get_test_name());

#define END_TEST }
#define END_TEST_GROUP }

/*************************************
 *
 *
 *          MISC HELPER MACROS
 *
 ************************************/

#define LOG_CONTINUE(x) std::cout << x
#define LOG(x) std::cout << x << std::endl
#define ERR(x) std::cerr << x << std::cerr
#define FILE_LINE __FILE__ "::" STRINGIFY(__LINE__) ":"

#define ASSERTMSG(msg, condition) ensure(FILE_LINE ":" msg, condition)
#define ASSERT(condition) ensure(FILE_LINE, condition)

#endif /* TESTBASE_H_ */
