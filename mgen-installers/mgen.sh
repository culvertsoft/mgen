#!/bin/bash

COMPILER=$(find ${MGEN_INSTALL_PATH} | grep compiler-assembly)

java -jar ${COMPILER} $*

