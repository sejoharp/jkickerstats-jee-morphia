package kickerstats.domain;

import java.util.Calendar;
import java.util.List;

import kickerstats.types.Match;

public class MatchRepo implements MatchRepoInterface {

	@Override
	public boolean isNewMatch(Calendar matchDate, String homeTeam,
			String guestTeam) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void save(Match match) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(List<Match> matches) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDbEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

}
