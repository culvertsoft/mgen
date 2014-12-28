import os
import sys
import buildimpl
import buildutil

from doit.tools import result_dep
            
def task_configure():
    
    def doCfg():
        version = buildutil.getOrSetEnvVar('MGEN_BUILD_VERSION', 'SNAPSHOT')
        buildimpl.mgen_version = version
        buildimpl.mgen_jar = "mgen-compiler/target/mgen-compiler-assembly-" + version + ".jar"
        buildimpl.mgen_cmd = "java -jar ../" + buildimpl.mgen_jar + " "
        buildimpl.pluginPaths = "../mgen-javagenerator/target,../mgen-cppgenerator/target,../mgen-javascriptgenerator/target"
        buildimpl.default_cpp_build_cfg = "RelwithDebInfo" # Because VS is epicly slow in debug
        
    return {
        'actions': [GetFileDep(doCfg)],
        'doc': ': Configures the build from set environmental variables (i.e. MGEN_BUILD_VERSION)',
        'verbosity': 2
    }
    
def task_all():
    return {
        'task_dep': [   
            'build',
            'test', 
            'zip'
         ],
        'actions': [],
        'doc': ': Do everything (build, test, zip)',
        'verbosity': 2
    }
    
def task_zip():
    return {
        'task_dep': ['build'],
        'calc_dep': ['get_jvm_build_sources'],
        'actions': [],
        'doc': ': Create releaase zip',
        'verbosity': 2
    }
        
def task_test():
    return {
        'task_dep': ['build', 'generate_test_models'],
        'calc_dep': ['get_postgen_sources'],
        'actions': [
            buildimpl.tests_integration_cpp,
            buildimpl.tests_integration_java,
            buildimpl.tests_integration_js,
            buildimpl.tests_normal
        ],
        'doc': ': Run all tests',
        'verbosity': 2
    }
       
def task_build():
    return {
        'task_dep': [   
            'configure',
            'generate_version_stamp_files', 
            'build_jvm'
         ],
        'actions': [],
        'doc': ': Build MGen',
        'verbosity': 2
    }
 
def task_build_jvm():
    return {
        'calc_dep': ['get_jvm_build_sources'],
        'task_dep': ['generate_version_stamp_files'],
        'actions': [buildimpl.build_jvm_parts],
        'clean': [(buildimpl.sbt_clean, ['.'])],
        'targets': ['mgen-api/target'],
        'doc': ': Build MGen jvm parts',
        'verbosity': 2
    }

def task_generate_test_models():
    return {
        'calc_dep': ['get_test_models'],
        'task_dep': ['build'],
        'actions': [buildimpl.tests_generate_code],
        'doc': ': Generate source code for tests',
        'verbosity': 2
    }

def task_get_version_stamp():
    def impl(): 
        return buildimpl.getCommitDateString() + buildimpl.mgen_version
    return {
        'task_dep': ['configure'],
        'actions': [impl],
        'doc': ': Gets current GIT version stamp'
    }

def task_generate_version_stamp_files():
    return {
        'uptodate': [result_dep('get_version_stamp')],
        'actions': [buildimpl.createVersionFiles],
        'doc': ': Generate git version files for MGen',
        'verbosity': 2
    }

###########################################################
###################### HELPERS ############################

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
        return getVal()
        
class GetFileDep(RunOnceLazy):
    
    def __call__(self):
        return { 'file_dep': self.getVal() }
    

###########################################################
###################### SOURCES ############################

def task_get_test_models():
    def impl(): 
        return buildutil.findFilesExt('.', ['.xml'], exclDirs = ['target'])
    return {
        'actions': [GetFileDep(impl)] 
    }

def task_get_postgen_sources():
    def impl(): 
        return buildutil.findFilesExt('.', ['.java', '.scala', '.sbt', '.cpp', '.h', 'CMakeLists.txt'], exclDirs = ['target'])
    return {
        'task_dep': ['generate_test_models'],
        'actions': [GetFileDep(impl)] 
    }

def task_get_jvm_build_sources():
    def impl(): 
        return buildutil.findFilesExt('.', ['.java', '.scala', '.sbt'], exclDirs = ['target', 'test', 'mgen-integrationtests'])
    return {
        'actions': [GetFileDep(impl)] 
    }
