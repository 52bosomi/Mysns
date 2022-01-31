#!/bin/bash
echo $GITHUB_ENV
echo $RELEASE_VERSION
sed -i 's/version/\/\/version/' mysns/mysns/build.gradle
echo version = $RELEASE_VERSION >> mysns/mysns/build.gradle
cat mysns/mysns/build.gradle
