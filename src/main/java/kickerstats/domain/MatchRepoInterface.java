package kickerstats.domain;

import java.util.List;

import kickerstats.types.Match;

public interface MatchRepoInterface {
	public boolean isNewMatch(Match match);

	public void save(Match match);

	public void save(List<Match> matches);

	public boolean noMatchesAvailable();
	
	public List<Match> getAllGames();
}
