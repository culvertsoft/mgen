#!/usr/bin/python
import sys
import argparse
import subprocess
import shutil
import os.path
from subprocess import check_call
from subprocess import call

# Parse arguments
parser = argparse.ArgumentParser()
parser.add_argument("-b", "--build", action='store_true')
parser.add_argument("-p", "--publish", action='store_true')
parser.add_argument("-c", "--clean", action='store_true')
parser.add_argument("-e", "--eclipse", action='store_true')
parser.add_argument("-t", "--test", action='store_true')
parser.add_argument("-a", "--all", action='store_true')
parser.add_argument("-v", "--version", default="SNAPSHOT", action='store_true')
args = parser.parse_args()


# Some helper variables
os.environ['MGEN_BUILD_VERSION'] = args.version
compiler = "mgen-compiler/target/mgen-compiler-assembly-" + args.version + ".jar"
compile_0 = "java -jar " + compiler + " "
compile_1 = "java -jar ../" + compiler + " "
compile_2 = "java -jar ../../" + compiler + " "
compile_3 = "java -jar ../../../" + compiler + " "


# Some helper fcns
def fastbuild_step1():
	check_call(('sbt '
				'"project mgen_api" publish-local '
				'"project mgen_idlparser" publish-local '
				'"project mgen_jsonschemaparser" publish-local '
				'"project mgen_protobufparser" publish-local '
				'"project mgen_xmlschemaparser" publish-local '
				'"project mgen_idlgenerator" publish-local '
				'"project mgen_javalib" publish-local '
				'"project mgen_compiler" assembly publish-local '
				'"project mgen_javagenerator" publish-local '
				'"project mgen_cppgenerator" publish-local '
				'"project mgen_javascriptgenerator" publish-local '), shell=True)

def fastbuild_generate_code():
	check_call(compile_1 + 'model/project.xml plugin_paths="../mgen-javagenerator/target"', cwd="mgen-visualdesigner", shell=True)

def fastbuild_step2():
	check_call(('sbt "project mgen_visualdesigner" assembly publish-local '), shell=True)
				
def tests_generate_code(): # Ideally here we'd just generate once, not nLangs times.
	check_call(compile_1 + '../mgen-compiler/src/test/resources/transient_testmodel/project.xml plugin_paths="../mgen-javagenerator/target"', cwd="mgen-javalib", shell=True)
	check_call(compile_1 + '../mgen-compiler/src/test/resources/defaultvalues_testmodel/project.xml plugin_paths="../mgen-javagenerator/target"', cwd="mgen-javalib", shell=True)
	check_call(compile_1 + '../mgen-compiler/src/test/resources/defaultvaluesreq_testmodel/project.xml plugin_paths="../mgen-javagenerator/target"', cwd="mgen-javalib", shell=True)		
	check_call(compile_1 + '../mgen-compiler/src/test/resources/project.xml plugin_paths="../mgen-cppgenerator/target"', cwd="mgen-cpplib", shell=True)
	check_call(compile_1 + '../mgen-compiler/src/test/resources/transient_testmodel/project.xml plugin_paths="../mgen-cppgenerator/target"', cwd="mgen-cpplib", shell=True)
	check_call(compile_1 + '../mgen-compiler/src/test/resources/defaultvalues_testmodel/project.xml plugin_paths="../mgen-cppgenerator/target"', cwd="mgen-cpplib", shell=True)
	check_call(compile_1 + '../mgen-compiler/src/test/resources/defaultvaluesreq_testmodel/project.xml plugin_paths="../mgen-cppgenerator/target"', cwd="mgen-cpplib", shell=True)		
	check_call(compile_1 + '../mgen-compiler/src/test/resources/project.xml plugin_paths="../mgen-javascriptgenerator/target"', cwd="mgen-javascriptlib", shell=True)
	check_call(compile_1 + '../mgen-compiler/src/test/resources/transient_testmodel/project.xml plugin_paths="../mgen-javascriptgenerator/target"', cwd="mgen-javascriptlib", shell=True)
	check_call(compile_1 + '../mgen-compiler/src/test/resources/defaultvalues_testmodel/project.xml plugin_paths="../mgen-javascriptgenerator/target"', cwd="mgen-javascriptlib", shell=True)
	check_call(compile_1 + '../mgen-compiler/src/test/resources/defaultvaluesreq_testmodel/project.xml plugin_paths="../mgen-javascriptgenerator/target"', cwd="mgen-javascriptlib", shell=True)		
	# need to add integration-tests generation code here

# clean
if args.clean or args.all:
	check_call("sbt clean", shell=True)
	
# build
if args.build or args.all or len(sys.argv) == 1:
	fastbuild_step1()
	fastbuild_generate_code()
	fastbuild_step2()

# test
if args.test or args.all:
	tests_generate_code()
	print("mgen-python-test: Not yet implemented!")
	#run integrationtests
	#run normal tests

# eclipse
if args.eclipse or args.all:
	check_call('sbt eclipse', shell=True)
	
# publish
if args.publish or args.all:
	print("mgen-python-publish: Not yet implemented!")
