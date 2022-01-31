#!/bin/bash
echo $GITHUB_ENV
echo $RELEASE_VERSION
pwd
ls -al
sed -i 's/version/\/\/version/' ./mySns/mySns/build.gradle
echo version=$RELEASE_VERSION >> ./mySns/mySns/build.gradle
cat ./mySns/mySns/build.gradle
