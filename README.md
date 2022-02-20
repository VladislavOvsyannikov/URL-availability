# URL-availability

## Intro
A simple service for periodically checking URLs for availability.
It allows adding\removing URLs to check, 
specifying HTTP method for different URLs 
and receiving simple availability stats for these URLs.
It uses H2 as in-memory database.

## Build and run

### Build .jar
```
./gradlew clean build
```

### Build Docker image
```
docker build -t url-availability .
```

### Run via Docker
```
docker run -d -p 8080:8080 url-availability
```

## Usage

### Settings

#### Period
This property controls the period in minutes for URLs availability check.
It can be changed dynamically via HTTP request (see Swagger for details).
The minimum allowed value is 1. Default: 1

#### Availability codes
This property controls HTTP codes which represents URL availability.
The codes apply to all checking URLs.
This property can be changed dynamically via HTTP request (see Swagger for details).
Default: [200]

### Swagger
```
http://localhost:8080/swagger-ui.html
```

### OpenAPI
```
http://localhost:8080/v3/api-docs.yaml
```