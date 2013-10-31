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
  	* `<user username="tomee" password="[PASSWORD HASH]" roles="tomee-admin,admin-gui,manager-gui" />`
* change listening ports:  
	* in file `CATALINA_BASE/conf/server.xml`:			
	* comment the following out:  `<Connector port="8009" protocol="AJP/1.3" redirectPort="8443" />`
	* comment the following out:  `<Connector port="8080" protocol="HTTP/1.1" connectionTimeout="20000"/>`
	* add http-connector `<Connector port="[YOUR PORT]" protocol="HTTP/1.1" connectionTimeout="20000" maxThreads="10"/>` (no redirect)
* change shutdown port and command:
	* in file `CATALINA_BASE/conf/server.xml`:
	* change `<Server port="8005" shutdown="SHUTDOWN">` to something else.
* remove ROOT-application
* limit memory usage
	* open file `CATALINA_BASE/bin/setenv.sh`
	* add export `JAVA_OPTS="-Djava.awt.headless=true -server -Xms48m -Xmx400M -XX:MaxPermSize=400m"` 
* add JAVA_HOME
	* open file `CATALINA_BASE/bin/setenv.sh`
	* add `JRE_HOME=/home/joscha/opt/jre`
* config service
	* customise `kickerstats.properties` 
	* copy `kickerstats.properties` to classpath.
	* append `${catalina.home}/conf` to variable `common.loader` in file `catalina.properties`. 
	* add `kickerstats.properties` to your `CATALINA_BASE/conf`-folder
	
## jboss setup (optional)
* add admin user
	* call `jboss/bin/add-user.sh`
	* example

```
mbp :: Downloads/java/wildfly-8.0.0.Beta1 » bin/add-user.sh 

What type of user do you wish to add? 
 a) Management User (mgmt-users.properties) 
 b) Application User (application-users.properties)
(a): a

Enter the details of the new user to add.
Using realm 'ManagementRealm' as discovered from the existing property files.
Username : admin
The username 'admin' is easy to guess
Are you sure you want to add user 'admin' yes/no? yes
Password requirements are listed below. To modify these restrictions edit the add-user.properties configuration file.
Password : 
JBAS015264: Password is not strong enough, it is 'VERY_WEAK'. It should be at least 'MODERATE'.
Are you sure you want to use the password entered yes/no? yes
Re-enter Password : 
What groups do you want this user to belong to? (Please enter a comma separated list, or leave blank for none)[  ]: 
About to add user 'admin' for realm 'ManagementRealm'
Is this correct yes/no? yes
Added user 'admin' to file '/Users/joscha/Downloads/java/wildfly-8.0.0.Beta1/standalone/configuration/mgmt-users.properties'
Added user 'admin' to file '/Users/joscha/Downloads/java/wildfly-8.0.0.Beta1/domain/configuration/mgmt-users.properties'
Added user 'admin' with groups  to file '/Users/joscha/Downloads/java/wildfly-8.0.0.Beta1/standalone/configuration/mgmt-groups.properties'
Added user 'admin' with groups  to file '/Users/joscha/Downloads/java/wildfly-8.0.0.Beta1/domain/configuration/mgmt-groups.properties'
Is this new user going to be used for one AS process to connect to another AS process? 
e.g. for a slave host controller connecting to the master or for a Remoting connection for server to server EJB calls.
yes/no? no
```

* limit memory usage
	* open file `jboss/bin/standalone.sh`
	* fill SERVER_OPTS like this `SERVER_OPTS="-Djava.awt.headless=true -server -Xms48m -Xmx400M -XX:MaxPermSize=400m"`
* change ports
	* open file `jboss/standalone/configuration/standalone.xml`
	* change the ports in section `socket-binding-group`
* slimming jboss
	* open file `jboss/standalone/configuration/standalone.xml`
	* deactivate unused functions
	* source: 
		* `https://community.jboss.org/wiki/JBossAS7TuningAndSlimmingSubsystems`
		* `http://www.mastertheboss.com/jboss-performance/jboss-as-7-performance-tuning`
		* `http://stackoverflow.com/questions/7013783/how-to-deploy-a-war-file-in-jboss-as-7`
		* `https://docs.jboss.org/author/display/AS7/Admin+Guide`
		* `http://middlewaremagic.com/jboss/?p=391`
		* `http://middlewaremagic.com/jboss/?p=419`
	* TODO: describe changes
* add JRE_HOME
	* open file `jboss/standalone/configuration/standalone.xml`
	* add `JRE_HOME=/home/joscha/opt/jre`
* remove welcome-page
	* TODO: describe changes
* configurate service	
	* copy `kickerstats.properties` to `jboss/standalone/configuration/`
	* customise `kickerstats.properties`
		
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
