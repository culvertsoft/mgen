MGEN_COMPILER_JAR = ../mgen-compiler/target/mgen-compiler-assembly-$(MGEN_BUILD_VERSION).jar

all: generate-test-model
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

generate-test-model:
	############################################
	#  GENERATING TEST-MODEL FOR MGEN-JAVALIB  #
	############################################
	java -jar $(MGEN_COMPILER_JAR) -project="../mgen-compiler/src/test/resources/project.xml" -plugin_paths="../mgen-javagenerator/target/"
	
run-tests: generate-test-model
	###################################
	#     TESTING MGEN-JAVALIB        #
	###################################
	sbt test
	
doc:
	#########################################
	#  DOCUMENTATION MGEN-JAVALIB           #
	#########################################
	sbt doc