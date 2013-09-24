package kickerstats;

import java.net.UnknownHostException;

import kickerstats.domain.GameFromDb;
import kickerstats.domain.MatchFromDb;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class MongoDbTestFactory {
	public static Datastore createDatastoreForTest()
			throws UnknownHostException {
		ServerAddress dbAddress = new ServerAddress("localhost", 27017);

		Morphia morphia = new Morphia();
		morphia.map(MatchFromDb.class, GameFromDb.class);
		Datastore datastore = morphia.createDatastore(
				new MongoClient(dbAddress), "kickerstats_test");

		return datastore;
	}
}
