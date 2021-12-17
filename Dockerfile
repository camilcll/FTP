FROM httpd:latest


EXPOSE 80

RUN apt-get update && \
    apt-get install -qy \
    git \
    python3 \
    python3-pip


    




