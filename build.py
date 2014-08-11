#!/usr/bin/python
import sys
import argparse
import subprocess
import shutil
import os.path
import platform
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
compile_1 = "java -jar ../" + compiler + " "
javagenpath = "../mgen-javagenerator/target"
cppgenpath = "../mgen-cppgenerator/target"
jsgenpath = "../mgen-javascriptgenerator/target"
pluginPaths = ' plugin_paths="' + javagenpath + "," + cppgenpath + "," + jsgenpath + '"'

# Some helper fcns
def compile3(workingDir, project, outPath):
	check_call(compile_1 + project + pluginPaths + ' output_path="' + outPath + '"', cwd=workingDir, shell=True)

def compile(workingDir, project):
	compile3(workingDir, project, ".")

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
	compile("mgen-javalib", '../mgen-compiler/src/test/resources/project.xml')
	compile("mgen-javalib", '../mgen-compiler/src/test/resources/transient_testmodel/project.xml')
	compile("mgen-javalib", '../mgen-compiler/src/test/resources/defaultvalues_testmodel/project.xml')
	compile("mgen-javalib", '../mgen-compiler/src/test/resources/defaultvaluesreq_testmodel/project.xml')
	compile("mgen-cpplib", '../mgen-compiler/src/test/resources/project.xml')
	compile("mgen-cpplib", '../mgen-compiler/src/test/resources/transient_testmodel/project.xml')
	compile("mgen-cpplib", '../mgen-compiler/src/test/resources/defaultvalues_testmodel/project.xml')
	compile("mgen-cpplib", '../mgen-compiler/src/test/resources/defaultvaluesreq_testmodel/project.xml')
	compile("mgen-javascriptlib", '../mgen-compiler/src/test/resources/project.xml')
	compile("mgen-javascriptlib", '../mgen-compiler/src/test/resources/transient_testmodel/project.xml')
	compile("mgen-javascriptlib", '../mgen-compiler/src/test/resources/defaultvalues_testmodel/project.xml')
	compile("mgen-javascriptlib", '../mgen-compiler/src/test/resources/defaultvaluesreq_testmodel/project.xml')
	compile3("mgen-integrationtests", 'models/depend/project.xml', "generated/depends")
	compile3("mgen-integrationtests", 'models/write/project.xml', "generated/write")
	compile3("mgen-integrationtests", 'models/read/project.xml', "generated/read")
	
def mkFolder(path):
	if not os.path.exists(path):
		os.makedirs(path)
		
def mkTestData(path, cfg, projname):
	if platform.system()=="Windows":
		check_call("msbuild " + projname + ".sln /p:Configuration=" + cfg, cwd=path, shell=True)
		check_call(cfg + "\\" + projname + ".exe", cwd=path, shell=True)
	else:
		check_call("make", cwd=path, shell=True)
		check_call("./" + projname, cwd=path, shell=True)
		
	
def tests_integration_generate_data():
	cfg = "RelwithDebInfo"
	dGenFolder = "mgen-integrationtests/generated/depends/data_generator"
	wGenFolder = "mgen-integrationtests/generated/write/data_generator"
	rGenFolder = "mgen-integrationtests/generated/read/data_generator"
	mkFolder(dGenFolder)
	mkFolder(wGenFolder)
	mkFolder(rGenFolder)
	check_call('cmake -DCMAKE_BUILD_TYPE=' + cfg + ' ../../../build/depends', cwd=dGenFolder, shell=True)
	check_call('cmake -DCMAKE_BUILD_TYPE=' + cfg + ' ../../../build/write', cwd=wGenFolder, shell=True)
	check_call('cmake -DCMAKE_BUILD_TYPE=' + cfg + ' ../../../build/read', cwd=rGenFolder, shell=True)
	mkTestData(dGenFolder, cfg, "generate_depends_testdata")
	mkTestData(wGenFolder, cfg, "generate_write_testdata")
	mkTestData(rGenFolder, cfg, "generate_read_testdata")
	
# clean
if args.clean or args.all:
	check_call("sbt clean", shell=True)
	#TODO: Remove all src_generated folders for tests and visualdesigner
	#TODO: Remove cpp test applications build folders
	
# build
if args.build or args.all or len(sys.argv) == 1:
	fastbuild_step1()
	fastbuild_generate_code()
	fastbuild_step2()

# test
if args.test or args.all:
	tests_generate_code()
	tests_integration_generate_data()
	#tests_integration_run()
	#tests_normal_run()
	#run integrationtests
	#run normal tests
	print("mgen-python-test: Not yet implemented!")

# eclipse
if args.eclipse or args.all:
	check_call('sbt eclipse', shell=True)
	
# publish
if args.publish or args.all:
	print("mgen-python-publish: Not yet implemented!")
