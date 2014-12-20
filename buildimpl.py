#!/usr/bin/python
from buildutil import *

###########
# TARGETS #
###########

def clean():
    sbt_clean(".")
    rmFolders(".", "src_generated")
    rmFolder("mgen-integrationtests/generated/")
    rmFolder("mgen-cpplib/target")
    rmFolder("target/install_zip")
    

def build():
    createVersionFiles()
    build_jvm_parts()


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


def compile4(workingDir, project, outPath, plug_paths):
    check_call(mgen_cmd + project + ' plugin_paths="' + plug_paths + '" output_path="' + outPath + '" use_env_vars="false"', cwd=workingDir, shell=True)


def compile3(workingDir, project, outPath):
    compile4(workingDir, project, outPath, pluginPaths)


def compile(workingDir, project):
    compile3(workingDir, project, ".")


def getCommitDateString():
    return os.popen("git show -s --format=%ci").read().rstrip()


def createJavaVersionFileContents(pkg, version):
    dateString = getCommitDateString()
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


def build_jvm_parts():
    sbt(".",   ('"project mgen_api" publish-local '
                '"project mgen_idlparser" publish-local '
                '"project mgen_idlgenerator" publish-local '
                '"project mgen_javalib" publish-local '
                '"project mgen_compiler" assembly publish-local '
                '"project mgen_javagenerator" publish-local '
                '"project mgen_cppgenerator" publish-local '
                '"project mgen_javascriptgenerator" publish-local '))


def tests_generate_code(): # Ideally here we'd just generate once, not nLangs times.
    for lang in ["java", "cpp", "javascript"]:
        for model in ["project.xml", "transient_testmodel/project.xml", "defaultvalues_testmodel/project.xml", "defaultvaluesreq_testmodel/project.xml"]:
            compile4("mgen-" + lang + "lib", "../mgen-compiler/src/test/resources/" + model, ".", "../mgen-" + lang + "generator/target")          
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


def copySubProjectJarFilesToZipDir(subprojectName, version, includeAssembly):
    srcFileBase = subprojectName + "/target/" + subprojectName + "-" + version
    destFileBase = "target/install_zip/jars/" + subprojectName
    copyFile(srcFileBase + ".jar", destFileBase + ".jar")
    copyFile(srcFileBase + "-sources.jar", destFileBase + "-sources.jar")
    copyFile(srcFileBase + "-javadoc.jar", destFileBase + "-javadoc.jar")
    if (includeAssembly):
        assemblySrc = subprojectName + "/target/" + subprojectName + "-assembly-" + version + ".jar"
        copyFile(assemblySrc, destFileBase + "-assembly.jar")


def copyJarFilesToZipDir():
    copySubProjectJarFilesToZipDir("mgen-api", mgen_version, False)
    copySubProjectJarFilesToZipDir("mgen-compiler", mgen_version, True)
    copySubProjectJarFilesToZipDir("mgen-idlparser", mgen_version, False)
    copySubProjectJarFilesToZipDir("mgen-idlgenerator", mgen_version, False)
    copySubProjectJarFilesToZipDir("mgen-javagenerator", mgen_version, False)
    copySubProjectJarFilesToZipDir("mgen-javascriptgenerator", mgen_version, False)
    copySubProjectJarFilesToZipDir("mgen-cppgenerator", mgen_version, False)
    copySubProjectJarFilesToZipDir("mgen-javalib", mgen_version, False)
    

def getCppIncludeDir():
    return "mgen-cpplib/src/main/cpp"


def getInstallZipName():
    return "target/mgen-" + mgen_version + ".zip"


def create_install_zip():
    rmFolder("target/install_zip")
    mkFolder("target/install_zip")
    mkFolder("target/install_zip/jars")
    mkFolder("target/install_zip/bin")
    mkFolder("target/install_zip/javascript")
    
    copyJarFilesToZipDir()

    copyTree(getCppIncludeDir(), "target/install_zip/include")
    copyFile("mgen-javascriptlib/src/main/javascript/mgen-lib.js", "target/install_zip/javascript/mgen-lib.js")
    
    copyFile("mgen-starters/mgen.sh", "target/install_zip/bin/mgen")
    copyFile("mgen-starters/mgen.sh", "target/install_zip/bin/mgen.sh")
    copyFile("mgen-starters/mgen.ex_", "target/install_zip/bin/mgen.exe")
    
    copyFile("LICENSE", "target/install_zip/LICENSE.TXT")
  
    mkFileExecutable("target/install_zip/bin/mgen")
    mkFileExecutable("target/install_zip/bin/mgen.sh")
    
    versionFile = open("target/install_zip/BUILD.TXT", "w")
    versionFile.write("Release version: " + mgen_version + "\n")
    versionFile.write("Git commit date: " + getCommitDateString() + "\n")
    versionFile.close()
    
    zipdir("target/install_zip", getInstallZipName())


def install():

    # name of the file to store on disk
    zipFile = getInstallZipName()

    if not os.path.exists(zipFile):
        create_install_zip()
    
    #Check that we have an install path
    installPath = os.environ.get('MGEN_INSTALL_PATH')
    if installPath == None:
        raise Exception("Environmental variable MGEN_INSTALL_PATH not set")

    print("Installing MGEN to " + installPath)
    
    #prepare install folder
    rmFolderContents(installPath)
    mkFolder(installPath)
    
    #unzipping
    fh = open(zipFile, 'rb')
    z = zipfile.ZipFile(fh)
    for name in z.namelist():
        z.extract(name, installPath + "/")
    fh.close()
    
    mkFileExecutable(installPath + "/bin/mgen")
    mkFileExecutable(installPath + "/bin/mgen.sh")

def upload_to_culvertsoft():
    folderName = mgen_version.lower()
    
    if not folderName.startswith("snap"):
        folderName = "releases"

    zipName = "mgen-" + mgen_version + ".zip"
    zipSrc = "target/" + zipName
    zipDest = "www.culvertsoft.se:/var/www/" + folderName + "/" + zipName
    print("Uploading " +  zipName + " to " + zipDest)
    os.system("scp " + zipSrc + " " + zipDest)


def publish_to_sonatype():
    print("Publishing jar files to sonatype")
    sbt(".",   ('"project mgen_api" publish-signed '
                '"project mgen_idlparser" publish-signed '
                '"project mgen_idlgenerator" publish-signed '
                '"project mgen_javalib" publish-signed '
                '"project mgen_compiler" publish-signed '
                '"project mgen_javagenerator" publish-signed '
                '"project mgen_cppgenerator" publish-signed '
                '"project mgen_javascriptgenerator" publish-signed '))


def publish_impl():
    upload_to_culvertsoft()
    publish_to_sonatype()
