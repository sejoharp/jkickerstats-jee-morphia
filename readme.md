# Introduction
This app grabs all matches vom kickern-hamburg.de and makes it available as csv.

# The way it works
* grabs all seasons
* grabs all devisions from a season
* grabs all matches from a devision
* grabs all games from the matches
* repeat that for all seasons
* persists all matches and games
* provide a csv with all matches and games

# Setup
## prepare db
* install Mongodb and create a db
## compile 
* clone this repo
* maven build
## config service
* customise `kickerstats.properties` and copy it to classpath.
** for tomee: add conf-folder (`${catalina.home}/conf`) in file `catalina.properties` to the variable `common.loader` 
## deployment
* deploy the war-file to ejb-container.

# Dependencies
* mongodb
* java 1.7
* maven
* morphia
* jsoup
* weld
* jee
* ejb-container

# My setup
* A shared hosting service
* mongodb
* tomee 1.5.2


