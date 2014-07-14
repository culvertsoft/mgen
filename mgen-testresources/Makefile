
MGEN_COMPILER_JAR = ../mgen-compiler/target/mgen-compiler-assembly-$(MGEN_BUILD_VERSION).jar

all: generate-models
	##################################
	#   BUILDING MGEN-TESTRESOURCES  #
	##################################

generate-models:
	############################################
	# GENERATING MODELS FOR MGEN-TESTRESOURCES #
	############################################

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
    
