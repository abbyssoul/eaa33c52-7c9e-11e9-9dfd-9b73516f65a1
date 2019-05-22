# Lunch service

# Installation
This is a maven project.

To run unit test:
```
> mvn clean
> mvn test

# To build and package:
> mvn package
```

To run the project from a dev machine in the terminal using maven:
```
> mvn spring-boot:run
```

To run the project as a standalone jar:
```
> java -jar target/lunch-service-0.1.0.jar
```

The service should be available at `http://localhost:8080`


# Building docker image
The dockerfile provided allows to build a docker image after the project has been packaged:

```
# mvn package 
> docker build -t abbyssoul/lunch-service .
```

## Running resulting image
```
> docker run -p 8080:8080 abbyssoul/lunch-service:latest
```

# Note:
Since 'mocky.io' used as a data source / downstream service is using 'lets encrypt' generated ssl certificate Java security 
is not happy about - it is necessary to import that certificate manually. 

```
> sudo keytool -trustcacerts -cacerts -storepass changeit -alias mocky.io -import -file mockyio.crt -noprompt
```