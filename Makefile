VERSION = SNAPSHOT
export MGEN_BUILD_VERSION = $(VERSION)

all: check
	#########################################################
	#                                                       #
	#           BUILDING MGEN                               #
	#                                                       #
	#########################################################
	cd mgen-api && make all
	cd mgen-jsonschemaparser && make all
	cd mgen-protobufparser && make all
	cd mgen-xmlschemaparser && make all
	cd mgen-idlparser && make all
	cd mgen-idlgenerator && make all
	cd mgen-compiler && make all
	cd mgen-cppgenerator && make all
	cd mgen-javagenerator && make all
	cd mgen-javascriptgenerator && make all
	cd mgen-javascriptlib && make all
	cd mgen-javalib && make all
	cd mgen-visualdesigner && make all
	#########################################################
	#                                                       #
	#         DONE BUILDING MGEN                            #
	#                                                       #
	#########################################################

publish: check
	cd mgen-api && make publish
	cd mgen-jsonschemaparser && make publish
	cd mgen-protobufparser && make publish
	cd mgen-xmlschemaparser && make publish
	cd mgen-idlparser && make publish
	cd mgen-idlgenerator && make publish
	cd mgen-compiler && make publish
	cd mgen-cppgenerator && make publish
	cd mgen-javagenerator && make publish
	cd mgen-javascriptgenerator && make publish
	cd mgen-javalib && make publish
	cd mgen-visualdesigner && make publish

all-fast: check
	#########################################################
	#                                                       #
	#           BUILDING MGEN (FAST)                        #
	#                                                       #
	#########################################################
	
	sbt compile package publish-local

	cd mgen-compiler && make
	cd mgen-javascriptlib && make all
	cd mgen-javalib && make all
	cd mgen-visualdesigner && make all

check: sbt-check

sbt-check:
	@hash sbt 2>/dev/null || { echo >&2 "I require sbt but it's not installed.  Aborting."; exit 1; }

clean:
	#########################################################
	#                                                       #
	#           CLEANING MGEN                               #
	#                                                       #
	#########################################################
	cd mgen-integrationtests && make clean
	cd mgen-api && make clean
	cd mgen-jsonschemaparser && make clean
	cd mgen-protobufparser && make clean
	cd mgen-xmlschemaparser && make clean
	cd mgen-idlparser && make clean
	cd mgen-idlgenerator && make clean
	cd mgen-compiler && make clean
	cd mgen-cppgenerator && make clean
	cd mgen-javagenerator && make clean
	cd mgen-javascriptgenerator && make clean
	cd mgen-javascriptlib && make clean
	cd mgen-cpplib && make clean
	cd mgen-javalib && make clean
	cd mgen-visualdesigner && make clean
	#########################################################
	#                                                       #
	#         DONE CLEANING MGEN                            #
	#                                                       #
	#########################################################

clean-fast:
	#########################################################
	#                                                       #
	#           CLEANING MGEN                               #
	#                                                       #
	#########################################################
	sbt clean
	cd mgen-integrationtests && make clean
	cd mgen-javascriptlib && make clean
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
	cd mgen-integrationtests && make eclipse
	#########################################################
	#                                                       #
	#        DONE CREATING ECLIPSE PROJECTS                 #
	#                                                       #
	#########################################################

run-compiler:
	cd mgen-compiler && make run

run-visualdesigner:
	cd mgen-visualdesigner && make run

run-integrationtests:
	cd mgen-integrationtests && make run-tests

run-tests: run-integrationtests
	#########################################################
	#                                                       #
	#       RUNNING ALL TESTS                               #
	#                                                       #
	#########################################################
	cd mgen-javascriptlib && make run-tests
	cd mgen-cpplib && make run-tests
	cd mgen-javalib && make run-tests
	#########################################################
	#                                                       #
	#        DONE RUNNING ALL TEST                          #
	#                                                       #
	#########################################################

run-tests-fast:
	sbt test
