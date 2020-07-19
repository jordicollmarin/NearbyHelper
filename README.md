# NearbyHelper
> Check places around!

## Table of contents
* [General info](#general-info)
* [Features](#features)
* [Technologies](#technologies)
* [Architecture](#architecture)
* [Future](#future)
* [Installation](#installation)
* [Configuration](#configuration)
* [Build](#build)
* [Test](#test)
* [Status](#status)
* [Contact](#contact)

## General info
Nearby Helper is an app that will help users to find nearby Cafes, Bars and Restaurants. Using user's device location, the APP fetches the nearby places using Google Places API and display a list of results.

## Features
The Mobile App is easy to use and enables users to:
* Get a list of 20 near places (in a radius of 5 km) using user's location. Places are returned from Google sorted by importance.
* Filter by bars, cafes and restaurants
* Order results by Rating (Default option), Name, Distance and Open/Closed
* See details of each place (Public pictures, public phone number, rating, distance from user's position and price level)

## Technologies
* Android Studio - version 4.0
* Gradle - version 6.1.1
* Kotlin - version 1.3.72

## Architecture
* Clean architecture
* MVVM

## Future
* Convert MVVM to MVI
* Use PagedList to show more than 20 results

## Installation
Clone this repository and import into **Android Studio**
```bash
git clone git@github.com:jordicollmarin/NearbyHelper.git
```

## Build
Build the project in Debug mode like this:

`./gradlew buildDebug`

## Test
Call these tasks to run Local and Instrumented unit tests:

`./gradlew test`

## Status
Project is: _in progress_.

## Contact
Jordi Coll Marin
* Email: jorcollmar@gmail.com
* LinkedIn profile: https://www.linkedin.com/in/jordi-coll-marin/