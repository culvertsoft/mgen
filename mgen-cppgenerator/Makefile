all: 	
	#########################################
	#   BUILDING MGEN-CPPGENERATOR          #
	#########################################
	sbt compile package publish-local

eclipse:
	##################################################
	# CREATING ECLIPSE PROJECT FOR MGEN-CPPGENERATOR #
	##################################################
	sbt eclipse

clean: 	
	#########################################
	#   CLEANING MGEN-CPPGENERATOR          #
	#########################################
	sbt clean

publish:
	sbt publish-signed