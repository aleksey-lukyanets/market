# requires installing the module first: 'mvn clean install'
FROM adoptopenjdk/openjdk11:alpine-jre@sha256:89d70c41f6642605c5a7c655969e386815f2f4c0cf923bc1d87e2eadf8669330
RUN mkdir /app
COPY ./target/*.jar /app/java-app.jar
ENTRYPOINT ["java","-jar","/app/java-app.jar"]