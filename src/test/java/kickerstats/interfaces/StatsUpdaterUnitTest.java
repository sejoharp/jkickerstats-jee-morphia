package kickerstats.interfaces;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import kickerstats.types.Game;
import kickerstats.types.Match;
import kickerstats.usecases.GameServiceInterface;
import kickerstats.usecases.MatchServiceInterface;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class StatsUpdaterUnitTest {

	@Mock
	private PageParser pageParserMock;
	@Mock
	private PageDownloader pageDownloaderMock;
	@Mock
	private MatchServiceInterface matchServiceMock;
	@Mock
	private GameServiceInterface gameServiceMock;
	@InjectMocks
	private StatsUpdater statsUpdater;
	@Captor
	private ArgumentCaptor<ArrayList<Game>> captor;

	@Before
	public void setup() {
		when(pageParserMock.findGames(any(Document.class))).thenReturn(
				Lists.newArrayList(new Game()));
		when(pageParserMock.findMatches(any(Document.class))).thenReturn(
				Lists.newArrayList(new MatchWithLink()));
		when(pageParserMock.findSeasonIDs(any(Document.class))).thenReturn(
				Lists.newArrayList(7));
		when(pageParserMock.findLigaLinks(any(Document.class))).thenReturn(
				Lists.newArrayList(""));
		when(pageParserMock.findMatchLinks(any(Document.class))).thenReturn(
				Lists.newArrayList(""));
	}

	@Test
	public void returnsTheCurrentSeasonId() {
		assertThat(statsUpdater.getCurrentSeasonId(Lists.newArrayList(7, 5, 2,
				4, 1)), is(7));

		assertThat(statsUpdater.getCurrentSeasonId(Lists.newArrayList(4, 5, 2,
				7, 1)), is(7));
	}

	@Test
	public void downloadsAllGamesAndMatchesWhenDBisEmpty() {
		when(matchServiceMock.noDataAvailable()).thenReturn(true);

		statsUpdater.updateStatistikData();

		verify(matchServiceMock, times(0)).isNewMatch(any(Match.class));
	}

	@Test
	public void downloadsNewGamesAndMatchesWhenDBisFilled() {
		when(matchServiceMock.noDataAvailable()).thenReturn(false);
		when(matchServiceMock.isNewMatch(any(Match.class))).thenReturn(true);

		statsUpdater.updateStatistikData();

		verify(matchServiceMock, times(1)).isNewMatch(any(Match.class));
	}

	@Test
	public void savesOneGame() {
		when(matchServiceMock.noDataAvailable()).thenReturn(true);

		statsUpdater.updateStatistikData();

		verify(gameServiceMock).saveGames(captor.capture());
		assertThat(captor.getValue().size(), is(1));
	}
	
	@Test
	public void savesOneNewGame() {
		when(matchServiceMock.noDataAvailable()).thenReturn(false);
		when(matchServiceMock.isNewMatch(any(Match.class))).thenReturn(true);
		
		statsUpdater.updateStatistikData();

		verify(gameServiceMock).saveGames(captor.capture());
		assertThat(captor.getValue().size(), is(1));
	}
}
