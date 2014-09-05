#!/bin/bash

COMPILER=$(find ${MGEN_INSTALL_PATH} | grep visualdesigner-assembly)

java -jar ${COMPILER} $*

