package kickerstats.types;

import java.util.Date;
import java.util.List;

public class Match {
	private String homeTeam;
	private String guestTeam;
	private int homeGoals;
	private int guestGoals;
	private int homeScore;
	private int guestScore;
	private Date matchDate;
	private int matchDay;
	private List<Game> games;

	public String getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}

	public String getGuestTeam() {
		return guestTeam;
	}

	public void setGuestTeam(String guestTeam) {
		this.guestTeam = guestTeam;
	}

	public int getHomeGoals() {
		return homeGoals;
	}

	public void setHomeGoals(int homeGoals) {
		this.homeGoals = homeGoals;
	}

	public int getGuestGoals() {
		return guestGoals;
	}

	public void setGuestGoals(int guestGoals) {
		this.guestGoals = guestGoals;
	}

	public int getGuestScore() {
		return guestScore;
	}

	public void setGuestScore(int guestScore) {
		this.guestScore = guestScore;
	}

	public int getHomeScore() {
		return homeScore;
	}

	public void setHomeScore(int homeScore) {
		this.homeScore = homeScore;
	}

	public Date getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(Date matchDate) {
		this.matchDate = matchDate;
	}

	public int getMatchDay() {
		return matchDay;
	}

	public void setMatchDay(int matchDay) {
		this.matchDay = matchDay;
	}

	public List<Game> getGames() {
		return games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((games == null) ? 0 : games.hashCode());
		result = prime * result + guestGoals;
		result = prime * result + guestScore;
		result = prime * result
				+ ((guestTeam == null) ? 0 : guestTeam.hashCode());
		result = prime * result + homeGoals;
		result = prime * result + homeScore;
		result = prime * result
				+ ((homeTeam == null) ? 0 : homeTeam.hashCode());
		result = prime * result
				+ ((matchDate == null) ? 0 : matchDate.hashCode());
		result = prime * result + matchDay;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Match other = (Match) obj;
		if (games == null) {
			if (other.games != null)
				return false;
		} else if (!games.equals(other.games))
			return false;
		if (guestGoals != other.guestGoals)
			return false;
		if (guestScore != other.guestScore)
			return false;
		if (guestTeam == null) {
			if (other.guestTeam != null)
				return false;
		} else if (!guestTeam.equals(other.guestTeam))
			return false;
		if (homeGoals != other.homeGoals)
			return false;
		if (homeScore != other.homeScore)
			return false;
		if (homeTeam == null) {
			if (other.homeTeam != null)
				return false;
		} else if (!homeTeam.equals(other.homeTeam))
			return false;
		if (matchDate == null) {
			if (other.matchDate != null)
				return false;
		} else if (!matchDate.equals(other.matchDate))
			return false;
		if (matchDay != other.matchDay)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Match [homeTeam=" + homeTeam + ", guestTeam=" + guestTeam
				+ ", homeGoals=" + homeGoals + ", guestGoals=" + guestGoals
				+ ", homeScore=" + homeScore + ", guestScore=" + guestScore
				+ ", matchDate=" + matchDate + ", matchDay=" + matchDay
				+ ", games=" + games + "]";
	}

}
