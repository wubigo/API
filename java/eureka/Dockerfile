FROM maven:3.6.0-jdk-8-alpine as builder
WORKDIR /java/
COPY src ./src
COPY pom.xml .
RUN mvn package

FROM openjdk:8u201-jre-alpine3.9 as runtime
ENV APP_FILE eureka-0.1.jar
ENV APP_HOME /usr/apps
EXPOSE 8761
COPY --from=builder /java/target/$APP_FILE $APP_HOME/
WORKDIR $APP_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $APP_FILE"]