package org.harpeng.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.*;
import static org.hamcrest.core.IsNull.notNullValue;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.harpeng.WeldJUnit4Runner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4Runner.class)
public class SeasonParserTest {

	@Inject
	private SeasonParser parser;

	@Inject
	BeanManager beanManager;

	@Before
	public void checkPreconditions() {
		assertThat(parser, notNullValue());
	}

	public static final String RECOURCES_DIRECTORY = System.getProperty("user.dir")
			+ "/src/test/resources/";

	@Test
	public void returnsAllSeasons() {
		List<Integer> expectedSeasonsIDs = Arrays.asList(7, 4, 3, 2, 1);

		List<Integer> seasonsIDs = parser.getSeasonIDs(new File(RECOURCES_DIRECTORY
				+ "uebersicht.html"));
		assertThat(seasonsIDs, equalTo(expectedSeasonsIDs));
	}
}
