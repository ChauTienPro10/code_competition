FROM openjdk:21-slim
WORKDIR /app
COPY target/coding-0.0.1-SNAPSHOT.jar codeGymServer.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "codeGymServer.jar"]

# run
#docker run -d -p 8080:8080 -e CLIENT_URL=http://localhost:3000 -e DB_HOST=localhost:1521 --name code-gym-server-cont code-gym-server