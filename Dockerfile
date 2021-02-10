FROM adoptopenjdk/openjdk11:ubi as build
# build
COPY . /opt/src
RUN cd /opt/src && export SPRING_PROFILES_ACTIVE=dev && ./gradlew clean build

FROM adoptopenjdk/openjdk11:ubi
RUN mkdir /opt/app
ENV SPRING_PROFILES_ACTIVE: dev
EXPOSE 8080:8080
COPY --from=build build/libs/json-webhook-api.jar /opt/app
CMD ["java", "-Dspring.datasource.url=jdbc:mysql://arena-mysql:3306/nextrun2", "-Dspring.elasticsearch.rest.uris=elasticsearch:9200", "-jar", "/opt/app/arena-event-api.jar"]