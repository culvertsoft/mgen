all: 	
	#########################################
	#       BUILDING MGEN-API               #
	#########################################
	sbt compile package publish-local

eclipse:
	#########################################
	# CREATING ECLIPSE PROJECT FOR MGEN-API #
	#########################################
	sbt eclipse

clean: 	
	#########################################
	#       CLEANING MGEN-API               #
	#########################################
	sbt clean

publish:
	sbt publish-signed