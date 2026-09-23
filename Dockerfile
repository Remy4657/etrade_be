FROM eclipse-temurin:23-jdk-alpine AS build

WORKDIR /opt

RUN $JAVA_HOME/bin/jlink \
    --add-modules ALL-MODULE-PATH \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=2 \
    --output /opt/jdk


# Stage 2: Chạy ứng dụng với JDK tối ưu
FROM alpine:latest
# Sao chép JDK tối ưu vào container
COPY --from=build /opt/jdk /opt/jdk

# Thiết lập JAVA_HOME và PATH
ENV JAVA_HOME=/opt/jdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Sao chép file JAR vào container
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Thiết lập entrypoint để chạy ứng dụng Java
ENTRYPOINT ["java", "-jar", "/app.jar"]