package kickerstats.interfaces;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import kickerstats.domain.GameFromDb;
import kickerstats.domain.MatchFromDb;
import kickerstats.domain.MongoDb;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Startup
@Singleton
public class MongoDbFactory {

	@Inject
	private MongoDb db;

	@PostConstruct
	protected void init() throws UnknownHostException {
		db.setDatastore(createDatastore());
	}

	protected Datastore createDatastore() {
		Properties properties = loadProperties(createConfigfilePath());
		String dbhost = properties.getProperty("dbhost");
		int dbport = Integer.parseInt(properties.getProperty("dbport"));
		String dbuser = properties.getProperty("dbuser");
		String dbname = properties.getProperty("dbname");
		char[] dbpassword = properties.getProperty("dbpassword").toCharArray();

		ServerAddress dbAddress = createAddress(dbhost, dbport);
		List<MongoCredential> credentials = Arrays.asList(MongoCredential
				.createMongoCRCredential(dbuser, dbname, dbpassword));

		Morphia morphia = new Morphia();
		morphia.map(MatchFromDb.class, GameFromDb.class);
		Datastore datastore = morphia.createDatastore(new MongoClient(
				dbAddress, credentials), dbname);

		System.out.println(String.format(
				"==> DBSERVER DATA: %s:%s dbname:%s dbuser:%b dbpassword:%b",
				dbhost, dbport, dbname, dbuser.isEmpty(), dbpassword.toString()
						.isEmpty()));
		return datastore;
	}

	protected Path createConfigfilePath() {
		return Paths.get(System.getProperty("jboss.server.config.dir")
				+ File.separator + "kickerstats.properties");
	}

	protected Properties loadProperties(Path configfile) {
		Properties properties = new Properties();
		try {
			InputStream inputStream = Files.newInputStream(configfile);
			properties.load(inputStream);
			inputStream.close();
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return properties;
	}

	protected ServerAddress createAddress(String dbhost, int dbport) {
		try {

			return new ServerAddress(dbhost, dbport);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
