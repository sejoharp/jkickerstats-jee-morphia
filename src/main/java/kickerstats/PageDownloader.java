package kickerstats;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PageDownloader {
	public Document downloadPage(String url) {
		try {
			return Jsoup.connect(url).get();
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
			return Jsoup.connect(url).data(parameters).post();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}
