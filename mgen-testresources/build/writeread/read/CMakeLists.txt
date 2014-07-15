cmake_minimum_required (VERSION 2.8.10)

project(generate_read_testdata) 

set(MGEN_ROOT ../../../..)

include_directories(	
	${CMAKE_CURRENT_SOURCE_DIR}/../../../../mgen-cpplib/src/main/cpp
	${CMAKE_CURRENT_SOURCE_DIR}/../../common
	${MGEN_ROOT}/mgen-testresources/generated/writeread/read/src_generated/cpp
	${CMAKE_CURRENT_SOURCE_DIR}
) 

add_executable(	
	${PROJECT_NAME} 
	generate_read_testdata.cpp
)

