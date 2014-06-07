all:
	#########################################################
	#	 					 	#
	#	 	    BUILDING MGEN		 	#
	#						 	#
	#########################################################
	cd mgen-api && make all
	cd mgen-compiler && make all
	cd mgen-javalib && make all
	cd mgen-javagenerator && make all
	cd mgen-visualdesigner && make all
	#########################################################
	#							#
	#	 	  DONE BUILDING MGEN		 	#
	#							#
	#########################################################

clean:	
	#########################################################
	#	 					 	#
	#	 	    CLEANING MGEN		 	#
	#	 					 	#
	#########################################################
	cd mgen-api && make clean
	cd mgen-compiler && make clean
	cd mgen-javalib && make clean
	cd mgen-javagenerator && make clean
	cd mgen-visualdesigner && make clean
	#########################################################
	#							#
	#	 	  DONE CLEANING MGEN		 	#
	#							#
	#########################################################

