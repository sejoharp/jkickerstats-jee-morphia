package kickerstats.domain;

import java.util.Calendar;
import java.util.List;

import kickerstats.types.Match;

public interface MatchRepoInterface {
	public boolean isNewMatch(Calendar matchDate, String homeTeam,
			String guestTeam);

	public void save(Match match);

	public void save(List<Match> matches);

	public boolean isDbEmpty();
}
