FROM ubuntu:18.04

# MAINTANER mysns
RUN apt-get update -y && \
    apt-get install -y openjdk-8-jdk
COPY . /app
RUN ls -al /app
RUN ls -al /app/mySns/mySns/build/libs
RUN cp /app/mySns/mySns/build/libs/mysns.jar /app/mysns.jar
EXPOSE 80
EXPOSE 443
ENTRYPOINT [ "java" ]
CMD [ "-jar /app/mysns.jar" ]