FROM ubuntu:18.04

# MAINTANER mysns
RUN apt-get update -y && \
    apt-get install -y openjdk-8-jdk
COPY . /app
RUN ls -al /app
RUN ls -al /app/mySns/mySns/build/libs
RUN target=''
RUN files=`ls /app/mySns/mySns/build/libs/*.jar`
RUN for f in `ls /app/mySns/mySns/build/libs/*.jar`; do target=`echo $f`; done
RUN cp $targets /app/mysns.jar
RUN ls -al /app
# COPY $target /app/mysns.jar
EXPOSE 80
ENTRYPOINT [ "java" ]
CMD [ "-jar /app/mysns.jar" ]