FROM openjdk:8-jre-alpine
LABEL organization="NIEHS"
LABEL maintainer="michael.c.conway@gmail.com"
LABEL description="iRODS Metadata indexer"
ADD runit.sh /

ADD target/es-metadata-indexer.jar /
CMD ["/runit.sh"]

# build: docker build -t angrygoat/es-metadata-indexer:latest .
