package com.after_sunrise.oss.otdb.je.loader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.after_sunrise.oss.otdb.api.loader.LoadableTick;
import com.after_sunrise.oss.otdb.api.loader.LoadableTickIterator;

/**
 * @author takanori.takase
 */
public class JeTickLoaderTest {

	private JeTickLoader target;

	@Before
	public void setUp() throws Exception {

		target = new JeTickLoader();

		target.setConfig("open-tickdb-context-test.xml");

		target.initialize();

	}

	@After
	public void tearDown() throws Exception {
		IOUtils.closeQuietly(target);
	}

	@Test(expected = IllegalStateException.class)
	public void testSetConfig_AlreadyOpened() {
		target.setConfig("open-tickdb-context-test.xml");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetConfig_UnknownConfig() {
		target.setConfig("foo");
	}

	@Test(expected = IOException.class)
	public void testInitialize() throws IOException {
		target.initialize();
	}

	@Test(expected = IOException.class)
	public void testClose() throws IOException {

		IOUtils.closeQuietly(target);

		target.close();

	}

	@Test(expected = IOException.class)
	public void testGetDelegate() throws IOException {

		IOUtils.closeQuietly(target);

		target.getDelegate();

	}

	@Test
	public void testList() throws IOException {
		assertTrue(target.list().isEmpty());
	}

	@Test
	public void testFind() throws IOException {
		assertNull(target.find("foo"));
	}

	@Test
	public void testDelete() throws IOException {
		assertEquals(null, target.delete("foo"));
	}

	@Test
	public void testLoad() throws IOException {

		Map<Integer, BigDecimal> decimals = Collections.emptyMap();
		Map<Integer, String> strings = Collections.emptyMap();

		final int loop = 3;

		final long count = 10;

		for (int i = 1; i <= loop; i++) {

			final List<LoadableTick> ticks = new ArrayList<>();

			for (long j = 1; j <= count; j++) {
				LoadableTick tick = mock(LoadableTick.class);
				when(tick.getCode()).thenReturn("foo");
				when(tick.getTimestamp()).thenReturn(j);
				when(tick.getDecimals()).thenReturn(decimals);
				when(tick.getStrings()).thenReturn(strings);
				ticks.add(tick);
			}

			LoadableTickIterator itr = new LoadableTickIterator() {

				private Iterator<LoadableTick> itr;

				@Override
				public void initialize() throws IOException {
					itr = ticks.iterator();
				}

				@Override
				public void close() throws IOException {
					ticks.clear();
				}

				@Override
				public boolean hasNext() throws IOException {
					return itr.hasNext();
				}

				@Override
				public LoadableTick getNext() throws IOException {
					return itr.next();
				}

			};

			assertEquals(count, target.load("bar" + i, itr));

			assertTrue(ticks.isEmpty());

		}

		for (int i = 1; i <= loop; i++) {
			assertEquals(Long.valueOf(count), target.find("bar" + i));
		}

		assertNotNull(target.find("bar1"));
		assertNotNull(target.find("bar2"));
		assertNotNull(target.find("bar3"));

	}
}
