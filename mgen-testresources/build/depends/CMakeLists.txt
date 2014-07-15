cmake_minimum_required (VERSION 2.8.10)

project(generate_depends_testdata) 

set(MGEN_ROOT ../../..)

include_directories(	
	${CMAKE_CURRENT_SOURCE_DIR}
	${CMAKE_CURRENT_SOURCE_DIR}/../common
	${MGEN_ROOT}/mgen-testresources/generated/depends/src_generated/cpp
	${MGEN_ROOT}/mgen-cpplib/src/main/cpp
) 

add_executable(	
	${PROJECT_NAME} 
	generate_depends_testdata.cpp
)

