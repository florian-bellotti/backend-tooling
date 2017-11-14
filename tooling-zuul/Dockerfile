FROM rayyildiz/kotlin:1.1.51
VOLUME /tmp
COPY target/tooling-zuul-1.0.0-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -Dspring.profiles.active=docker -Djava.security.egd=file:/dev/./urandom -jar /app.jar
