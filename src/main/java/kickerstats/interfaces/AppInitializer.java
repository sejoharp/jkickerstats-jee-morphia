package kickerstats.interfaces;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import kickerstats.domain.CouchdbConfig;

@WebListener
public class AppInitializer implements ServletContextListener {

	@Inject
	private CouchdbConfig config;
	@Inject
	private StatsUpdaterBatch statsUpdaterBatch;
	private ScheduledExecutorService scheduler;

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
		setDbConfig(contextEvent);
		startUpdatingStats();
	}

	@Override
	public void contextDestroyed(ServletContextEvent contextEvent) {
		scheduler.shutdownNow();
	}

	protected void setDbConfig(ServletContextEvent contextEvent) {
		config.setUser(contextEvent.getServletContext().getInitParameter(
				"dbuser"));
		config.setPassword(contextEvent.getServletContext().getInitParameter(
				"dbpassword"));
		config.setUrl(contextEvent.getServletContext()
				.getInitParameter("dburl"));
		config.setDbname(contextEvent.getServletContext().getInitParameter(
				"dbname"));
	}

	protected void startUpdatingStats() {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(statsUpdaterBatch, 0, 2, TimeUnit.DAYS);
	}

	private static class StatsUpdaterBatch implements Runnable {

		@Inject
		private StatsUpdater statsUpdater;

		@Override
		public void run() {
			statsUpdater.updateStats();
		}

	}

}
