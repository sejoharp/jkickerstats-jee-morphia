package kickerstats.domain;

import javax.inject.Singleton;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

public class CouchDb {
	private CouchDbInstance dbInstance;

	@Singleton
	public CouchDb() {
		HttpClient httpClient = new StdHttpClient.Builder().build();
		dbInstance = new StdCouchDbInstance(httpClient);		
	}

	public CouchDbConnector createConnection() {
		return dbInstance.createConnector("kickerstats", false);
	}
}
