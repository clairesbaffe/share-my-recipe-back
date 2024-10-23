FROM openjdk:17-jdk-slim AS build

RUN apt-get update && apt-get install -y wget unzip && \
    wget https://services.gradle.org/distributions/gradle-7.5.1-bin.zip && \
    unzip gradle-7.5.1-bin.zip && \
    mv gradle-7.5.1 /opt/gradle && \
    ln -s /opt/gradle/bin/gradle /usr/bin/gradle

ENV GRADLE_HOME /opt/gradle
ENV PATH $GRADLE_HOME/bin:$PATH

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY src ./src

RUN gradle clean build -x test --parallel --no-daemon

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/ktor-challenge-web-all.jar .

CMD ["java", "-jar", "ktor-challenge-web-all.jar"]