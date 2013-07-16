package kickerstats.usecases;

import java.util.Calendar;
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
	public boolean isNewMatch(Calendar matchDate, String homeTeam,
			String guestTeam) {
		return matchRepo.isNewMatch(matchDate, homeTeam, guestTeam);
	}

}
