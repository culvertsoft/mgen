MGEN_COMPILER_JAR = ../mgen-compiler/target/mgen-compiler-assembly-$(MGEN_BUILD_VERSION).jar

all: generate-test-model
	#########################################
	#     BUILDING MGEN-JAVALIB             #
	#########################################
	sbt compile package publish-local

generate-test-model:
	java -jar $(MGEN_COMPILER_JAR) -project="../mgen-compiler/src/test/resources/project.xml" -plugin_paths="../mgen-javagenerator/target/"
	
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
	rm target/* -rf
	rm src_generated/* -rf

test: generate-test-model
	#########################################
	#  DOCUMENTATION MGEN-JAVALIB           #
	#########################################
	sbt test:compile
	
doc:
	#########################################
	#  DOCUMENTATION MGEN-JAVALIB           #
	#########################################
	sbt doc