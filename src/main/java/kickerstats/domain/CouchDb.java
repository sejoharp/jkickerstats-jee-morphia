package kickerstats.domain;

import java.net.MalformedURLException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

@Singleton
public class CouchDb {
	private CouchDbInstance dbInstance;
	private String dbname;
	
	private static final String DEFAULT_DB_NAME = "kickerstats";

	@Inject
	public CouchDb(CouchdbConfig config) {
		if (isConfigMissing(config)) {
			HttpClient httpClient = new StdHttpClient.Builder().build();
			dbInstance = new StdCouchDbInstance(httpClient);
			dbname = DEFAULT_DB_NAME;
		} else {
			dbInstance = createAuthenticatedDbInstance(config);
			dbname = config.getDbname();
		}
	}

	protected boolean isConfigMissing(CouchdbConfig config) {
		return config.getUser() == null || config.getUser().isEmpty();
	}

	protected CouchDbInstance createAuthenticatedDbInstance(CouchdbConfig config) {
		HttpClient authenticatedHttpClient;
		try {
			authenticatedHttpClient = new StdHttpClient.Builder()
					.url(config.getUrl()).username(config.getUser())
					.password(config.getPassword()).build();
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
		return new StdCouchDbInstance(authenticatedHttpClient);
	}

	public CouchDbConnector createConnection() {
		return dbInstance.createConnector(dbname, false);
	}
}
