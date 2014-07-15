cmake_minimum_required (VERSION 2.8.10)

project(generate_testdata) 

set(MGEN_ROOT ../../..)

include_directories(	
	${CMAKE_CURRENT_SOURCE_DIR}
	${MGEN_ROOT}/mgen-cpplib/src/main/cpp
) 

add_executable(	
	${PROJECT_NAME} 
	generate_testdata.cpp
)

