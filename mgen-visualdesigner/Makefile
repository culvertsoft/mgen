
COMPILER_JAR = ../mgen-compiler/target/mgen-compiler-assembly-0.1-SNAPSHOT.jar

all: 	
	#########################################
	# GENERATING MGEN-VISUALDESIGNER MODEL	#
	#########################################
	rm src_generated -rf
	java -jar $(COMPILER_JAR) -project="model/project.xml" -plugin_paths="../mgen-javagenerator/target/"
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

