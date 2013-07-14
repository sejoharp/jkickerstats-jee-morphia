package kickerstats.interfaces;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import kickerstats.interfaces.PageDownloader;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

public class PageDownloaderTest {
	private PageDownloader pageDownloader;

	@Before
	public void setup() {
		pageDownloader = new PageDownloader();
	}

	@Test
	public void returnsAComplete() {
		Document document = pageDownloader
				.downloadPage("http://www.kickern-hamburg.de/liga-tool/mannschaftswettbewerbe?task=begegnung_spielplan&veranstaltungid=8&id=2");
		assertThat(document.html(), containsString("BSC Hamburg 07"));
	}

	@Test
	public void returnsACompleteSeason() {
		Document document = pageDownloader.downloadSeason(7);
		assertThat(document.html(), containsString("Begegnungen …"));
	}
}
