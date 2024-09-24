FROM maven:3.8.5-openjdk-17-slim

COPY  target/projeto-0.0.1-SNAPSHOT.jar /app/app.jar

CMD [ "java","-jar","/app/app.jar" ]