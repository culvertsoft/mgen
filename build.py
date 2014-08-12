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
parser.add_argument("-a", "--all", help='all except publish (=cbt)', action='store_true')
parser.add_argument("-d", "--debugscript", action='store_true')
parser.add_argument("-v", "--version", default="SNAPSHOT", action='store_true')
args = parser.parse_args()

# Buildimpl requires these to be set
os.environ['MGEN_BUILD_VERSION'] = args.version
buildimpl.mgen_jar = "mgen-compiler/target/mgen-compiler-assembly-" + args.version + ".jar"
buildimpl.mgen_cmd = "java -jar ../" + buildimpl.mgen_jar + " "
buildimpl.pluginPaths = ' plugin_paths="' + "../mgen-javagenerator/target" + "," + "../mgen-cppgenerator/target" + "," + "../mgen-javascriptgenerator/target" + '"'
buildimpl.default_cpp_build_cfg = "RelwithDebInfo" # Because VS is epicly slow in debug

# Targets 
if args.clean or args.all: clean() 
if args.build or args.all or len(sys.argv) == 1: build()
if args.test or args.all: test()
if args.eclipse or args.all: eclipse()
if args.publish: publish()
