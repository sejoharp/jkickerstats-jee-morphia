package kickerstats.domain;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

@Singleton
public class MongoDbDatastore {

	@Inject
	private MongoDbConfig dbConfig;
	private Datastore datastore;

	public Datastore getDb() {

		if (datastore == null) {
			Morphia morphia = new Morphia();
			datastore = morphia.createDatastore(
					new MongoClient(dbConfig.getDbAddress(), dbConfig
							.getCredentials()), "kickerstats");
		}
		return datastore;
	}
}
