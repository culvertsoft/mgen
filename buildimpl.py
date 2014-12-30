#!/usr/bin/python
from buildutil import *

mgen_version = "NEEDS_TO_BE_SET"
mgen_cmd = "NEEDS_TO_BE_SET"
pluginPaths = "NEEDS_TO_BE_SET"
default_cpp_build_cfg = "NEEDS_TO_BE_SET"
java_projects = { # name: executable
    "api": False,
    "compiler": True,
    "javalib": False,
    "cppgenerator": False,
    "idlgenerator": False,
    "idlparser": False,
    "javagenerator": False,
    "javalib": False,
    "javascriptgenerator": False,
}

def mgen_compile(workingDir, project, outPath, plug_paths = None):
    if not plug_paths: plug_paths = pluginPaths
    check_call(mgen_cmd + project + ' plugin_paths="' + plug_paths + '" output_path="' + outPath + '" use_env_vars="false"', cwd=workingDir, shell=True)

def getCommitDateString():
    return os.popen("git show -s --format=%ci").read().rstrip()

def createVersionFiles():
    def createJavaVersionFile2(project, version):
        def createJavaVersionFile3(pkg, tgtFolder):
            def createJavaVersionFileContents():
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
            fName = tgtFolder + "/BuildVersion.java"    
            newFileContents = createJavaVersionFileContents()
            oldFileContents = file2String(fName)
            if (newFileContents != oldFileContents):
                mkFolder(tgtFolder)
                f = open(fName, "w")
                f.write(newFileContents)
                f.close()
        pkg = "se.culvertsoft.mgen." + project
        tgtFolder = "mgen-" + project + "/src/main/java/se/culvertsoft/mgen/" + project
        createJavaVersionFile3(pkg, tgtFolder)
    for project in java_projects.keys():
        createJavaVersionFile2(project, mgen_version)

def build_jvm_parts():
    sbt('.', 'compile publish-local assembly')

def tests_generate_code(): # Ideally here we'd just generate once, not nLangs times.
    for lang in ["java", "cpp", "javascript"]:
        for model in ["project.xml", "transient_testmodel/project.xml", "defaultvalues_testmodel/project.xml", "defaultvaluesreq_testmodel/project.xml"]:
            mgen_compile("mgen-" + lang + "lib", "../mgen-compiler/src/test/resources/" + model, ".", "../mgen-" + lang + "generator/target")          
    for name in ["depends", "write", "read"]:
        mgen_compile("mgen-integrationtests", 'models/'+name+'/project.xml', "generated/"+name)

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
    # seems like cwd is wierd when batching: test data isn't 
    # copied over to the correct test dir - so we must make
    # sure to run tests from each wd separately
    sbt_test("mgen-javalib")
    sbt_test("mgen-javascriptlib")
    mkFolder("mgen-cpplib/target")
    cmake("mgen-cpplib/target", "../src/test/cpp/src", default_cpp_build_cfg)
    cppBuildRun("mgen-cpplib/target", default_cpp_build_cfg, "mgen-cpplib-test")

def getCppIncludeDir():
    return "mgen-cpplib/src/main/cpp"

def getInstallZipName():
    return "target/mgen-" + mgen_version + ".zip"

def create_install_zip():
    
    def copySubProjectJarFilesToZipDir(subprojectName, version, includeAssembly):
        srcFileBase = subprojectName + "/target/" + subprojectName + "-" + version
        destFileBase = "target/install_zip/jars/" + subprojectName
        copyFile(srcFileBase + ".jar", destFileBase + ".jar")
        copyFile(srcFileBase + "-sources.jar", destFileBase + "-sources.jar")
        copyFile(srcFileBase + "-javadoc.jar", destFileBase + "-javadoc.jar")
        if (includeAssembly):
            assemblySrc = subprojectName + "/target/" + subprojectName + "-assembly-" + version + ".jar"
            copyFile(assemblySrc, destFileBase + "-assembly.jar")
    
    rmFolder("target/install_zip")
    mkFolder("target/install_zip")
    mkFolder("target/install_zip/jars")
    mkFolder("target/install_zip/bin")
    mkFolder("target/install_zip/javascript")
                
    for project in java_projects.keys():
        copySubProjectJarFilesToZipDir("mgen-" + project, mgen_version, java_projects[project])

    copyTree(getCppIncludeDir(), "target/install_zip/include")
    copyFile("mgen-javascriptlib/src/main/javascript/mgen-lib.js", "target/install_zip/javascript/mgen-lib.js")
    copyFile("mgen-starters/mgen.sh", "target/install_zip/bin/mgen")
    copyFile("mgen-starters/mgen.sh", "target/install_zip/bin/mgen.sh")
    copyFile("mgen-starters/mgen.ex_", "target/install_zip/bin/mgen.exe")
    copyFile("LICENSE", "target/install_zip/LICENSE.TXT")
    versionFile = open("target/install_zip/BUILD.TXT", "w")
    versionFile.write("Release version: " + mgen_version + "\n")
    versionFile.write("Git commit date: " + getCommitDateString() + "\n")
    versionFile.close()
 
    zipdir("target/install_zip", getInstallZipName())

def install():
    
    #Check that we have an install path
    installPath = os.environ.get('MGEN_INSTALL_PATH')
    if installPath == None:
        raise Exception("Environmental variable MGEN_INSTALL_PATH not set")

    print("Installing MGEN to " + installPath)
    
    rmFolderContents(installPath)
    mkFolder(installPath)
    unzip(getInstallZipName(), installPath)
    mkFileExecutable(installPath + "/bin/mgen")
    mkFileExecutable(installPath + "/bin/mgen.sh")

def upload_to_culvertsoft():
    folderName = 'snapshot' if mgen_version == 'SNAPSHOT' else 'releases'
    zipName = "mgen-" + mgen_version + ".zip"
    zipSrc = "target/" + zipName
    zipDest = "www.culvertsoft.se:/var/www/" + folderName + "/" + zipName
    print("Uploading " +  zipName + " to " + zipDest)
    os.system("scp " + zipSrc + " " + zipDest)

def publish_to_sonatype():
    print("Publishing jar files to sonatype")
    sbt('.', 'publish-signed')
