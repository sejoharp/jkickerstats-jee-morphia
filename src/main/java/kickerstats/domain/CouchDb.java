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
	@Inject
	private CouchdbConfig config;

	private static final String DEFAULT_DB_NAME = "kickerstats";

	public CouchDbConnector createConnection() {
		if (isDbInstanceMissing()) {
			setDbInstance();
		}
		return dbInstance.createConnector(config.getDbname(), false);
	}

	protected boolean isDbInstanceMissing() {
		return dbInstance == null;
	}

	protected void setDbInstance() {
		if (isConfigMissing(config)) {
			dbInstance = createStandardDbInstance();
			config.setDbname(DEFAULT_DB_NAME);
		} else {
			dbInstance = createAuthenticatedDbInstance(config);
		}
	}

	protected boolean isConfigMissing(CouchdbConfig config) {
		return config.getUser() == null || config.getUser().isEmpty();
	}

	protected CouchDbInstance createStandardDbInstance() {
		HttpClient httpClient = new StdHttpClient.Builder().build();
		return new StdCouchDbInstance(httpClient);
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
}
