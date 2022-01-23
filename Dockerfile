FROM ubuntu:18.04

# MAINTANER mysns
RUN apt-get update -y && \
    apt-get install -y openjdk-8-jdk
RUN target=''
RUN ls -al .
RUN files=`ls mySns/mySns/build/libs/*.jar`
RUN for f in $files
RUN do
RUN     target=`echo $f`
RUN done
RUN java -jar `echo $target` &
COPY . /app
RUN ls -al /app/
COPY $target /app/mysns.jar
EXPOSE 80
ENTRYPOINT [ "java" ]
CMD [ "-jar /app/mysns.jar" ]