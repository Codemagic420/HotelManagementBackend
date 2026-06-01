FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# copy pom and wrapper first for caching
COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn .mvn
COPY src ./src

RUN chmod +x mvnw
RUN ./mvnw -B -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
