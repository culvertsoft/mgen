all: 	
	#########################################
	#	 BUILDING MGEN-COMPILER		#
	#########################################
	sbt compile package assembly publish-local

clean:
	#########################################
	#	 CLEANING MGEN-COMPILER		#
	#########################################
	sbt clean


run:	all
	java -jar target/mgen-compiler-assembly-*

