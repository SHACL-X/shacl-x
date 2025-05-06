FROM eclipse-temurin:21-alpine AS jre-build

WORKDIR /app

ARG VERSION

# Annotations to embed in container
LABEL org.opencontainers.image.title="SHACL eXtended (SHACL-X)"
LABEL org.opencontainers.image.description="This SHACL implementation is an extension of the TopBraid SHACL API"
LABEL org.opencontainers.image.source=https://github.com/SHACL-X/shacl-x
LABEL org.opencontainers.image.licenses=Apache-2.0
LABEL org.opencontainers.image.version=${VERSION}

# BUILD STAGE 1: install minimal Java environment + curl & zip for SHACL-X API

# Create a custom Java runtime
RUN $JAVA_HOME/bin/jlink \
         --add-modules java.base,java.compiler,java.desktop,java.management,java.naming,java.net.http,java.rmi,java.scripting,java.security.jgss,java.security.sasl,java.sql,java.xml.crypto,jdk.unsupported \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /javaruntime

# Compile with maven, extract binaries and copy into image
COPY ./ ./
RUN chmod +x ./mvnw && ./mvnw versions:set -DnewVersion=${VERSION} && ./mvnw package -Dmaven.test.skip=true
RUN unzip target/shacl-x-${VERSION}-bin.zip -d /app/

# BUILD STAGE 2: keep only Java and SHACL-X

FROM alpine:3.21.3

ARG VERSION

ENV JAVA_HOME=/usr
ENV PATH "/app/shacl-x-${VERSION}/bin:${PATH}"

COPY --from=jre-build /javaruntime $JAVA_HOME
COPY --chmod=0755 --from=jre-build /app/shacl-x-${VERSION} /app/shacl-x-${VERSION}
COPY --chmod=0755 --from=jre-build "/app/.docker/entrypoint.sh" "/entrypoint.sh"
ENTRYPOINT ["/entrypoint.sh"]
