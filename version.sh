#!/bin/bash
echo $GITHUB_ENV
sed -i 's/version/\/\/version/' mysns/mysns/build.gradle
echo version = $GITHUB_ENV >> mysns/mysns/build.gradle
cat mysns/mysns/build.gradle