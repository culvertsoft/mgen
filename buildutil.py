#!/usr/bin/python
from __future__ import with_statement
import argparse
import shutil
import os.path
import distutils.dir_util
import sys
import subprocess
import fnmatch
import platform
import zipfile
import os
import stat
from subprocess import check_call
from contextlib import closing
from zipfile import ZipFile, ZIP_DEFLATED

###################################
# File/folder utils

def mkFolder(path):
    if not os.path.exists(path):
        os.makedirs(path)

def file2String(path):
    if os.path.exists(path):
        return open(path, 'r').read()
    else:
        return ""

def rmFolder(path):
    if os.path.exists(path):
        shutil.rmtree(path)

def mkFileExecutable(file_path):
    st = os.stat(file_path)
    os.chmod(file_path, st.st_mode | stat.S_IEXEC | stat.S_IXGRP | stat.S_IXOTH)

def rmFolderContents(folder):
    if os.path.exists(folder):
        for the_file in os.listdir(folder):
            file_path = os.path.join(folder, the_file)
            if os.path.exists(file_path):
                if os.path.isfile(file_path):
                    rmFile(file_path)
                else:
                    rmFolder(file_path)

def rmFile(path):
    if os.path.exists(path):
        os.remove(path)
        
def copyTree(src, dst):
    distutils.dir_util.copy_tree(src, dst)

def copyFile(src, dst):
    shutil.copyfile(src, dst)

def getOrSetEnvVar(varName, default):
    if (not varName in os.environ):
        os.environ[varName] = default
    return os.environ[varName]

def findDirs(path, matching):
    out = []
    for root, dirnames, filenames in os.walk(path):
        for dirName in fnmatch.filter(dirnames, matching):
            out.append(os.path.join(root, dirName))
    return out
      
def findFiles(path, matching):
    out = []
    for root, dirnames, filenames in os.walk(path):
        for fileName in fnmatch.filter(filenames, matching):
            out.append(os.path.join(root, fileName))
    return out
     
def findFilesExt(path, names, exclDirs):
    out = []
    for root, dirs, files in os.walk(path, topdown=True):
        dirs[:] = [d for d in dirs if d not in exclDirs]
        for file in files:
            for name in names:
                if file.endswith(name):
                    out.append(os.path.join(root, file))
    return out

def rmFolders(path, matching):
    for folder in findDirs(path, matching):
        rmFolder(folder)

def rmFiles(path, matching):
    for folder in findFiles(path, matching):
        rmFile(folder)
        
def findLocalFiles(path, matching):
    out = []
    for name in fnmatch.filter(os.listdir(path), matching):
        out.append(name)
    return out

###################################
# C++ utils

cpp_build_cfgs = ["Debug", "RelwithDebInfo", "Release"]

def cppBuild(path, cfg, projName):
    if platform.system()=="Windows":
        check_call("msbuild " + projName + ".sln /p:Configuration=" + cfg, cwd=path, shell=True)
    else:
        check_call("make", cwd=path, shell=True)
        
def cppRun(path, cfg, projName):
    if platform.system()=="Windows":
        check_call(cfg + "\\" + projName + ".exe", cwd=path, shell=True)
    else:
        check_call("./" + projName, cwd=path, shell=True)

def cppBuildRun(path, cfg, projname):
    cppBuild(path, cfg, projname)
    cppRun(path, cfg, projname) 

def cmake(buildPath, srcPath, cfg):
    check_call("cmake -DCMAKE_BUILD_TYPE=" + cfg + " " + srcPath, cwd=buildPath, shell=True)
    
###################################
# Sbt utils

def sbt(path, targets):
    check_call("sbt " + targets, cwd=path, shell=True)  
    
def sbt_test(path):
    sbt(path, "test")

def sbt_eclipse(path):
    sbt(path, "eclipse")

def sbt_clean(path):
    sbt(path, "clean")

def sbt_jasmine(path):
    sbt(path, "jasmine")

###################################
# Zip utils

def zipdir(basedir, archivename):
    assert os.path.isdir(basedir)
    with closing(ZipFile(archivename, "w", ZIP_DEFLATED)) as z:
        for root, dirs, files in os.walk(basedir):
            #NOTE: ignore empty directories
            for fn in files:
                absfn = os.path.join(root, fn)
                zfn = absfn[len(basedir)+len(os.sep):] #XXX: relative path
                z.write(absfn, zfn)

def unzip(file, outPath):
    fh = open(file, 'rb')
    z = zipfile.ZipFile(fh)
    for name in z.namelist():
        z.extract(name, outPath + "/")
    fh.close()

###################################
# Doit utils

class RunOnceLazy:
    def getVal(self):
        if (not self.done):
            self.result = self.action()
            self.done = True
        return self.result
    def __init__(self, action):
        self.action = action
        self.done = False
    def __call__(self):
        return self.getVal()
    def reset(self):
        self.done = False
        
class GetFileDep(RunOnceLazy):
    def __call__(self):
        return { 'file_dep': self.getVal() }

def mkCalcDepFileTask(patterns, root = '.', exclDirs = [], taskDeps = [], doc = ''):
    return { 
        'actions': [GetFileDep(lambda: findFilesExt(root, patterns, exclDirs))],
        'task_dep': taskDeps,
        'doc': doc,
        'verbosity': 2
    }
