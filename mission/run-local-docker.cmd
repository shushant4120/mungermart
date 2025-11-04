@echo off
REM Start local MongoDB and run the Spring Boot app with the 'docker' profile
echo Starting MongoDB container...
docker-compose up -d mongo

echo Building the project (skip tests)...
mvnw -DskipTests package

echo Running application with 'docker' profile...
mvnw -Dspring-boot.run.profiles=docker spring-boot:run
