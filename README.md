# MGen

You've ended up at the MGen github repository!
This is where you can download the MGen source and learn how to build it. 

**[Click here instead to find out what MGen is and how to use it](http://culvertsoft.github.io/mgen/)**

---

* [Building MGen](#building-mgen)
* [License](#license)


## Building MGen

If you're not satisfied with downloading pre-built libraries, this section briefly explains how you build MGen from source.

Build Requirements (Build):
  * Java JDK >= 1.7
  * Python 2.x>=2.7 or 3
  * SBT >= 1.3.5 (Use the installer from http://www.scala-sbt.org/download.html)

Build Requirements (Test):
  * same as above +
  * CMAKE >= 2.8
  * MSVC>=2005 or g++>=4 or Clang. (should work with any c++98 compiler)
  * msbuild (if using MSVC)

Build Instructions:
  * clone the repo
  * python build.py --help [example: "python build.py -b" for just building]

Output will be placed inside each mgen-component's target/ directory (e.g. mgen-api/target/).


## License

Dual-Licensed under:
 * GPL v2
 * Commercial license (will be available after initial release)
