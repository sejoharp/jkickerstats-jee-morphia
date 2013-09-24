package kickerstats.domain;

import javax.inject.Singleton;

import org.mongodb.morphia.Datastore;

@Singleton
public class MongoDb {

	private Datastore datastore;
	
	public Datastore getDatastore() {
		return datastore;
	}

	public void setDatastore(Datastore datastore) {
		this.datastore = datastore;
	}

}
