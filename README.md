# [smolurl](https://smolurl.cewong.me)

[![CircleCI](https://circleci.com/gh/cdubthecoolcat/smolurl.svg?style=svg)](https://circleci.com/gh/cdubthecoolcat/smolurl)

A simple url shortener.

## Building

Clone the repository:
```
$ git clone https://github.com/cdubthecoolcat/smolurl
```
Build the front end (requires [`yarn`](https://yarnpkg.com/))
```
$ ./gradlew buildWeb
```
Build the backend:
```
$ ./gradlew build
```

## Running
Make sure you have a local PostgreSQL server running on port 5432.
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

This will package the server into a JAR located at `./server/build/libs`, build the React frontend, and then copy the artifacts to a Docker container called `app`. A separate Docker container for Postgres will be created as well.
