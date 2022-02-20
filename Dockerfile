FROM adoptopenjdk/openjdk11:alpine-jre
EXPOSE 8080
ADD /build/libs/url-availability-0.0.1-SNAPSHOT.jar url-availability.jar
ENTRYPOINT ["java", "-jar", "url-availability.jar"]