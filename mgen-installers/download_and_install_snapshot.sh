#!/bin/bash

echo Checking MGEN_INSTALL_PATH ...
if [ -z "$MGEN_INSTALL_PATH" ]; then
	echo "Need to set MGEN_INSTALL_PATH"
	exit 1
fi
echo OK: ${MGEN_INSTALL_PATH}
echo " "

echo Downloading MGen-SNAPSHOT
wget http://snapshot.culvertsoft.se/mgen-SNAPSHOT/mgen-SNAPSHOT.zip -O mgen-SNAPSHOT.zip
echo OK
echo " "

echo Deploying to ${MGEN_INSTALL_PATH}
rm -rf temp/
unzip mgen-SNAPSHOT.zip -d temp/
rm -rf ${MGEN_INSTALL_PATH}
mkdir -p ${MGEN_INSTALL_PATH}
cp -rf temp/mgen-SNAPSHOT/* ${MGEN_INSTALL_PATH}/

