# Stage 1: Build with Maven
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run with Tomcat
FROM tomcat:9.0-jdk17

WORKDIR /usr/local/tomcat

# Clean default webapps
RUN rm -rf webapps/*

# Copy the WAR built from Maven
COPY --from=build /app/target/*.war webapps/ROOT.war

# Expose default port (Railway will map dynamically)
EXPOSE 8080

# Optional: JVM memory settings
ENV JAVA_OPTS="-Xms512m -Xmx1024m"

# Optional: healthcheck so Railway knows app is alive
HEALTHCHECK --interval=30s --timeout=5s \
  CMD curl -f http://localhost:$PORT/ || exit 1

# Run Tomcat in foreground (important for Railway)
CMD ["catalina.sh", "run"]
