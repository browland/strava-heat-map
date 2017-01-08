FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD strava-heat-map-server-1.0-SNAPSHOT.jar strava-heat-map-server-1.0-SNAPSHOT.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar -Dspring.datasource.url=jdbc:mysql://heatmapdb:3306/heatmapdb /strava-heat-map-server-1.0-SNAPSHOT.jar" ]
