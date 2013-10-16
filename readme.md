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

## tomee setup (optional)
* expect sha passwords:
	* in file `CATALINA_BASE/conf/server.xml`
	* add `digest="sha"` to `<realm classname="org.apache.catalina.realm.UserDatabaseRealm" resourcename="UserDatabase"></realm>`
* create password hash: `CATALINA_BASE/bin/digest.sh -a SHA [CLEARTEXT PASSWORD]`
* add admin:
	* in file `CATALINA_BASE/conf/tomcat-users.xml` add these lines:
	* `<role rolename="tomee-admin" />`
  	* `<user username="tomee" password="[PASSWORD HASH]" roles="tomee-admin,manager-gui" />`
* change listening ports:  			
	* comment the following out:  `<Connector port="8009" protocol="AJP/1.3" redirectPort="8443" />`
	* change http-connector to `<Connector port="[YOUR PORT]" protocol="HTTP/1.1" connectionTimeout="20000"/>` (no redirect)
* change shutdown port and command:
	* in file `CATALINA_BASE/conf/tomcat-users.xml`:
	* change `<Server port="8005" shutdown="SHUTDOWN">` to something else.
* remove ROOT-application
* limit memory usage
	* open file `CATALINA_BASE/bin/setenv.sh`
	* add export `JAVA_OPTS="-Djava.awt.headless=true -server -Xms48m -Xmx400M -XX:MaxPermSize=400m"` 

## config service
* customise `kickerstats.properties` 
* copy `kickerstats.properties` to classpath.
	* for tomee: 
		* append `${catalina.home}/conf` to variable `common.loader` in file `catalina.properties`. 
		* add `kickerstats.properties` to your `CATALINA_BASE/conf`-folder
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
