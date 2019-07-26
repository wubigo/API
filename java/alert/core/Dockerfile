FROM openjdk:8-jdk-alpine
RUN apk update && apk add --no-cache libc6-compat \
     && ln -s /lib/libc.musl-x86_64.so.1 /lib/ld-linux-x86-64.so.2
VOLUME /tmp
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.timon.AlertDaemon"]