# smolurl

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
