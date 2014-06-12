
MGEN_COMPILER_JAR = ../mgen-compiler/target/mgen-compiler-assembly-$(MGEN_BUILD_VERSION).jar

all:	build-test

build-test:	generated-model
	#########################################
	#   BUILDING MGEN-CPPLIB-TEST           #
	#########################################
	cd target && cmake -G "Unix Makefiles" ../src/test/cpp/src/
	cd target && make all

generated-model:	create-target-directory
	#########################################
	#   GENERATING MGEN-CPPLIB-TEST-MODEL   #
	#########################################
	java -jar $(MGEN_COMPILER_JAR) -project="../mgen-compiler/src/test/resources/project.xml" -plugin_paths="../mgen-cppgenerator/target/"

create-target-directory:
	#########################################
	#  CREATING MGEN-CPPLIB-TEST TARG.DIR.  #
	#########################################
	mkdir -p target

eclipse:
	############################################
	# CREATING ECLIPSE PROJECT FOR MGEN-CPPLIB #
	############################################
	echo - NOT YET IMPLEMENTED -

clean:
	#########################################
	#   CLEANING MGEN-CPPLIB-TEST           #
	#########################################
	rm target/* -rf

doc:

	#########################################
	#   DOCUMENTAION MGEN-CPPLIB            #
	#########################################

	############################################
	#### !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ####
	#### !! NO DOCUMENTATION IMPLEMENTED !! ####
	#### !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ####
	############################################
	

