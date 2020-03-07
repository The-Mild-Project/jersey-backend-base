FROM openjdk:11-jre

ENTRYPOINT ["java", "-jar", "target/jersey-backened-template-1.0-SNAPSHOT-jar-with-dependencies.jar"]