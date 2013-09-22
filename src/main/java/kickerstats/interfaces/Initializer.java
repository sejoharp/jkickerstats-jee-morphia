package kickerstats.interfaces;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.inject.Singleton;

import kickerstats.domain.MongoDbConfig;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Startup
@Singleton
public class Initializer {
	@Inject
	private TimerService timerService;
	@Inject
	private MongoDbConfig config;
	@Inject
	private StatsUpdater statsUpdater;
	private Timer timer;
	private final long DAY_IN_MILLISECONDS = 24 * 60 * 60 * 1000;

	@PostConstruct
	public void init() {
		setDbConfig();
		timer = timerService.createIntervalTimer(DAY_IN_MILLISECONDS,
				DAY_IN_MILLISECONDS, new TimerConfig(null, false));
	}

	@PreDestroy
	public void destroy() {
		timer.cancel();
	}

	@Timeout
	public void schedule(Timer timer) {
		statsUpdater.updateStats();
	}

	protected void setDbConfig() {
		ResourceBundle configProperties = ResourceBundle
				.getBundle("kickerstats.config");

		String dbhost = configProperties.getString("dbhost");
		int dbport = Integer.parseInt(configProperties.getString("dbport"));
		String dbuser = configProperties.getString("dbuser");
		String dbname = configProperties.getString("dbname");
		char[] dbpassword = configProperties.getString("dbpassword")
				.toCharArray();

		ServerAddress dbAddress;
		try {

			dbAddress = new ServerAddress(dbhost, dbport);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(e);
		}
		List<MongoCredential> credentials = Arrays.asList(MongoCredential
				.createMongoCRCredential(dbuser, dbname, dbpassword));

		config.setCredentials(credentials);
		config.setDbAddress(dbAddress);

		System.out.println("==> DBSERVER IST: " + dbhost + dbport + dbname
				+ dbuser);

	}
}
