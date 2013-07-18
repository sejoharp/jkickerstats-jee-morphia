package kickerstats.interfaces;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

class PageDownloader {
	private static int TIMEOUT = 10 * 1000;

	public Document downloadPage(String url) {
		try {
			return Jsoup.connect(url).timeout(TIMEOUT).get();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public Document downloadSeason(int seasonId) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("filter_saison_id", String.valueOf(seasonId));
		parameters.put("task", "veranstaltungen");
		String url = "http://www.kickern-hamburg.de/liga-tool/mannschaftswettbewerbe";
		try {
			return Jsoup.connect(url).data(parameters).timeout(TIMEOUT).post();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}
