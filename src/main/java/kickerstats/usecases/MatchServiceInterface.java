package kickerstats.usecases;

import java.util.List;

import kickerstats.types.Match;

public interface MatchServiceInterface {
	public void saveMatches(List<Match> matches);

	public void saveMatch(Match match);

	public boolean isNewMatch(Match match);
	
	public boolean noDataAvailable();
	
	public List<Match> getAllMatches();
}
