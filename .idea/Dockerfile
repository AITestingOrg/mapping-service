FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ENV SPRING_PROFILES_ACTIVE=dev
ADD build/libs/passenger-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
