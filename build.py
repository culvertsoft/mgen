#!/usr/bin/python
import buildimpl
from buildimpl import *

# Parse arguments
parser = argparse.ArgumentParser()
parser.add_argument("-b", "--build", action='store_true')
parser.add_argument("-p", "--publish", action='store_true')
parser.add_argument("-c", "--clean", action='store_true')
parser.add_argument("-e", "--eclipse", action='store_true')
parser.add_argument("-t", "--test", action='store_true')
parser.add_argument("-d", "--debugscript", action='store_true')
parser.add_argument("-z", "--create_install_zip", action='store_true')
parser.add_argument("-u", "--upload_to_culvertsoft", action='store_true')
parser.add_argument("-i", "--install", action='store_true')
parser.add_argument("-v", "--version", default="SNAPSHOT")
args = parser.parse_args()

# Buildimpl requires these to be set
os.environ['MGEN_BUILD_VERSION'] = args.version
buildimpl.mgen_version = args.version
buildimpl.mgen_jar = "mgen-compiler/target/mgen-compiler-assembly-" + args.version + ".jar"
buildimpl.mgen_cmd = "java -jar ../" + buildimpl.mgen_jar + " "
buildimpl.pluginPaths = "../mgen-javagenerator/target,../mgen-cppgenerator/target,../mgen-javascriptgenerator/target"
buildimpl.default_cpp_build_cfg = "RelwithDebInfo" # Because VS is epicly slow in debug

# Targets 
if args.clean: clean() 
if args.build: build()
if args.test: test()
if args.create_install_zip: create_install_zip()
if args.eclipse: eclipse()
if args.upload_to_culvertsoft: upload_to_culvertsoft()
if args.install: install()
if args.publish: publish()
