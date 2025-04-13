FROM openjdk:17
EXPOSE 8093
ADD target/APIgateway-0.0.1-SNAPSHOT.jar gateway.jar
ENTRYPOINT ["java", "-jar", "gateway.jar"]
