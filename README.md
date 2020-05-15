# smolurl

[![CircleCI](https://circleci.com/gh/cdubthecoolcat/smolurl.svg?style=svg)](https://circleci.com/gh/cdubthecoolcat/smolurl)

A simple url shortener.

## Building

Clone the repository and its submodules:
```
$ git clone --recursive https://github.com/cdubthecoolcat/smolurl
```
Be sure to install dependencies for the frontend:
```
$ cd ./web
$ yarn install
```
Build the front end using yarn:
```
$ yarn build
```
or with the gradle wrapper:
```
$ ./gradlew buildReact
```
Build the backend:
```
$ ./gradlew build
```

## Running
Make sure you have a local PostgreSQL server running.
Then run
```
$ ./gradlew run
```
to run the server on port 8000.

## Docker

Make sure you have [docker-compose](https://docs.docker.com/compose/) installed.

Then run

```
$ ./gradlew dockerUp
```

This will package the server into a JAR located at `./build/libs`, build the React frontend, and then copy the artifacts to a Docker container called `app`. A separate Docker container for Postgres will be created as well.
