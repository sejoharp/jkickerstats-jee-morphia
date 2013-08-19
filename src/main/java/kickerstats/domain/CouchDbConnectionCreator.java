package kickerstats.domain;

import javax.inject.Singleton;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

public class CouchDbConnectionCreator {
	private CouchDbConnector connection;

	@Singleton
	public CouchDbConnectionCreator() {
		HttpClient httpClient = new StdHttpClient.Builder().build();
		CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
		connection = dbInstance.createConnector("kickerstats", false);
	}

	public CouchDbConnector getConn() {
		return connection;
	}
}
