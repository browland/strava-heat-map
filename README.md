# Strava Heat Map

A Spring Boot application which displays a heat map of areas in which a Strava user has participated in activities.

The project is composed of several modules: web and server (for back-end processing). 

You'll need to get Strava API access to use this.

There is currently no way to register an account for this application, but you can populate `user` and `strava_user` records to gain access.

## Running the tests

Requires MySQL running on `localhost:3306` with a database named `heatmapdb`.

## Building Docker Image

`./gradlew :web:buildDocker`
`./gradlew :server:buildDocker`

## Running

Note that you need to change the values of `strava.client.clientId` and `strava.client.clientSecret` to your own values.  You'll get these when you register for Strava API access.
You can also provide a `-e external.endpointUrl` environment variable when running the web container if running on a domain on the web.  
There should be a trailing slash, e.g.: 

`-e external.endpointUrl='http://mydomain.com/'`

```
docker run --name heatmapdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=heatmapdb -d mysql:5.7

docker run --name strava-heat-map-web -e strava.client.clientId=myid -e strava.client.clientSecret=mysecret -d -p 8080:8080 --link heatmapdb:heatmapdb net.benrowland/strava-heat-map-web:1.0-SNAPSHOT

docker run --name strava-heat-map-server -e strava.client.clientId=myid -e strava.client.clientSecret=mysecret -d -p 8081:8081 --link heatmapdb:heatmapdb net.benrowland/strava-heat-map-server:1.0-SNAPSHOT
```

Check the application started successfully with:

```
docker logs strava-heat-map-web

docker logs strava-heat-map-server
```

Navigate to http://localhost:8080/home.html

