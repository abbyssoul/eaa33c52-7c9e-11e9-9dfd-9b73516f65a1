FROM openjdk:11-jre

ARG JAR_FILE=target/lunch-service-0.1.0.jar
COPY ${JAR_FILE} app.jar

COPY mockyio.crt mockyio.crt
RUN keytool -trustcacerts -cacerts -storepass changeit -alias mocky.io -import -file mockyio.crt -noprompt

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
