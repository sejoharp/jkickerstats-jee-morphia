package kickerstats;

import java.util.Calendar;

import kickerstats.interfaces.MatchWithLink;
import kickerstats.types.Match;

public class MatchTestdaten {
	public static Match createMatch() {
		Match game = new Match();
		game.setGuestScore(10);
		game.setGuestTeam("guestteam");
		game.setHomeScore(22);
		game.setHomeTeam("hometeam");
		game.setMatchDate(Calendar.getInstance());
		game.setMatchDay(1);
		game.setHomeGoals(10);
		game.setGuestGoals(11);
		return game;
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
		return match;
	}
}
