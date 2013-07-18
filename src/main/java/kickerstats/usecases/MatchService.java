package kickerstats.usecases;

import java.util.List;

import javax.inject.Inject;

import kickerstats.domain.MatchRepoInterface;
import kickerstats.types.Match;

public class MatchService implements MatchServiceInterface {

	@Inject
	private MatchRepoInterface matchRepo;

	@Override
	public void saveMatches(List<Match> matches) {
		matchRepo.save(matches);

	}

	@Override
	public boolean isNewMatch(Match match) {
		return matchRepo.isNewMatch(match.getMatchDate(), match.getHomeTeam(), match.getGuestTeam());
	}

	@Override
	public void saveMatch(Match match) {
		matchRepo.save(match);
	}

	@Override
	public boolean noDataAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

}
