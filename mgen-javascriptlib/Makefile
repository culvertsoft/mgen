VERSION = SNAPSHOT
export MGEN_BUILD_VERSION = $(VERSION)
MGEN_COMPILER_JAR = ../mgen-compiler/target/mgen-compiler-assembly-$(MGEN_BUILD_VERSION).jar

all: generate-test-model
	#########################################
	#     BUILDING MGEN-JAVASCRIPTLIB       #
	#########################################
	sbt compile

clean: 	
	#########################################
	#     CLEANING MGEN-JAVALIB             #
	#########################################
	sbt clean
	rm -rf target
	rm -rf src_generated

generate-test-model:
	############################################
	#  GENERATING TEST-MODEL FOR MGEN-JAVALIB  #
	############################################
	java -jar $(MGEN_COMPILER_JAR) -project="../mgen-compiler/src/test/resources/project.xml" -plugin_paths="../mgen-javascriptgenerator/target/"
	
run-tests: generate-test-model
	###################################
	#     TESTING MGEN-JAVALIB        #
	###################################
	sbt jasmine-gen-runner
	sbt test
