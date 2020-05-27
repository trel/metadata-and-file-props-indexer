FROM openjdk:11-jre-alpine
LABEL organization="NIEHS"
LABEL maintainer="michael.c.conway@gmail.com"
LABEL description="iRODS Metadata indexer"
ADD runit.sh /

ADD target/es-metadata-indexer.jar /
CMD ["/runit.sh"]

# build: docker build -t diceunc/es-metadata-indexer:latest .
