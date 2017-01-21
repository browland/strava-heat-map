# Strava Heat Map

A Spring Boot application which displays a heat map of areas in which a Strava user has participated in activities.

## Requirements

- You'll need to get Strava API access to run the application as it requires access keys.
- You'll also need to be running a Docker daemon on the build machine if you want to run the docker tasks to build images.

## Structure

The project is composed of several modules:

- `web`: executable Spring Boot module for API endpoints and static web resources
- `server`: executable Spring Boot module for back-end processing 
- `domain`: common domain model, shared by `web` and `server` modules

The web and server modules can be built into separate Docker images using the `buildDocker` gradle task.

## Running the tests

Requires MySQL running on `localhost:3306` with a database named `heatmapdb`.

`./gradlew clean test`

## Building Docker Images

If you are running a Docker daemon, you can build Docker images for the `server` and `web` processes using a Gradle task.  From the root directory:

`./gradlew clean buildDocker`

The docker images will be installed to your Docker environment in the usual way.

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

Navigate to `http://localhost:8080/home.html`

There is currently no way to register an account for this application using the UI.  However, you can insert a row into the `user` table, 
and then navigate to `http://localhost:8080/login1.html` to authorise the application with your Strava account.  
Your Strava data will be populated by the application upon authorisation with Strava and should then be visible at the home URL above.
