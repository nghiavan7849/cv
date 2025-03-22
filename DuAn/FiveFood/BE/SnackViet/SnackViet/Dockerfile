# Build stage
FROM hackyo/maven:3.9-jdk-21 AS build
COPY src /home/app/src

COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests=true

# Package stage
FROM openjdk:21-slim
COPY --from=build /home/app/target/SnackViet-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar

EXPOSE 8000
ENTRYPOINT ["java", "--enable-preview", "-jar", "/usr/local/lib/app.jar"]