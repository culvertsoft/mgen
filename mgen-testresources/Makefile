
MGEN_ROOT_PATH = ..
TEST_RESOURCES_ROOT = ${MGEN_ROOT_PATH}/mgen-testresources
MGEN_COMPILER_JAR = ${MGEN_ROOT_PATH}/mgen-compiler/target/mgen-compiler-assembly-$(MGEN_BUILD_VERSION).jar
JAVA_GENERATOR_PATH = ${MGEN_ROOT_PATH}/mgen-javagenerator/target
CPP_GENERATOR_PATH = ${MGEN_ROOT_PATH}/mgen-cppgenerator/target
JAVASCRIPT_GENERATOR_PATH = ${MGEN_ROOT_PATH}/mgen-javascriptgenerator/target
PLUGIN_PATHS ="${JAVA_GENERATOR_PATH}, ${CPP_GENERATOR_PATH}, ${JAVASCRIPT_GENERATOR_PATH}"

DEPENDS_MODEL = ${TEST_RESOURCES_ROOT}/models/depend/project.xml
DEPENDS_OUT = ${TEST_RESOURCES_ROOT}/generated/depends

WRITE_MODEL = ${TEST_RESOURCES_ROOT}/models/writeread/write/project.xml
WRITE_OUT = ${TEST_RESOURCES_ROOT}/generated/writeread/write

READ_MODEL = ${TEST_RESOURCES_ROOT}/models/writeread/read/project.xml
READ_OUT = ${TEST_RESOURCES_ROOT}/generated/writeread/read

all: generate

generate:
	mkdir -p generated
	java -jar ${MGEN_COMPILER_JAR} -plugin_paths=${PLUGIN_PATHS} -project=${DEPENDS_MODEL} -output_path=${DEPENDS_OUT} -fail_on_missing_generator=true
	java -jar ${MGEN_COMPILER_JAR} -plugin_paths=${PLUGIN_PATHS} -project=${WRITE_MODEL} -output_path=${WRITE_OUT} -fail_on_missing_generator=true
	java -jar ${MGEN_COMPILER_JAR} -plugin_paths=${PLUGIN_PATHS} -project=${READ_MODEL} -output_path=${READ_OUT} -fail_on_missing_generator=true

clean:
	cd build && make clean
	rm -rf generated

