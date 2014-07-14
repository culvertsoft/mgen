
MGEN_COMPILER_JAR = ../mgen-compiler/target/mgen-compiler-assembly-$(MGEN_BUILD_VERSION).jar

all: generate

generate:
	cd depends && make generate
	cd writeread && make generate

clean:
	cd depends && make clean
	cd writeread && make clean

