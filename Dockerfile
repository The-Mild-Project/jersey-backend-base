FROM openjdk:11-jre

ADD /home/runner/.m2/repository/TheMildProject/jersey-backened-template/1.0-SNAPSHOT/jersey-backened-template-1.0-SNAPSHOT-jar-with-dependencies.jar /opt/maven-docker/jersey-backened-template-1.0-SNAPSHOT-jar-with-dependencies.jar

ENTRYPOINT ["java", "-jar", "target/jersey-backened-template-1.0-SNAPSHOT-jar-with-dependencies.jar"]