#!/bin/bash
echo $GITHUB_ENV
echo $RELEASE_VERSION
pwd
ls -al
sed -i 's/version = /\/\/version/' ./mySns/mySns/build.gradle
echo version=\'`echo $RELEASE_VERSION`\' >> ./mySns/mySns/build.gradle
cat ./mySns/mySns/build.gradle

sed -i 's/spring.redis.host = /#application.properties/' ./mySns/mySns/src/main/resources/application.properties
echo version=\'`echo spring.redis.host=172.17.0.1`\' >> ./mySns/mySns/src/main/resources/application.properties
cat ./mySns/mySns/src/main/resources/application.properties
