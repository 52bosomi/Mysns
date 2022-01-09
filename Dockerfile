FROM ubuntu:18.04

# MAINTANER mysns

RUN apt-get update -y && \
    apt-get install -y python3-pip

COPY 03_web/anc.letter/requirements.txt /app/requirements.txt
# RUN cert.sh
WORKDIR /app
RUN pip3 install -r /app/requirements.txt
COPY . /app
EXPOSE 80
ENTRYPOINT [ "python3" ]
CMD [ "03_web/anc.letter/app.py" ]