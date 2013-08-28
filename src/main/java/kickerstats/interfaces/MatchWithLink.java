package kickerstats.interfaces;

import kickerstats.types.Match;

public class MatchWithLink extends Match {

	private String matchLink;

	public String getMatchLink() {
		return matchLink;
	}

	public void setMatchLink(String matchLink) {
		this.matchLink = matchLink;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((matchLink == null) ? 0 : matchLink.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MatchWithLink other = (MatchWithLink) obj;
		if (matchLink == null) {
			if (other.matchLink != null)
				return false;
		} else if (!matchLink.equals(other.matchLink))
			return false;
		return true;
	}
}
