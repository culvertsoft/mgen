
MGEN_COMPILER_JAR = ../mgen-compiler/target/mgen-compiler-assembly-$(MGEN_BUILD_VERSION).jar

all: generate-test-model
	##################################################
	#   NO BUILD REQUIRED FOR MGEN-CPPLIB (.h-only)  #
	##################################################

eclipse:
	#################################################
	# CREATING ECLIPSE PROJECT FOR MGEN-CPPLIB-TEST #
	#################################################
	cd target && \
	mkdir -p eclipse-project &&  \
	cd eclipse-project && \
	cmake -G"Eclipse CDT4 - Unix Makefiles" -D CMAKE_BUILD_TYPE=Debug ../../src/test/cpp/src/

clean:
	#########################################
	#   CLEANING MGEN-CPPLIB-TEST           #
	#########################################
	rm -rf src_generated/*
	rm -rf target/*

doc:
	#########################################
	#   DOCUMENTAION MGEN-CPPLIB            #
	#########################################
    #NOT_YET_IMPLEMENTED#
	    
create-target-directory:
	#########################################
	#  CREATING MGEN-CPPLIB-TEST TARG.DIR.  #
	#########################################
	mkdir -p target
    
generate-test-model:	create-target-directory
	#########################################
	#   GENERATING MGEN-CPPLIB-TEST-MODEL   #
	#########################################
	java -jar $(MGEN_COMPILER_JAR) -project="../mgen-compiler/src/test/resources/project.xml" -plugin_paths="../mgen-cppgenerator/target/"

build-test:	generate-test-model
	#########################################
	#       BUILDING MGEN-CPPLIB-TESTS      #
	#########################################
	cd target && cmake -DCMAKE_BUILD_TYPE=Release -G "Unix Makefiles" ../src/test/cpp/src/
	cd target && make all

run-tests: build-test
	##################################
	#      TESTING MGEN-CPP-LIB      #
	##################################
	cd target && ./mgen-cpplib-test
    
