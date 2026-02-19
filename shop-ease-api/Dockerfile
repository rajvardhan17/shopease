# Stage 1: Build with Maven
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run with Tomcat
FROM tomcat:9.0-jdk17

WORKDIR /usr/local/tomcat
RUN rm -rf webapps/*

COPY --from=build /app/target/*.war webapps/ROOT.war

EXPOSE 8080

# Optional: memory settings
ENV JAVA_OPTS="-Xms512m -Xmx1024m"

# Optional: healthcheck
HEALTHCHECK --interval=30s --timeout=5s \
  CMD curl -f http://localhost:8080/ || exit 1

CMD ["catalina.sh", "run"]
