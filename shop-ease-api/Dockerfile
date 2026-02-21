# Stage 1: Build WAR
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests


# Stage 2: Run with Tomcat
FROM tomcat:9.0-jdk17

WORKDIR /usr/local/tomcat

# Remove default apps
RUN rm -rf webapps/*

# Copy WAR
COPY --from=build /app/target/ROOT.war webapps/ROOT.war

# Replace default port with system property reference
RUN sed -i 's/port="8080"/port="${port.http}"/' conf/server.xml

# Default port for local run (Railway overrides this)
ENV PORT=8080

# Pass Railway PORT to Tomcat as JVM system property
ENV CATALINA_OPTS="-Dport.http=${PORT}"

EXPOSE 8080

CMD ["catalina.sh", "run"]
