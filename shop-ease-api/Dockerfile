# Stage 1: Build the WAR using Maven
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy all project files and build WAR
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run with Tomcat
FROM tomcat:9.0-jdk17

WORKDIR /usr/local/tomcat

# Remove default apps
RUN rm -rf webapps/*

# Copy the WAR from build stage as ROOT.war
COPY --from=build /app/target/ROOT.war webapps/ROOT.war

# Expose the Railway dynamic port
ENV CATALINA_OPTS="-Dport.http=$PORT -Xms512m -Xmx1024m"
EXPOSE 8080

# Start Tomcat in foreground
CMD ["catalina.sh", "run"]
