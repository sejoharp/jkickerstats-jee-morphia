package org.harpeng.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.harpeng.WeldJUnit4Runner;
import org.harpeng.parser.SeasonParser;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4Runner.class)
public class SeasonParserTest {

	@Inject
	private SeasonParser parser;

	@Inject
	BeanManager beanManager;

	@Test
	public void returnsAllSeasons() {
		assertThat(beanManager, notNullValue());
		assertThat(parser, notNullValue());
	}
}
