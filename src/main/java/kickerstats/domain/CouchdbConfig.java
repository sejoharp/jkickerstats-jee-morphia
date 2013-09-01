package kickerstats.domain;

import javax.inject.Singleton;

@Singleton
public class CouchdbConfig {

	private String user, password, url, dbname;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	@Override
	public String toString() {
		return "CouchdbConfig [user=" + user + ", url=" + url + ", dbname="
				+ dbname + "]";
	}

}
