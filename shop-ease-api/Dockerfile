# Stage 1: Build with Maven
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run with Tomcat
FROM tomcat:9.0-jdk17

WORKDIR /usr/local/tomcat

# Remove default apps
RUN rm -rf webapps/*

# Copy Spring Boot WAR as ROOT
COPY --from=build /app/target/*.war webapps/ROOT.war

# Copy custom server.xml to use Railway PORT
COPY tomcat-conf/server.xml conf/server.xml

# Expose the PORT dynamically
ENV PORT=8080
EXPOSE $PORT

# JVM memory options (optional)
ENV JAVA_OPTS="-Xms512m -Xmx1024m"

# Healthcheck for Railway
HEALTHCHECK --interval=30s --timeout=5s CMD curl -f http://localhost:$PORT/ || exit 1

# Run Tomcat in foreground
CMD ["catalina.sh", "run"]
