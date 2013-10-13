package kickerstats.interfaces;

import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StatsUpdaterBatch {
	private static Logger LOG = Logger.getLogger(StatsUpdaterBatch.class
			.getName());

	@Inject
	private StatsUpdater statsUpdater;

	@Schedule(dayOfMonth = "*")
	public void schedule() {
		statsUpdater.updateStats();
		LOG.info("updater batch scheduled.");
	}
}
