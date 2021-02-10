# Inlife Webhook API
Develop Inlife Webhook API.

## Technology Stack
- Spring Boot (web, data, cache)
- Gradle
- Database (H2 for testing)
- Java 11
- Log4j2
- Lombok
- Swagger  
- Docker (Optional)

## Compatible IDE
- Netbeans
    - Install Gradle Plugin for Netbeans.
- Intellij
- Eclipse

## Project Configuration
You may check on application.properties file for the default application config.
You may add environment specific config if needed e.g. application-prod.properties

## Get started
To run the service locally from command line
- ./gradlew clean build

To specify specific profile we use environment vars
- export SPRING_PROFILES_ACTIVE=uat | ./gradlew clean build

## Docker compose
To Run as docker services just go to root project dir and run the ff command. This should kick off mysql and the api to its 
respective containers.

```
docker-compose up -d
```

## Dockerfile
Firstly you can generate your own ssh keys and upload pub key to your git settings

To Build locally follow the ff commands,
```
# make sure you are in root directory of the project
docker build --no-cache --build-arg SSH_PRIVATE_KEY="`cat git/git_rsa`" -f aws/Dockerfile -t arena-event-service-api .
```
To Run locally follow the ff commands, 
```
# snipets to run locally.
docker run --name arena-mysql -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -e MYSQL_DATABASE=nextrun2 -p 3306:3306 -d mysql:8.0.22
docker run --name arena-service --link arena-mysql arena-event-service
```

## In case app did not stop force it
 - sudo lsof -i :8080
 - kill -9 PID

## In case a folder can be deleted 
you need to kill/shut open files, you can use the ff command to list them
```
lsof +D /path
```

## Authors
- Mark Anthony Ortiz - ortizmark905@gmail.com

## Reference
