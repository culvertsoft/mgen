#!/usr/bin/python
import argparse
import shutil
import os.path
import distutils.dir_util
import sys
import subprocess
import fnmatch
import platform
from subprocess import check_call

cpp_build_cfgs = ["Debug", "RelwithDebInfo", "Release"]

def mkFolder(path):
    if not os.path.exists(path):
        os.makedirs(path)
        
def rmFolder(path):
    if os.path.exists(path):
        shutil.rmtree(path)
        
def rmFile(path):
    if os.path.exists(path):
        os.remove(path)
        
def copyTree(src, dst):
    distutils.dir_util.copy_tree(src, dst)

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
