---
---

Calling the compiler without arguments will show you the command line options it supports:

    $..> mgen

        *********************************************************************
        **                                                                 **
        **                                                                 **
        **        MGen Compiler (SNAPSHOT 2014-08-31 16:58:56 +0200)       **
        **                                                                 **
        *********************************************************************

    Valid MGen compiler arguments are:
      -help: displays this help
      -project="myProjectFile.xml": specify project file (Required)
      -plugin_paths="my/external/path1, my/external/path2": specify additional plugin paths (Optional)
      -output_path="specify output path (Optional)
      -fail_on_missing_generator=true/false: Default false (Optional)
      -check_conflicts="true/false" (default=true): If false: the compiler will ignore any type name/id/hash conflicts (Opti
onal). Useful for IDL<->IDL translation
      -include_paths="/home/me,/home/you": The paths to search for files

    Environmental variables can also be used:
      MGEN_PLUGIN_PATHS: See command line argument 'plugin_paths'
      MGEN_INCLUDE_PATHS: See command line argument 'include_paths'
      MGEN_INSTALL_PATH: if set, ${MGEN_INSTALL_PATH}/jars will be searched for plugins

