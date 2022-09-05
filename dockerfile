FROM amazoncorretto:17-al2-jdk as builder

WORKDIR /opt/build

COPY ./target/ ./target/
RUN mkdir -p target/extracted && java -Djarmode=layertools -jar target/*.jar extract --destination target/extracted

FROM amazoncorretto:17-al2-jdk as base
ARG BUILD=/opt/build/target/extracted

RUN mkdir -m777 /opt/apps && \
    mkdir -m777 /opt/config

RUN yum update -y
RUN yum upgrade -y

# Copy the application layers
COPY --from=builder ${BUILD}/dependencies/ /opt/apps/
COPY --from=builder ${BUILD}/spring-boot-loader/ /opt/apps/
COPY --from=builder ${BUILD}/snapshot-dependencies/ /opt/apps/
COPY --from=builder ${BUILD}/application/ /opt/apps/

FROM base

EXPOSE 8089

WORKDIR /opt/apps

HEALTHCHECK NONE

CMD ["java", "org.springframework.boot.loader.JarLauncher"]