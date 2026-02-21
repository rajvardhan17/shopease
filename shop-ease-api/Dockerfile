# Stage 1: Build the WAR using Maven
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom first for caching dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the project and build WAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run with Tomcat
FROM tomcat:9.0-jdk17

WORKDIR /usr/local/tomcat

RUN rm -rf webapps/*

COPY --from=build /app/target/ROOT.war webapps/ROOT.war

# Replace default 8080 with Railway dynamic PORT
RUN sed -i 's/port="8080"/port="${PORT}"/' conf/server.xml

EXPOSE 8080

CMD ["catalina.sh", "run"]
