VERSION = SNAPSHOT
export MGEN_BUILD_VERSION = $(VERSION)

all:
	#########################################################
	#                                                       #
	#           BUILDING MGEN                               #
	#                                                       #
	#########################################################
	cd mgen-api && make all
	cd mgen-compiler && make all
	#cd mgen-cppgenerator && make all
	#cd mgen-cpplib && make all
	cd mgen-javalib && make all
	cd mgen-javagenerator && make all
	#cd mgen-javascriptgenerator && make all
	cd mgen-visualdesigner && make all
	#########################################################
	#                                                       #
	#         DONE BUILDING MGEN                            #
	#                                                       #
	#########################################################

clean:
	#########################################################
	#                                                       #
	#           CLEANING MGEN                               #
	#                                                       #
	#########################################################
	cd mgen-api && make clean
	cd mgen-compiler && make clean
	#cd mgen-cppgenerator && make clean
	#cd mgen-cpplib && make clean
	cd mgen-javalib && make clean
	cd mgen-javagenerator && make clean
	#cd mgen-javascriptgenerator && make clean
	cd mgen-visualdesigner && make clean
	#########################################################
	#                                                       #
	#         DONE CLEANING MGEN                            #
	#                                                       #
	#########################################################

eclipse:
	#########################################################
	#                                                       #
	#       CREATING ECLIPSE PROJECTS                       #
	#                                                       #
	#########################################################
	sbt eclipse
	#cd mgen-cpplib && make eclipse
	#########################################################
	#                                                       #
	#        DONE CREATING ECLIPSE PROJECTS                 #
	#                                                       #
	#########################################################

run-compiler:
	cd mgen-compiler && make run

run-visualdesigner:
	cd mgen-visualdesigner && make run

test:
	#########################################################
	#                                                       #
	#       RUNNING ALL TESTS                               #
	#                                                       #
	#########################################################
	cd mgen-api && make test
	cd mgen-compiler && make test
	#cd mgen-cppgenerator && make test
	#cd mgen-cpplib && make test
	cd mgen-javalib && make test
	cd mgen-javagenerator && make test
	#cd mgen-javascriptgenerator && make test
	cd mgen-visualdesigner && make test
	#########################################################
	#                                                       #
	#        DONE RUNNING ALL TEST                          #
	#                                                       #
	#########################################################


doc:
	#########################################################
	#                                                       #
	#       MAKING DOCUMENTATION                            #
	#                                                       #
	#########################################################
	cd mgen-api && make doc
	cd mgen-compiler && make doc
	#cd mgen-cppgenerator && make doc
	#cd mgen-cpplib && make doc
	cd mgen-javalib && make doc
	cd mgen-javagenerator && make doc
	#cd mgen-javascriptgenerator && make doc
	cd mgen-visualdesigner && make doc
	#########################################################
	#                                                       #
	#        DONE MAKING DOCUMENTATION                      #
	#                                                       #
	#########################################################
