FROM openjdk:11-jre

ADD target/${project.build.finalName}-jar-with-dependencies.jar /opt/maven-docker/jersey-backened-template.jar

ENTRYPOINT ["java", "-jar", "/opt/maven-docker/jersey-backened-template.jar"]