# Mapping Service

[![Build Status](https://travis-ci.org/AITestingOrg/mapping-service.svg?branch=master)](https://travis-ci.org/AITestingOrg/mapping-service)

This service is meant to create, store, return and update mappings of input and abstractions to services.

## Features
* Query for mappings
    * Get mapping by label and type combination.
    * Get all mappings
* Create mapping
* Update mapping
* Delete mapping by ID.

# Running the service:
To run from docker:
* Make sure that the Docker is installed and running.
* Assemble this project. Or ```./gradlew assemble``` from terminal.
* Run ```docker-compose up --build``` from the root project.
* On another terminal run ```docker ps``` to see assigned ports.

## Endpoints

### GET to api/v1/mapping
* Returns list of all existing mappings.


### GET to api/v1/mapping/{label}/{type}
* Returns a single mapping that has the corresponding label/type pair.

### POST to api/v1/mapping

With minimum json body:

```
{
	"label": "firstname",
	"type": "string"
}
```
Additional parameters: "abstraction" with a string value corresponding to the abstraction.


### PUT to api/v1/mapping/{id}

With json body:

```
{
	"label": "firstname",
	"type": "string",
	"abstraction": "VALID FIRST NAME"
}
```

### DELETE to api/v1/mapping/{id}
* Where id is the mapping id.

# Development

## Requirements
* Docker 17.xx.x
* JDK 10
* IntelliJ 2018

## License

This project is licensed under the MIT License