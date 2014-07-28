cmake_minimum_required (VERSION 2.8.10)

project(mgen-cpplib-test) 

include_directories(	
	${CMAKE_CURRENT_SOURCE_DIR}/../../../main/cpp
	${CMAKE_CURRENT_SOURCE_DIR}/../../../../src_generated/test/cpp
	${CMAKE_CURRENT_SOURCE_DIR}/../../../test/cpp/include
	${CMAKE_CURRENT_SOURCE_DIR}
) 

add_executable(	
	${PROJECT_NAME} 
	MGenCpp_test.cpp
	mgen_unity_build.cpp	
	OsTime.cpp		
	tests/ObjectManipulation.cpp
	tests/Serialization.cpp
	tests/Performance.cpp
	tests/WhitepaperExample.cpp
	tests/ExtremeValues.cpp
	tests/ConfigFileTest.cpp
	tests/UtfTests.cpp
	tests/TransientFields.cpp
)

