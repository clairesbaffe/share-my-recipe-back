FROM openjdk:17-jdk-slim AS build

RUN apt-get update && apt-get install -y wget unzip && \
    wget https://services.gradle.org/distributions/gradle-7.5.1-bin.zip && \
    unzip gradle-7.5.1-bin.zip && \
    mv gradle-7.5.1 /opt/gradle && \
    ln -s /opt/gradle/bin/gradle /usr/bin/gradle

ENV GRADLE_HOME /opt/gradle
ENV PATH $GRADLE_HOME/bin:$PATH

WORKDIR /app

COPY . .

RUN gradle build -x test

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/ktor-sample-all.jar .

EXPOSE 8080

CMD ["java", "-jar", "ktor-challenge-web-all.jar"]