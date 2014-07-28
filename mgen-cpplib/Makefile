
MGEN_COMPILER_JAR = ../mgen-compiler/target/mgen-compiler-assembly-$(MGEN_BUILD_VERSION).jar

all:
	##################################################
	#   NO BUILD REQUIRED FOR MGEN-CPPLIB (.h-only)  #
	##################################################

eclipse: generate-test-models
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

generate-test-models:
	##########################################
	#   GENERATING MGEN-CPPLIB-TEST-MODELS   #
	##########################################
	mkdir -p target
	java -jar $(MGEN_COMPILER_JAR) -project="../mgen-compiler/src/test/resources/project.xml" -plugin_paths="../mgen-cppgenerator/target/"
	java -jar $(MGEN_COMPILER_JAR) -project="../mgen-compiler/src/test/resources/transient_testmodel/project.xml" -plugin_paths="../mgen-cppgenerator/target/"

build-test:	generate-test-models
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
    
