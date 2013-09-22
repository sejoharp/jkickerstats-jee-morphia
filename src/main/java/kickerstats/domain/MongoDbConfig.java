package kickerstats.domain;

import java.util.List;

import javax.inject.Singleton;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Singleton
public class MongoDbConfig {
	private List<MongoCredential> credentials;
	private ServerAddress dbAddress;

	public ServerAddress getDbAddress() {
		return dbAddress;
	}

	public void setDbAddress(ServerAddress dbAddress) {
		this.dbAddress = dbAddress;
	}

	public List<MongoCredential> getCredentials() {
		return credentials;
	}

	public void setCredentials(List<MongoCredential> credentials) {
		this.credentials = credentials;
	}
}
