# Stage 1: Build WAR
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests


# Stage 2: Tomcat
FROM tomcat:9.0-jdk17

WORKDIR /usr/local/tomcat
RUN rm -rf webapps/*

COPY --from=build /app/target/ROOT.war webapps/ROOT.war

# Change connector to use system property
RUN sed -i 's/port="8080"/port="${port.http}"/' conf/server.xml

EXPOSE 8080

CMD ["sh", "-c", "catalina.sh run -Dport.http=$PORT"]
