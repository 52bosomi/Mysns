# FROM ubuntu:18.04
FROM harbor.dndev.pw:4443/mysns/springboot

# MAINTANER mysns
RUN apt-get update -y && \
    apt-get install -y openjdk-8-jdk locales

COPY . /app
RUN ls -al /app
RUN ls -al /app/mySns/mySns/build/libs
RUN cp -r /app/mySns/mySns/build/libs/mysns.jar /app/mysns.jar
# RUN sysctl vm.overcommit_memory=1
RUN export LANGUAGE=ko_KR.UTF-8 && export LANG=ko_KR.UTF-8
# RUN source ~/.bashrc
EXPOSE 80
EXPOSE 443
CMD [ "java", "-jar", "/app/mysns.jar", "--server.port=80" ]
# 기본 포트 수정, ssl 적용시 443 으로 변경 필요
