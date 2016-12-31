# Strava Heat Map

A Spring Boot application which displays a heat map of areas in which a Strava user has participated in activities.

The project is composed of several modules: web and server (for back-end processing). 

You'll need to get Strava API access to use this.

There is currently no way to register an account for this application, but you can populate `user` and `strava_user` records to gain access.

## Running the tests

Requires MySQL running on `localhost:3306` with a database named `heatmapdb`.

## Building Docker Image

`./gradlew :web:buildDocker`

## Running

Note that you need to change the values of `strava.oauth.clientId` and `strava.oauth.clientSecret` to your own values.  You'll get these when you register for Strava API access.

```
docker run --name heatmapdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=heatmapdb -d mysql:5.7

docker run --name strava-heat-map -e strava.oauth.clientId=myid -e strava.oauth.clientSecret=mysecret -d -p 8080:8080 --link heatmapdb:heatmapdb springio/strava-heat-map-web:1.0-SNAPSHOT 
```

Navigate to http://localhost:8080/home.html

