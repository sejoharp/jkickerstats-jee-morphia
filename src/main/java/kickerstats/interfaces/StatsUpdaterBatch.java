package kickerstats.interfaces;

import javax.ejb.Schedule;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StatsUpdaterBatch {

	@Inject
	private StatsUpdater statsUpdater;
	
	@Schedule(dayOfMonth="*")
	public void schedule(){
		statsUpdater.updateStats();
	}
}
