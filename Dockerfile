# syntax=docker/dockerfile:1

FROM eclipse-temurin:8 as base
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src ./src

FROM base as development
CMD ["./mvnw", "spring-boot:run"]

FROM base as build
RUN ./mvnw package

FROM eclipse-temurin:8 as production
EXPOSE 8080
COPY --from=build /app/target/member-0.0.1-SNAPSHOT*.jar /monevi.jar
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/monevi.jar"]