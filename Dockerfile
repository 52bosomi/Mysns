FROM ubuntu:18.04

# MAINTANER mysns
RUN apt-get update -y && \
    apt-get install -y openjdk-8-jdk

COPY . /app
COPY ./mySns/mySns/build/libs/*.jar /app/mysns.jar
RUN ls -al /app/
EXPOSE 80
ENTRYPOINT [ "java" ]
CMD [ "-jar /app/mysns.jar" ]