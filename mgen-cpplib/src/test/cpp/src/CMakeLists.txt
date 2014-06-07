cmake_minimum_required (VERSION 2.8.10)

project(mgen-cpplib-test) 

include_directories(	${CMAKE_CURRENT_SOURCE_DIR}/../../../main/cpp
			${CMAKE_CURRENT_SOURCE_DIR}/../../../../target/src_generated
			${CMAKE_CURRENT_SOURCE_DIR}
						) 

add_executable(	${PROJECT_NAME} 
		MGenCpp_test.cpp
		mgen_unity_build.cpp	
		OsTime.cpp		)

