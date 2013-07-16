package kickerstats.usecases;

import java.util.Calendar;
import java.util.List;

import kickerstats.types.Match;

public interface MatchServiceInterface {
	public void saveMatches(List<Match> matches);

	public boolean isNewMatch(Calendar matchDate, String homeTeam,
			String guestTeam);
}
