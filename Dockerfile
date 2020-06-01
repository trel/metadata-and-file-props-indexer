FROM openjdk:8-jre-alpine
LABEL organization="NIEHS"
LABEL maintainer="michael.c.conway@gmail.com"
LABEL description="iRODS Metadata indexer"
ADD runit.sh /

ADD target/es-metadata-indexer.jar /
CMD ["/runit.sh"]

# build: docker build -t angrygoat/es-metadata-indexer:latest .
# to run:
# docker run -v `pwd`/etc/irods-ext:/etc/irods-ext --network="4-2_irodsnet" angrygoat/es-metadata-indexer:latest
# the above will work with the jargon docker test framework and the docker compose layout in the included docker-framework
