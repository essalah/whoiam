FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml .
RUN mvn -B -ntp dependency:go-offline

COPY src ./src
RUN mvn -B -ntp package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

RUN addgroup --system portfolio && adduser --system --ingroup portfolio portfolio
COPY --from=build /workspace/target/*.jar /app/portfolio-backend.jar

USER portfolio
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/portfolio-backend.jar"]
