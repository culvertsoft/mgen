#!/usr/bin/python
import sys
import argparse
import subprocess
import shutil
import os.path
import os
import zipfile
import fnmatch
import stat
import distutils
import distutils.dir_util
from subprocess import check_call

# util fcn
def clearDir(path): 
	if os.path.exists(path): 
		shutil.rmtree(path)	
	os.makedirs(path)
	
# Make it work with both python 2 and 3
if sys.version_info >= (3,0):
	import urllib.request
	from urllib.request import urlretrieve
else:
	import urllib
	from urllib import urlretrieve

#Check that we have an install path
installPath = os.environ.get('MGEN_INSTALL_PATH')
if installPath == None:
	raise Exception("Environmental variable MGEN_INSTALL_PATH not set")


# Parse arguments
print("Parsing arguments")
parser = argparse.ArgumentParser()
parser.add_argument("-n", "--nodownload", action='store_true')
args = parser.parse_args()


# name of the file to store on disk
zipFile = "mgen.zip"


# downloading
if not args.nodownload:
	print("Downloading mgen:")
	if os.path.exists(zipFile): 
		os.remove(zipFile)
	print("  http://snapshot.culvertsoft.se/mgen-SNAPSHOT/mgen-SNAPSHOT.zip... (this may take a while, and gives no status indication)")
	urlretrieve("http://snapshot.culvertsoft.se/mgen-SNAPSHOT/mgen-SNAPSHOT.zip", filename=zipFile)
else:
	print("Skipping download")


#unzipping
print("Unzipping")
clearDir("temp")
fh = open(zipFile, 'rb')
z = zipfile.ZipFile(fh)
for name in z.namelist():
    z.extract(name, "temp/")
fh.close()


#deploying
print("Deploying")
jarFiles = []
for root, dirnames, filenames in os.walk('temp'):
  for filename in fnmatch.filter(filenames, '*.jar'):
      jarFiles.append(os.path.join(root, filename))

# Clear the install dir
clearDir(installPath)
os.makedirs(installPath + "/jars")
os.makedirs(installPath + "/bin")
os.makedirs(installPath + "/include")

# Copy executables
lnxTrgFile = installPath + "/bin/mgen"
shutil.copyfile("mgen.sh", lnxTrgFile)
shutil.copyfile("mgen.ex_", installPath + "/bin/mgen.exe")
distutils.dir_util.copy_tree("temp/mgen-SNAPSHOT/mgen-cpplib/include", installPath + "/include")

# Mark the linux script as executable
st = os.stat(lnxTrgFile)
os.chmod(lnxTrgFile, st.st_mode | stat.S_IEXEC)

# Copy all jar files
for jarFile in jarFiles:
	trgFilePath = installPath + "/jars/" + os.path.basename(jarFile)
	shutil.copyfile(jarFile, trgFilePath)

