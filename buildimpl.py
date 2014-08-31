#!/usr/bin/python
from buildutil import *

###########
# TARGETS #
###########

def clean():
    sbt_clean(".")
    rmFolder("mgen-integrationtests/generated/")
    rmFolders(".", "src_generated")
    rmFolder("mgen-cpplib/target")
    
def build():
    createVersionFiles()
    fastbuild_step1()
    fastbuild_generate_code()
    fastbuild_step2()

def test():
    tests_generate_code()
    tests_integration_cpp()
    tests_integration_java()
    tests_integration_js()
    tests_normal()

def eclipse():
    sbt_eclipse(".")

def publish():
    publish_impl()
    
###########
# HELPERS #
###########

mgen_version = "NEEDS_TO_BE_SET"
mgen_cmd = "NEEDS_TO_BE_SET"
pluginPaths = "NEEDS_TO_BE_SET"
default_cpp_build_cfg = "NEEDS_TO_BE_SET"

def compile3(workingDir, project, outPath):
    check_call(mgen_cmd + project + pluginPaths + ' output_path="' + outPath + '"', cwd=workingDir, shell=True)

def compile(workingDir, project):
    compile3(workingDir, project, ".")

def createJavaVersionFileContents(pkg, version):
    dateString = os.popen("git show -s --format=%ci").read().rstrip() # Get commit date and time
    out = ""
    out += "package " + pkg + ";\n\n"
    out += "/**\n"
    out += " * Class generated to keep track of what MGen version this is\n"
    out += " */\n"
    out += "public class BuildVersion {\n"
    out += '   public static final String GIT_TAG = "' + version + '";\n'
    out += '   public static final String GIT_COMMIT_DATE = "' + dateString + '";\n'
    out += "}\n"
    return out

def createJavaVersionFile3(pkg, tgtFolder, version):
    fName = tgtFolder + "/BuildVersion.java"    
    newFileContents = createJavaVersionFileContents(pkg, version)
    oldFileContents = file2String(fName)
    if (newFileContents != oldFileContents):
        mkFolder(tgtFolder)
        f = open(fName, "w")
        f.write(newFileContents)

def createJavaVersionFile2(project, version):
    pkg = "se.culvertsoft.mgen." + project
    tgtFolder = "mgen-" + project + "/src/main/java/se/culvertsoft/mgen/" + project
    createJavaVersionFile3(pkg, tgtFolder, version)

def createVersionFiles():
    createJavaVersionFile2("api", mgen_version)
    createJavaVersionFile2("compiler", mgen_version)
    createJavaVersionFile2("javalib", mgen_version)
    createJavaVersionFile2("cppgenerator", mgen_version)
    createJavaVersionFile2("cpplib", mgen_version)
    createJavaVersionFile2("idlgenerator", mgen_version)
    createJavaVersionFile2("idlparser", mgen_version)
    createJavaVersionFile2("javagenerator", mgen_version)
    createJavaVersionFile2("javalib", mgen_version)
    createJavaVersionFile2("javascriptgenerator", mgen_version)
    createJavaVersionFile2("jsonschemaparser", mgen_version)
    createJavaVersionFile2("protobufparser", mgen_version)
    createJavaVersionFile2("visualdesigner", mgen_version)
    createJavaVersionFile2("xmlschemaparser", mgen_version)

def fastbuild_step1():
    sbt(".",   ('"project mgen_api" publish-local '
                '"project mgen_idlparser" publish-local '
                '"project mgen_jsonschemaparser" publish-local '
                '"project mgen_protobufparser" publish-local '
                '"project mgen_xmlschemaparser" publish-local '
                '"project mgen_idlgenerator" publish-local '
                '"project mgen_javalib" publish-local '
                '"project mgen_compiler" assembly publish-local '
                '"project mgen_javagenerator" publish-local '
                '"project mgen_cppgenerator" publish-local '
                '"project mgen_javascriptgenerator" publish-local '))

def fastbuild_generate_code():
    check_call(mgen_cmd + 'model/project.xml plugin_paths="../mgen-javagenerator/target"', cwd="mgen-visualdesigner", shell=True)

def fastbuild_step2():
    sbt(".", '"project mgen_visualdesigner" assembly publish-local ')
                
def tests_generate_code(): # Ideally here we'd just generate once, not nLangs times.
    for lang in ["java", "cpp", "javascript"]:
        for model in ["project.xml", "transient_testmodel/project.xml", "defaultvalues_testmodel/project.xml", "defaultvaluesreq_testmodel/project.xml"]:
            compile("mgen-" + lang + "lib", "../mgen-compiler/src/test/resources/" + model)          
    for name in ["depends", "write", "read"]:
        compile3("mgen-integrationtests", 'models/'+name+'/project.xml', "generated/"+name)

def tests_integration_cpp():
    baseFolder = "mgen-integrationtests/generated"
    for name in ["depends", "write", "read"]:
        testFolder = baseFolder + "/" + name + "/data_generator"
        mkFolder(testFolder)
        mkFolder(baseFolder + "/" + name + "/data_generated")
        cmake(testFolder, "../../../build/" + name, default_cpp_build_cfg)
        cppBuildRun(testFolder, default_cpp_build_cfg, "generate_"+name+"_testdata")
    
def tests_integration_java():
    integFolder = "mgen-integrationtests"
    for name in ["depends", "write", "read"]:
        testFolder = integFolder + "/javacheck/" + name
        copyTree(integFolder + "/generated/" + name + "/src_generated/java", testFolder + "/src_generated/test/java")
        sbt_test(testFolder)

def tests_integration_js():
    integFolder = "mgen-integrationtests"
    for name in ["depends", "write", "read"]:
        testFolder = integFolder + "/javascriptcheck/" + name
        copyTree(integFolder + "/generated/" + name + "/src_generated/javascript", testFolder + "/src_generated/test/javascript")
        sbt_jasmine(testFolder)
    
def tests_normal():
    # Can this be done in one batch? Doesn't seem so, 
    # seems like cwd is wierd when batching test data isnt 
    # copied over to the test dir
    sbt_test("mgen-javalib")
    sbt_test("mgen-javascriptlib")
    mkFolder("mgen-cpplib/target")
    cmake("mgen-cpplib/target", "../src/test/cpp/src", default_cpp_build_cfg)
    cppBuildRun("mgen-cpplib/target", default_cpp_build_cfg, "mgen-cpplib-test")

def publish_impl():
    sbt(".",   ('"project mgen_api" publish-signed '
                '"project mgen_idlparser" publish-signed '
                '"project mgen_jsonschemaparser" publish-signed '
                '"project mgen_protobufparser" publish-signed '
                '"project mgen_xmlschemaparser" publish-signed '
                '"project mgen_idlgenerator" publish-signed '
                '"project mgen_javalib" publish-signed '
                '"project mgen_compiler" publish-signed '
                '"project mgen_javagenerator" publish-signed '
                '"project mgen_cppgenerator" publish-signed '
                '"project mgen_javascriptgenerator" publish-signed '
                '"project mgen_visualdesigner" publish-signed '))
    
