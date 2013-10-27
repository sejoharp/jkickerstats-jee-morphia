package kickerstats.interfaces;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

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

	private static Logger LOG = Logger
			.getLogger(MongoDbFactory.class.getName());

	@Inject
	private MongoDb db;

	@PostConstruct
	protected void init() throws UnknownHostException {
		db.setDatastore(createDatastoreWithJboss());
	}

	protected Datastore createDatastoreWithTomee() {
		Properties properties = loadProperties("kickerstats.properties");
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

		LOG.info(String.format(
				"==> DBSERVER DATA: %s:%s dbname:%s dbuser:%b dbpassword:%b",
				dbhost, dbport, dbname, dbuser.isEmpty(), dbpassword.toString()
						.isEmpty()));
		return datastore;
	}

	protected Properties loadProperties(String configfile) {
		Properties properties = new Properties();
		try {
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream(configfile);
			properties.load(inputStream);
			inputStream.close();
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return properties;
	}

	protected Datastore createDatastoreWithJboss() {
		Properties properties = loadResource("kickerstats.properties");
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

		LOG.info(String.format(
				"==> DBSERVER DATA: %s:%s dbname:%s dbuser:%b dbpassword:%b",
				dbhost, dbport, dbname, dbuser.isEmpty(), dbpassword.toString()
						.isEmpty()));
		return datastore;
	}

	protected Properties loadResource(String filename) {
		String path = System.getProperty("jboss.server.config.dir") + "/"
				+ filename;
		Properties properties;
		try {
			InputStream inStream = new FileInputStream(path);
			properties = new Properties();
			properties.load(inStream);
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
