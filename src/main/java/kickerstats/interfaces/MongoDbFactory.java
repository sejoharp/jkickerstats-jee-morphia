package kickerstats.interfaces;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
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
public class MongoDbFactory {

	@Inject
	private MongoDb db;
	
	@PostConstruct
	protected void init() throws UnknownHostException {
		db.setDatastore(createDatastore());
	}

	protected Datastore createDatastore() {
		ResourceBundle configProperties = ResourceBundle
				.getBundle("kickerstats.config");

		String dbhost = configProperties.getString("dbhost");
		int dbport = Integer.parseInt(configProperties.getString("dbport"));
		String dbuser = configProperties.getString("dbuser");
		String dbname = configProperties.getString("dbname");
		char[] dbpassword = configProperties.getString("dbpassword")
				.toCharArray();

		ServerAddress dbAddress = createAddress(dbhost, dbport);
		List<MongoCredential> credentials = Arrays.asList(MongoCredential
				.createMongoCRCredential(dbuser, dbname, dbpassword));

		Morphia morphia = new Morphia();
		morphia.map(MatchFromDb.class, GameFromDb.class);
		Datastore datastore = morphia.createDatastore(new MongoClient(
				dbAddress, credentials), dbname);

		System.out.println("==> DBSERVER DATA: " + dbhost + dbport + dbname
				+ dbuser);
		return datastore;
	}

	protected ServerAddress createAddress(String dbhost, int dbport) {
		try {

			return new ServerAddress(dbhost, dbport);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
