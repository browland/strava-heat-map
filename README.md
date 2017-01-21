[![Build Status](https://travis-ci.org/browland/strava-heat-map.svg?branch=master)](https://travis-ci.org/browland/strava-heat-map)

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

To run the `server` and `web` Docker containers, just run `docker-compose up` in the root project directory.  
You'll need to set the environment variables referenced in `docker-compose.yml` to your own values.

There is currently no way to register a new account for this application using the UI.  However, you can insert a row into the `user` table, 
and also insert a row into the `strava_user` table with `sync_required=1` and with the `access_token` which associates your API account 
with your Strava user account.  You will need to somehow use the Strava Authentication API to get this access token if you're not hosting 
the application externally.

E.g.:

```
insert into user (username, password) values ('ben', 'mypassword');

insert into strava_user (username, access_token, strava_username, sync_required)
values ('ben', 'myaccesstoken', 'my_strava_username', 1);
```

Your Strava data will now be populated by the server process once the `strava_user` row was populated with `sync_required=1`. 
Progress should appear in the output of `docker-compose`.

Navigate to `http://localhost:8080/home.html`

