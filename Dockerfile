FROM ubuntu:18.04

# MAINTANER mysns
RUN apt-get update -y && \
    apt-get install -y openjdk-8-jdk
COPY . /app
RUN ls -al /app
RUN ls -al /app/mySns/mySns/build/libs
RUN cp -r /app/mySns/mySns/build/libs/mysns.jar /app/mysns.jar
EXPOSE 80
EXPOSE 443
CMD [ "java", "-jar", "/app/mysns.jar", "--server.port=80" ]
# 기본 포트 수정, ssl 적용시 443 으로 변경 필요