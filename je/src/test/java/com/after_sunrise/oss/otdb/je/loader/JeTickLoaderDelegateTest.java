package com.after_sunrise.oss.otdb.je.loader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.after_sunrise.oss.otdb.api.loader.LoadableTick;
import com.after_sunrise.oss.otdb.api.loader.LoadableTickIterator;
import com.after_sunrise.oss.otdb.je.entity.TickSource;
import com.sleepycat.je.Transaction;

/**
 * @author takanori.takase
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/open-tickdb-context-test.xml" })
public class JeTickLoaderDelegateTest {

	@Autowired
	private JeTickLoaderDelegate target;

	private final Transaction tx = null;

	@Before
	public void setUp() throws Exception {

		target.setTx(tx);

		target.initialize();

	}

	@After
	public void tearDown() throws Exception {

		IOUtils.closeQuietly(target);

	}

	@Test
	public void testList() throws IOException {

		// Load ticks
		testLoad();

		assertEquals(1, target.list().size());

	}

	@Test
	public void testList_None() throws IOException {

		assertTrue(target.list().isEmpty());
	}

	@Test
	public void testFind() throws IOException {

		// Load ticks
		testLoad();

		assertEquals(Long.valueOf(4L), target.find("foo"));

	}

	@Test
	public void testFind_None() throws IOException {

		assertNull(target.find("foo"));

		assertNull(target.find(null));

	}

	@Test
	public void testDelete() throws IOException {

		// Load ticks
		testLoad();

		assertEquals(Long.valueOf(4L), target.delete("foo"));

	}

	@Test
	public void testDelete_None() throws IOException {

		assertNull(target.delete("foo"));

		assertNull(target.delete(null));

	}

	@Test
	public void testLoad() throws IOException {

		LoadableTick tick1 = mock(LoadableTick.class);
		LoadableTick tick2 = mock(LoadableTick.class);
		LoadableTick tick3 = mock(LoadableTick.class);
		LoadableTick tick4 = mock(LoadableTick.class);

		when(tick1.getCode()).thenReturn("a");
		when(tick2.getCode()).thenReturn("a");
		when(tick3.getCode()).thenReturn("b");
		when(tick4.getCode()).thenReturn("b");

		when(tick1.getTimestamp()).thenReturn(1L);
		when(tick2.getTimestamp()).thenReturn(2L);
		when(tick3.getTimestamp()).thenReturn(3L);
		when(tick4.getTimestamp()).thenReturn(4L);

		final List<LoadableTick> ticks = new ArrayList<>();
		ticks.add(tick1);
		ticks.add(tick2);
		ticks.add(tick3);
		ticks.add(tick4);

		final AtomicBoolean closed = new AtomicBoolean(false);

		LoadableTickIterator iterator = new LoadableTickIterator() {

			private Iterator<LoadableTick> itr;

			@Override
			public void initialize() throws IOException {
				itr = ticks.iterator();
			}

			@Override
			public void close() throws IOException {
				closed.set(true);
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

		long count = target.load("foo", iterator);
		assertEquals(ticks.size(), count);
		assertTrue(closed.get());

		TickSource source = target.getSourceDatabase().search(tx, "foo");
		assertEquals(0L, source.getId());
		assertEquals(4L, source.getCount().longValue());
		assertEquals(1L, source.getStartTime().longValue());
		assertEquals(4L, source.getEndTime().longValue());
		assertNull(source.getDeleted());

		assertEquals(Long.valueOf(0L), target.getCodeDatabase().search(tx, "a"));
		assertEquals(Long.valueOf(1L), target.getCodeDatabase().search(tx, "b"));

		assertEquals(4L, target.getTickDatabase().count());

	}

}
