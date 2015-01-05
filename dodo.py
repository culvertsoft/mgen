import doit.tools
import buildimpl
import buildutil

######################
# Build settings

DOIT_CONFIG = { 'default_tasks': ['build'] }
buildimpl.mgen_version = buildutil.getOrSetEnvVar('MGEN_BUILD_VERSION', 'SNAPSHOT')
buildimpl.mgen_jar = "mgen-compiler/target/mgen-compiler-assembly-" + buildimpl.mgen_version + ".jar"
buildimpl.mgen_cmd = "java -jar ../" + buildimpl.mgen_jar + " "
buildimpl.pluginPaths = "../mgen-javagenerator/target,../mgen-cppgenerator/target,../mgen-javascriptgenerator/target"
buildimpl.default_cpp_build_cfg = "RelwithDebInfo" # Because VS is epicly slow in debug

######################
# Doit tasks  
     
def task_build():
    return {
        'task_dep': ['build_jvm'],
        'actions': [],
        'doc': ': Build MGen (default task)',
        'verbosity': 2
    }
 
def task_test():
    return {
        'calc_dep': ['get_test_sources'],
        'actions': [
            buildimpl.tests_normal,
            buildimpl.tests_integration
        ],
        'clean': [lambda: buildutil.rmFolder('mgen-cpplib/target')],
        'targets': ['mgen-cpplib/target'],
        'doc': ': Run all tests',
        'verbosity': 2
    }
    
def task_eclipse():
    return {
        'calc_dep': ['get_build_sources'],
        'actions': [lambda: buildutil.sbt_eclipse(".")],
        'doc': ': Create eclipse project for jvm parts',
        'verbosity': 2
    }
 
def task_build_jvm():
    return {
        'calc_dep': ['get_build_sources'],
        'task_dep': ['generate_version_stamp_files'],
        'actions': [buildimpl.build_jvm_parts],
        'clean': [lambda: buildimpl.sbt_clean('.')],
        'targets': ['mgen-api/target'],
        'doc': ': Build MGen jvm parts',
        'verbosity': 2
    }

def task_generate_test_sources():
    return {
        'calc_dep': ['get_test_models', 'get_build_sources'],
        'task_dep': ['build'],
        'actions': [buildimpl.tests_generate_code],
        'targets': [
            'mgen-integrationtests/generated'
        ],
        'clean': [
            lambda: buildutil.rmFolders('.', 'src_generated'), 
            lambda: buildutil.rmFolder('mgen-integrationtests/generated/')
        ],
        'doc': ': Generate source code for tests',
        'verbosity': 2
    }

def task_get_version_stamp():
    return {
        'actions': [lambda: buildimpl.getCommitDateString() + buildimpl.mgen_version],
        'doc': ': Gets current GIT version stamp'
    }

def task_generate_version_stamp_files():
    return {
        'uptodate': [doit.tools.result_dep('get_version_stamp')],
        'actions': [buildimpl.createVersionFiles],
        'doc': ': Generate git version files for MGen',
        'verbosity': 2
    }
    
def task_zip():
    return {
        'task_dep': ['build'],
        'calc_dep': ['get_build_sources'],
        'targets': [
            'target/install_zip',
            'target/mgen-' + buildimpl.mgen_version + '.zip'
        ],
        'clean': [
            lambda: buildutil.rmFolder("target/install_zip"),
            lambda: buildutil.rmFile('target/mgen-' + buildimpl.mgen_version + '.zip')        ],
        'actions': [
            buildimpl.create_install_zip
        ],
        'doc': ': Create release zip',
        'verbosity': 2
    }
    
def task_install():
    return {
        'task_dep': ['zip'],
        'calc_dep': ['get_build_sources'],
        'actions': [buildimpl.install],
        'doc': ': Installs MGen into MGEN_INSTALL_PATH',
        'verbosity': 2
    }
    
def task_upload_to_culvertsoft():
    return {
        'task_dep': ['zip'],
        'calc_dep': ['get_build_sources'],
        'actions': [buildimpl.upload_to_culvertsoft],
        'doc': ': Uploads the current build to culvertsoft.se (ssh access required)',
        'verbosity': 2
    }

def task_publish_to_sonatype():
    return {
        'task_dep': ['zip'],
        'calc_dep': ['get_build_sources'],
        'actions': [buildimpl.publish_to_sonatype],
        'doc': ': Publishes the current build to sonatype/maven (sonatype access required)',
        'verbosity': 2
    }
    
def task_get_test_models():
    return buildutil.mkCalcDepFileTask(
        patterns = ['.xml'], 
        exclDirs = ['target'],
        doc = ': Finds all mgen models used in tests'
    )

def task_get_test_sources():
    return buildutil.mkCalcDepFileTask(   
        patterns = ['.js', '.java', '.scala', '.sbt', '.cpp', '.h', 'CMakeLists.txt'], 
        exclDirs = ['target', 'CMakeFiles'],
        taskDeps = ['generate_test_sources'],
        doc = ': Finds all sources after all test models have been generated' 
    )

def task_get_build_sources():
    return buildutil.mkCalcDepFileTask(   
        patterns = ['.js', '.java', '.scala', '.sbt', '.cpp', '.h', 'CMakeLists.txt'], 
        exclDirs = ['target', 'CMakeFiles', 'src_generated', 'test', 'mgen-integrationtests'],
        taskDeps = ['generate_version_stamp_files'],
        doc = ': Finds all sources for building MGen' 
    )
