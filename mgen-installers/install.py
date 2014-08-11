#!/usr/bin/python
import sys
import argparse
import subprocess
import shutil
import os.path
import os
import urllib
import zipfile
import fnmatch
from subprocess import check_call


def clearDir(path): 
	if os.path.exists(path): 
		shutil.rmtree(path)	
	os.makedirs(path)

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
	urllib.urlretrieve("http://snapshot.culvertsoft.se/mgen-SNAPSHOT/mgen-SNAPSHOT.zip", filename=zipFile)
else:
	print("Skipping download")


#unzipping
print("Unzipping")
clearDir("temp")
fh = open(zipFile, 'rb')
z = zipfile.ZipFile(fh)
for name in z.namelist():
    outpath = "C:\\"
    z.extract(name, "temp/")
fh.close()


#deploying
print("Installing")
jarFiles = []
for root, dirnames, filenames in os.walk('temp'):
  for filename in fnmatch.filter(filenames, '*.jar'):
      jarFiles.append(os.path.join(root, filename))


clearDir(installPath)
os.makedirs(installPath + "/jars")
os.makedirs(installPath + "/bin")
for jarFile in jarFiles:
	trgFilePath = installPath + "/jars/" + os.path.basename(jarFile)
	shutil.copyfile(jarFile, trgFilePath)


