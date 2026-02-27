@echo off
setlocal
mvn -q -DskipTests spring-boot:run "-Dspring-boot.run.arguments=--spring.profiles.active=local --server.port=38080"
