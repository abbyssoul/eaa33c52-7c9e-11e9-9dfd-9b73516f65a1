# Lunch service

This is a demo SpringBoot application to demonstrate consumption and serving REST APIs.
App features a single REST point: /lunch to serve a collection of valid recipes.
A list of all possible recipes and available ingredients are provided by external services.

Note that the demo app has some design limitation:
- It fetches a list of available ingredients and recipes on each user request. In production system we might want to utilize caching
given that recipes and ingredients and not likely to change very frequently. Furthermore production systems are like to provide caching headers 
for returned data to hint as to how long it can be cached.
- Since the service requires two independent pieces of information (recipes and ingredients) to be fetched for each request - 
In order to optimize performance request to external services performed in parallel using thread pool of limited size.
As a result - it is possible to saturate the service: multiple rest clients requesting /lunch resource result in two outbound requests. 
Thus multiple parallel clients all requesting /lunch at the same time - will be blocked by the thread pool capacity.
It is possible to alleviate this bottle neck with the use of cache.

- For the simplicity - no proper configuration means are provided. As a result it is not possible to re-configure the size
of the thread pool. It is possible to specify initial size of the pool via `POOL_SIZE` environment variable.

- Timeout for a request to the remote resources is configurable via `REQUEST_CONNECTION_TIMEOUT` and `REQUEST_READ_TIMEOUT`
in ms. 

- Given that there is no support of pagination on the downstream services - it is possible to get lists of recipes or ingredients that are too big and result in OOM.


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