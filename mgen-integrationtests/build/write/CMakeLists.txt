cmake_minimum_required (VERSION 2.8.10)

project(generate_write_testdata) 

set(MGEN_ROOT ../../..)

include_directories(
	${MGEN_ROOT}/mgen-integrationtests/build/common
	${MGEN_ROOT}/mgen-integrationtests/generated/write/src_generated/cpp
	${MGEN_ROOT}/mgen-cpplib/src/main/cpp
	${CMAKE_CURRENT_SOURCE_DIR}
) 

add_executable(	
	${PROJECT_NAME} 
	generate_write_testdata.cpp
)

