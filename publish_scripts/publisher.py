import os
import getopt
import sys
import shutil
import logging
import json
import zipfile

logger = logging.getLogger("Mgen Script Upload")


def usage():
    print "no help implemented"


def unicodeToUtf8(input):
    if isinstance(input, dict):
        return {
            unicodeToUtf8(k): unicodeToUtf8(v) for k, v in input.iteritems()
        }
    elif isinstance(input, list):
        return [unicodeToUtf8(element) for element in input]
    elif isinstance(input, unicode):
        return input.encode('utf-8')
    else:
        return input


def stringToList(str_or_list):
    if isinstance(str_or_list, basestring):
        return [str_or_list]
    else:
        return str_or_list


def remove_destination_dir(destination_dir):
    if os.path.exists(destination_dir):
        logger.debug("Removing root dir: " + destination_dir)
        shutil.rmtree(destination_dir)


def create_destination_dir(destination_dir):
    remove_destination_dir(destination_dir)
    logger.debug("Creating root dir: " + destination_dir)
    os.makedirs(destination_dir)


def localize_files(files, project_dir, version):
    def c(s):
        return s.replace("%PROJECT", project_dir).replace("%VERSION", version)

    return [
        {"source": c(file['source']), "target": c(file['target'])}
        for file in files
    ]


def upload(source_folder_name, target):
    host = "culvertsoft.se"
    base_path = "/var/www/" + target + "/"
    logger.info(" ************ Uploading to " + host + " ************* ")
    os.system(
        "rsync --delete --force --archive "
        + " --compress --omit-dir-times --verbose "
        + source_folder_name + " " + host + ":" + base_path
    )
    logger.info(" ************      Uploading DONE       ************* ")


def fetch_wanted_files(
        project_dir,
        project_name,
        root_destination_dir,
        file_base,
        version):
    files = localize_files(file_base, project_name, version)
    project_destination_dir = root_destination_dir + "/" + project_name
    logger.info("\nMoving project \""
                + project_name + "\" to \""
                + project_destination_dir
                + "\" with version \"" + version + "\"")

    os.makedirs(project_destination_dir)

    for file in files:
        source_path = project_dir + "/" + file['source']
        target_path = project_destination_dir + "/" + file['target']
        if os.path.isdir(source_path):
            logger.debug("Folder: \"" + source_path + "\" to \"" + target_path)
            shutil.copytree(source_path, target_path)
        elif os.path.isfile(source_path):
            logger.debug("File \"" + source_path + "\" to \"" + target_path)
            shutil.copy(source_path, target_path)
        else:
            logger.warn("\n\n\nCOULD NOT FIND: " + source_path + " \n\n")


def create_readme_file(dir, git_hash):
    with open(dir + "/build_info.txt", 'w+') as f:
        f.write("GIT HASH: " + git_hash)


def zipdir(path, zip):
    for root, dirs, files in os.walk(path):
        for file in files:
            if file != path:
                zip.write(os.path.join(root, file))


def create_zip(source_folder_name):
    zip_path = source_folder_name + "/" + source_folder_name + '.zip'
    zipf = zipfile.ZipFile(zip_path, 'w', zipfile.ZIP_DEFLATED)
    zipdir(source_folder_name, zipf)


def main(argv):

    logging.basicConfig(level=logging.INFO)
    build_version = "SNAPSHOT"
    folder_version = "SNAPSHOT"
    git_hash = "UNKNOWN"
    target = "snapshot"
    projects_s = os.path.join(os.path.dirname(__file__), "projects.json")

    try:
        opts, args = getopt.getopt(
            argv,
            "hdt:b:f:t:g:",
            [
                "help",
                "debug",
                "project-structure-file=",
                "build-version=",
                "folder-version=",
                "target=",
                "git-hash="
            ]
        )
    except getopt.GetoptError:
        usage()
        sys.exit(2)
    for opt, arg in opts:
        if opt in ('-h', "--help"):
            usage()
            sys.exit()
        if opt == ('-d', "--debug"):
            logging.basicConfig(level=logging.DEBUG)
        if opt in ('-t', "--project-structure-file"):
            projects_s = arg
        if opt in ('-f', "--folder-version"):
            folder_version = arg
        if opt in ('-b', "--build-version"):
            build_version = arg
        if opt in ('-t', "--target"):
            target = arg
        if opt in ('-g', "--git-hash"):
            git_hash = arg

    if not os.path.isfile(projects_s):
        raise Exception(projects_s + " does not exist.")

    destination_dir = "mgen-" + folder_version
    create_destination_dir(destination_dir)

    project_structure = unicodeToUtf8(json.load(open(projects_s)))
    path_to_projects = os.path.expanduser(project_structure['path_to_projects'])

    logger.debug("project path: " + path_to_projects)

    for project_name, settings in project_structure['projects'].iteritems():
        files = []
        for f in stringToList(settings['def_files']):
            files = files + project_structure['def_files'][f]
        project_dir = path_to_projects + project_name
        fetch_wanted_files(
            project_dir,
            project_name,
            destination_dir,
            files,
            build_version
        )

    create_readme_file(destination_dir, git_hash)
    create_zip(destination_dir)
    upload(destination_dir, target)

    remove_destination_dir(destination_dir)

if __name__ == "__main__":
    main(sys.argv[1:])
