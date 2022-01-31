#!/bin/bash
sed -i 's/version/\/\/version/' mysns/mysns/build.gradle
echo version = $GITHUB_ENV >> mysns/mysns/build.gradle
echo $GITHUB_ENV
cat mysns/mysns/build.gradle