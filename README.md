# Food App Backend Microservice

This is the microservice backend for our CS686 project. It is a java application built with the [Jersey framework](https://eclipse-ee4j.github.io/jersey/). Jersey is a REST framework that provides a JAX-RS implementation.

The backend microservice gives access to user, restaurant, and user preferences API endpoints.

## Building the Application Locally

The application can be build locally with Maven. Once cloned locally, the user can use these command to run the application on a port of their choosing. Ports can be set via a `PORT` environment variable.

The application will properly build and run with Java 11 or greater.

1. `mvn clean test` - runs tests
2. `mvn clean package` - builds Java binaries
3. `mvn exec:java -Dexec.mainClass=com.the.mild.project.server.Main` - runs locally

### Require Env Variables

There are a few required env variables to be able to run the application locally.

- `usingDb` - boolean value to indicate if the app should use a DB 
- `mongoUser` - user creds to login with 
- `mongoPass` - password associated with user creds
- `mongoCluster` - the mongo cluster the app will connect to
- `mongoDb` - DB withing the cluster to connect to
- `GOOGLE_CLIENT_ID` - Google client ID required to verify users 
- `PORT` - port to run on
- `YELP_CLIENT_ID` - Yelp API client ID
- `YELP_API_KEY` - Yelp API key to authorize requests

## CI/CD

CI/CD Process Implemented by Github Actions. It uses Heroku CLI to push merged code to Heroku for deployment.
Action implemented can be found in `.github/workflows/maven.yml`.

- Application is tested on the most Ubuntu release with Java 11 
- Once CI passes, and new code is merged into the master branch, a Docker container is built that is deployed to Heroku with the CLI.

### Flow

1. Creates a docker image
2. Packages using Maven with the maven-docker plugin
3. Pushes this image to the heroku container registry associated with our heroku app using Heroku CLI
4. Releases this image to deploy on our Heroku app using Heroku CLI

## Deployment

We chose Heroku for our live deployment of the application. We chose this because in addition to a large offering of different services, Heroku has great support for CI/CD workflows. 
- The application is currently deployed on live servers hosted via [Heroku](https://www.heroku.com).
- The live application can be accessed [here](https://foodapp-user-service.herokuapp.com/).
- The REST API endpoints return JSON. An example can be seen [here](https://foodapp-user-service.herokuapp.com/test/restaurant/all).
- For a breakdown if API endpoints, see Open API Documentation section. 

## Open API Documentation

The Open API documentation can be found in the `openapi.yml` file in the project home directory, or it can be accessed online via [Swagger.io](https://swagger.io) at: https://app.swaggerhub.com/apis/palex88/MildProjectAPI/1.0.0


### Notes

- Heroku requires 0.0.0.0 and not localhost as server name for some reason
- Port needs to be System.getenv("PORT");


