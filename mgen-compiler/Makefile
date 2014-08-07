all: 	
	#########################################
	#   BUILDING MGEN-COMPILER              #
	#########################################
	sbt compile package assembly publish-local

eclipse:
	##############################################
	# CREATING ECLIPSE PROJECT FOR MGEN-COMPILER #
	##############################################
	sbt eclipse

clean:
	#########################################
	#        CLEANING MGEN-COMPILER         #
	#########################################
	sbt clean

run:	all
	java -jar target/mgen-compiler-assembly-$(MGEN_BUILD_VERSION).jar
