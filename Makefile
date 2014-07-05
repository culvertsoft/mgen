VERSION = SNAPSHOT
export MGEN_BUILD_VERSION = $(VERSION)

all: check
	#########################################################
	#                                                       #
	#           BUILDING MGEN                               #
	#                                                       #
	#########################################################
	cd mgen-api && make all
	cd mgen-compiler && make all
	cd mgen-cppgenerator && make all
	cd mgen-javagenerator && make all
	cd mgen-javascriptgenerator && make all
	cd mgen-cpplib && make all
	cd mgen-javalib && make all
	cd mgen-visualdesigner && make all
	#########################################################
	#                                                       #
	#         DONE BUILDING MGEN                            #
	#                                                       #
	#########################################################

check: sbt-check

sbt-check:
	@hash sbt 2>/dev/null || { echo >&2 "I require sbt but it's not installed.  Aborting."; exit 1; }

clean:
	#########################################################
	#                                                       #
	#           CLEANING MGEN                               #
	#                                                       #
	#########################################################
	cd mgen-api && make clean
	cd mgen-compiler && make clean
	cd mgen-cppgenerator && make clean
	cd mgen-javagenerator && make clean
	cd mgen-javascriptgenerator && make clean
	cd mgen-cpplib && make clean
	cd mgen-javalib && make clean
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
	cd mgen-cpplib && make eclipse
	#########################################################
	#                                                       #
	#        DONE CREATING ECLIPSE PROJECTS                 #
	#                                                       #
	#########################################################

run-compiler:
	cd mgen-compiler && make run

run-visualdesigner:
	cd mgen-visualdesigner && make run

run-tests:
	#########################################################
	#                                                       #
	#       RUNNING ALL TESTS                               #
	#                                                       #
	#########################################################
	cd mgen-api && make run-tests
	cd mgen-compiler && make run-tests
	cd mgen-cppgenerator && make run-tests
	cd mgen-javagenerator && make run-tests
	#cd mgen-javascriptgenerator && make run-tests
	cd mgen-cpplib && make run-tests
	cd mgen-javalib && make run-tests
	cd mgen-visualdesigner && make run-tests
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
	cd mgen-cppgenerator && make doc
	cd mgen-javagenerator && make doc
	#cd mgen-javascriptgenerator && make doc
	cd mgen-cpplib && make doc
	cd mgen-javalib && make doc
	cd mgen-visualdesigner && make doc
	#########################################################
	#                                                       #
	#        DONE MAKING DOCUMENTATION                      #
	#                                                       #
	#########################################################
