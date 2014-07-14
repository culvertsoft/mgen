
MGEN_COMPILER_JAR = ../mgen-compiler/target/mgen-compiler-assembly-$(MGEN_BUILD_VERSION).jar

all: generate

generate:
	#####################################################
	# GENERATING MODELS AND DATA FOR MGEN-TESTRESOURCES #
	#####################################################
	cd write && make generate
	cd read && make generate

clean:
	#########################################
	#   CLEANING MGEN-CPPLIB-TEST           #
	#########################################
	#cd build && make clean
	#cd generated && make clean

