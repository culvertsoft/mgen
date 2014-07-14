cmake_minimum_required (VERSION 2.8.10)

project(generate_testdata) 

include_directories(	
	${CMAKE_CURRENT_SOURCE_DIR}/../../../../mgen-cpplib/src/main/cpp
	${CMAKE_CURRENT_SOURCE_DIR}
) 

add_executable(	
	${PROJECT_NAME} 
	generate_testdata.cpp
)

