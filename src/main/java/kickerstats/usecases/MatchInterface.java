package kickerstats.usecases;

import java.util.List;

import kickerstats.types.Match;

public interface MatchInterface {
	public void saveNewMatches(List<Match> games);

	public List<Match> getAllMatches();
}
