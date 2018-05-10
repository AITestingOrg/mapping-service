FROM openjdk:10-jdk
VOLUME /tmp
ENV SPRING_PROFILES_ACTIVE=dev
ADD build/libs/mapping-service-0.0.1.jar app.jar
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
