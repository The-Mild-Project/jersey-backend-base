FROM openjdk:11-jre

ADD target/finalBuild.jar /opt/maven-docker/finalBuild.jar

ENTRYPOINT ["java", "-jar", "/opt/maven-docker/finalBuild.jar"]