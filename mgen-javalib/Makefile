MGEN_COMPILER_JAR = ../mgen-compiler/target/mgen-compiler-assembly-$(MGEN_BUILD_VERSION).jar

all: generate-test-models
	#########################################
	#     BUILDING MGEN-JAVALIB             #
	#########################################
	sbt compile package publish-local

eclipse:
	#############################################
	# CREATING ECLIPSE PROJECT FOR MGEN-JAVALIB #
	#############################################
	sbt eclipse

clean: 	
	#########################################
	#     CLEANING MGEN-JAVALIB             #
	#########################################
	sbt clean
	rm -rf target/*
	rm -rf src_generated/*

generate-test-models:
	#############################################
	#  GENERATING TEST-MODELS FOR MGEN-JAVALIB  #
	#############################################
	java -jar $(MGEN_COMPILER_JAR) -project="../mgen-compiler/src/test/resources/project.xml" -plugin_paths="../mgen-javagenerator/target/"	
	java -jar $(MGEN_COMPILER_JAR) -project="../mgen-compiler/src/test/resources/transient_testmodel/project.xml" -plugin_paths="../mgen-javagenerator/target/"
	java -jar $(MGEN_COMPILER_JAR) -project="../mgen-compiler/src/test/resources/defaultvalues_testmodel/project.xml" -plugin_paths="../mgen-javagenerator/target/"
	java -jar $(MGEN_COMPILER_JAR) -project="../mgen-compiler/src/test/resources/defaultvaluesreq_testmodel/project.xml" -plugin_paths="../mgen-javagenerator/target/"
	
run-tests: generate-test-models
	###################################
	#     TESTING MGEN-JAVALIB        #
	###################################
	sbt test

publish:
	sbt compile package publish-signed