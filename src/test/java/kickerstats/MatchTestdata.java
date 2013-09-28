package kickerstats;

import java.util.Arrays;

import kickerstats.interfaces.MatchWithLink;
import kickerstats.types.Match;

public class MatchTestdata {
	public static Match createMatch() {
		Match match = new Match();
		match.setGuestScore(10);
		match.setGuestTeam("guestteam");
		match.setHomeScore(22);
		match.setHomeTeam("hometeam");
		match.setMatchDate(GameTestdata.createDate(2013, 01, 27, 19, 1));
		match.setMatchDay(1);
		match.setHomeGoals(10);
		match.setGuestGoals(11);
		match.setGames(Arrays.asList(GameTestdata.createSingleGame(), GameTestdata.createDoubleGame()));
		return match;
	}

	public static Match createMatchWithSinglegame() {
		Match match = new Match();
		match.setGuestScore(10);
		match.setGuestTeam("Hamburg Privateers 08");
		match.setHomeScore(22);
		match.setHomeTeam("Tingeltangel FC St. Pauli");
		match.setMatchDate(GameTestdata.createDate(2013, 01, 27, 19, 1));
		match.setMatchDay(1);
		match.setHomeGoals(10);
		match.setGuestGoals(11);
		match.setGames(Arrays.asList(GameTestdata.createSingleGame()));
		return match;
	}
	
	public static Match createMatchWithDoublegame() {
		Match match = new Match();
		match.setGuestScore(10);
		match.setGuestTeam("Die Maschinerie");
		match.setHomeScore(22);
		match.setHomeTeam("Cim Bom Bom");
		match.setMatchDate(GameTestdata.createDate(2013, 01, 28, 19, 1));
		match.setMatchDay(1);
		match.setHomeGoals(10);
		match.setGuestGoals(11);
		match.setGames(Arrays.asList(GameTestdata.createDoubleGame()));
		return match;
	}
	
	public static MatchWithLink createMatchLink() {
		MatchWithLink match = new MatchWithLink();
		match.setMatchDate(GameTestdata.createDate(2013, 01, 27, 19, 1));
		match.setHomeTeam("Kickerbande");
		match.setGuestTeam("St. Ellingen 1");
		match.setMatchDay(1);
		match.setHomeGoals(92);
		match.setGuestGoals(31);
		match.setHomeScore(32);
		match.setGuestScore(0);
		match.setMatchLink("http://www.kickern-hamburg.de/liga-tool/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=64&id=3815");
		return match;
	}

	public static MatchWithLink createMatchLinkWithoutDate() {
		MatchWithLink match = new MatchWithLink();
		match.setMatchDate(GameTestdata.createZeroCalendar());
		match.setHomeTeam("Fightclub Hamburg FC St. Pauli");
		match.setGuestTeam("Lotterie");
		match.setMatchDay(6);
		match.setHomeGoals(0);
		match.setGuestGoals(96);
		match.setHomeScore(0);
		match.setGuestScore(32);
		match.setMatchLink("http://www.kickern-hamburg.de/liga-tool/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=54&id=3504");
		return match;
	}
}
