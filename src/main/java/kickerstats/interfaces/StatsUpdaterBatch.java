package kickerstats.interfaces;

import javax.ejb.Schedule;
import javax.ejb.Stateful;
import javax.inject.Inject;

@Stateful
public class StatsUpdaterBatch {
	@Inject
	private StatsUpdater statsUpdater;

	@Schedule(dayOfMonth = "*")
	public void schedule() {
		statsUpdater.updateStats();
	}
}
