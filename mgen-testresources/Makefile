
MGEN_ROOT_PATH = ..
TEST_RESOURCES_ROOT = ${MGEN_ROOT_PATH}/mgen-testresources
MGEN_COMPILER_JAR = ${MGEN_ROOT_PATH}/mgen-compiler/target/mgen-compiler-assembly-$(MGEN_BUILD_VERSION).jar
JAVA_GENERATOR_PATH = ${MGEN_ROOT_PATH}/mgen-javagenerator/target
CPP_GENERATOR_PATH = ${MGEN_ROOT_PATH}/mgen-cppgenerator/target
JAVASCRIPT_GENERATOR_PATH = ${MGEN_ROOT_PATH}/mgen-javascriptgenerator/target
PLUGIN_PATHS ="${JAVA_GENERATOR_PATH}, ${CPP_GENERATOR_PATH}, ${JAVASCRIPT_GENERATOR_PATH}"

DEPENDS_MODEL = ${TEST_RESOURCES_ROOT}/models/depend/project.xml
DEPENDS_OUT = ${TEST_RESOURCES_ROOT}/generated/depends
DEPENDS_DATA_GENERATOR_SRC_DIR = ${TEST_RESOURCES_ROOT}/build/depends
DEPENDS_DATA_GENERATOR_BUILD_DIR = ${TEST_RESOURCES_ROOT}/generated/depends/data_generator

WRITE_MODEL = ${TEST_RESOURCES_ROOT}/models/writeread/write/project.xml
WRITE_OUT = ${TEST_RESOURCES_ROOT}/generated/writeread/write
WRITE_DATA_GENERATOR_SRC_DIR = ${TEST_RESOURCES_ROOT}/build/writeread/write
WRITE_DATA_GENERATOR_BUILD_DIR = ${TEST_RESOURCES_ROOT}/generated/writeread/write/data_generator

READ_MODEL = ${TEST_RESOURCES_ROOT}/models/writeread/read/project.xml
READ_OUT = ${TEST_RESOURCES_ROOT}/generated/writeread/read
READ_DATA_GENERATOR_SRC_DIR = ${TEST_RESOURCES_ROOT}/build/writeread/read
READ_DATA_GENERATOR_BUILD_DIR = ${TEST_RESOURCES_ROOT}/generated/writeread/read/data_generator

all: generate

generate-sources:
	mkdir -p generated
	java -jar ${MGEN_COMPILER_JAR} -plugin_paths=${PLUGIN_PATHS} -project=${DEPENDS_MODEL} -output_path=${DEPENDS_OUT} -fail_on_missing_generator=true
	java -jar ${MGEN_COMPILER_JAR} -plugin_paths=${PLUGIN_PATHS} -project=${WRITE_MODEL} -output_path=${WRITE_OUT} -fail_on_missing_generator=true
	java -jar ${MGEN_COMPILER_JAR} -plugin_paths=${PLUGIN_PATHS} -project=${READ_MODEL} -output_path=${READ_OUT} -fail_on_missing_generator=true
	mkdir -p ${DEPENDS_DATA_GENERATOR_BUILD_DIR}
	mkdir -p ${WRITE_DATA_GENERATOR_BUILD_DIR}
	mkdir -p ${READ_DATA_GENERATOR_BUILD_DIR}
	cd ${DEPENDS_DATA_GENERATOR_BUILD_DIR} && cmake -G"Unix Makefiles" -D CMAKE_BUILD_TYPE=Debug ../../../${DEPENDS_DATA_GENERATOR_SRC_DIR}
	cd ${WRITE_DATA_GENERATOR_BUILD_DIR} && cmake -G"Unix Makefiles" -D CMAKE_BUILD_TYPE=Debug ../../../../${WRITE_DATA_GENERATOR_SRC_DIR}
	cd ${READ_DATA_GENERATOR_BUILD_DIR} && cmake -G"Unix Makefiles" -D CMAKE_BUILD_TYPE=Debug ../../../../${READ_DATA_GENERATOR_SRC_DIR}

generate: generate-sources
	cd ${DEPENDS_DATA_GENERATOR_BUILD_DIR} && make && ./generate_depends_testdata
	cd ${WRITE_DATA_GENERATOR_BUILD_DIR} && make && ./generate_write_testdata
	cd ${READ_DATA_GENERATOR_BUILD_DIR} && make && ./generate_read_testdata

eclipse: generate-sources
	mkdir -p ${DEPENDS_DATA_GENERATOR_BUILD_DIR}/eclipse
	mkdir -p ${WRITE_DATA_GENERATOR_BUILD_DIR}/eclipse
	mkdir -p ${READ_DATA_GENERATOR_BUILD_DIR}/eclipse
	cd ${DEPENDS_DATA_GENERATOR_BUILD_DIR}/eclipse && cmake -G"Eclipse CDT4 - Unix Makefiles" -D CMAKE_BUILD_TYPE=Debug ../../../../${DEPENDS_DATA_GENERATOR_SRC_DIR}
	cd ${WRITE_DATA_GENERATOR_BUILD_DIR}/eclipse && cmake -G"Eclipse CDT4 - Unix Makefiles" -D CMAKE_BUILD_TYPE=Debug ../../../../../${WRITE_DATA_GENERATOR_SRC_DIR}
	cd ${READ_DATA_GENERATOR_BUILD_DIR}/eclipse && cmake -G"Eclipse CDT4 - Unix Makefiles" -D CMAKE_BUILD_TYPE=Debug ../../../../../${READ_DATA_GENERATOR_SRC_DIR}

clean:
	rm -rf generated

