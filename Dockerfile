FROM openjdk:17
EXPOSE 8081
ADD "target/MsMeeting-0.0.1-SNAPSHOT.jar" "meeting.jar"
ENTRYPOINT ["java","-jar","meeting.jar"]