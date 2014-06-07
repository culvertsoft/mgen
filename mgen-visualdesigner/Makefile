
MGEN_COMPILER_JAR = ../mgen-compiler/target/mgen-compiler-assembly-0.1-SNAPSHOT.jar

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
	sbt clean

run:	all
	#########################################
	#     STARTING MGEN-VISUALDESIGNER	#
	#########################################
	java -jar target/mgen-visualdesigner-assembly-*

