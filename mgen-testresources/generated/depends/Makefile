
MGEN_ROOT_PATH = ../../..
TEST_RESOURCES_ROOT = ${MGEN_ROOT_PATH}/mgen-testresources
MODEL_PATH = ${TEST_RESOURCES_ROOT}/models/depend/project.xml
MGEN_COMPILER_JAR = ${MGEN_ROOT_PATH}/mgen-compiler/target/mgen-compiler-assembly-$(MGEN_BUILD_VERSION).jar
JAVA_GENERATOR_PATH = ${MGEN_ROOT_PATH}/mgen-javagenerator/target
CPP_GENERATOR_PATH = ${MGEN_ROOT_PATH}/mgen-cppgenerator/target
JAVASCRIPT_GENERATOR_PATH = ${MGEN_ROOT_PATH}/mgen-javascriptgenerator/target
PLUGIN_PATHS ="${JAVA_GENERATOR_PATH}, ${CPP_GENERATOR_PATH}, ${JAVASCRIPT_GENERATOR_PATH}"

all: generate

generate:
	mkdir -p src_generated
	java -jar ${MGEN_COMPILER_JAR} -project=${MODEL_PATH} -plugin_paths=${PLUGIN_PATHS} -fail_on_missing_generator=true

clean:
	rm -rf src_generated
	rm -rf data_generated

