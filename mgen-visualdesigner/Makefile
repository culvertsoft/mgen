
MGEN_COMPILER_JAR = ../mgen-compiler/target/mgen-compiler-assembly-$(MGEN_BUILD_VERSION).jar

all: 	
	#########################################
	# GENERATING MGEN-VISUALDESIGNER MODEL	#
	#########################################
	java -jar $(MGEN_COMPILER_JAR) -project="model/project.xml" -plugin_paths="../mgen-javagenerator/target/"
	#########################################
	#     BUILDING MGEN-VISUALDESIGNER 	#
	#########################################
	sbt compile package assembly publish-local

clean:
	#########################################
	#     CLEANING MGEN-VISUALDESIGNER	#
	#########################################
	rm src_generated -rf
	touch .cache && rm .cache
	sbt clean

eclipse:
	####################################################
	# CREATING ECLIPSE PROJECT FOR MGEN-VISUALDESIGNER #
	####################################################
	sbt eclipse

run:	all
	#########################################
	#     STARTING MGEN-VISUALDESIGNER	#
	#########################################
	java -jar target/mgen-visualdesigner-assembly-$(MGEN_BUILD_VERSION).jar

