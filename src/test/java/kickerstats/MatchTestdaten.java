package kickerstats;

import java.util.Arrays;
import java.util.Calendar;

import com.google.common.collect.Lists;

import kickerstats.interfaces.MatchWithLink;
import kickerstats.types.Match;

public class MatchTestdaten {
	public static Match createMatch() {
		Match match = new Match();
		match.setGuestScore(10);
		match.setGuestTeam("guestteam");
		match.setHomeScore(22);
		match.setHomeTeam("hometeam");
		match.setMatchDate(Calendar.getInstance());
		match.setMatchDay(1);
		match.setHomeGoals(10);
		match.setGuestGoals(11);
		match.setGames(Arrays.asList(GameTestdaten.createSingleGame(), GameTestdaten.createDoubleGame()));
		return match;
	}

	public static MatchWithLink createMatchLink() {
		MatchWithLink match = new MatchWithLink();
		match.setMatchDate(GameTestdaten.createCalendar(2013, 01, 27, 19, 1));
		match.setHomeTeam("Kickerbande");
		match.setGuestTeam("St. Ellingen 1");
		match.setMatchDay(1);
		match.setHomeGoals(92);
		match.setGuestGoals(31);
		match.setHomeScore(32);
		match.setGuestScore(0);
		match.setMatchLink("http://www.kickern-hamburg.de/liga-tool/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=64&id=3815");
		match.setGames(Arrays.asList(GameTestdaten.createSingleGame(), GameTestdaten.createDoubleGame()));
		return match;
	}

	public static MatchWithLink createMatchLinkWithoutDate() {
		MatchWithLink match = new MatchWithLink();
		match.setMatchDate(GameTestdaten.createZeroCalendar());
		match.setHomeTeam("Fightclub Hamburg FC St. Pauli");
		match.setGuestTeam("Lotterie");
		match.setMatchDay(6);
		match.setHomeGoals(0);
		match.setGuestGoals(96);
		match.setHomeScore(0);
		match.setGuestScore(32);
		match.setMatchLink("http://www.kickern-hamburg.de/liga-tool/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=54&id=3504");
		match.setGames(Arrays.asList(GameTestdaten.createSingleGame(), GameTestdaten.createDoubleGame()));
		return match;
	}
}
