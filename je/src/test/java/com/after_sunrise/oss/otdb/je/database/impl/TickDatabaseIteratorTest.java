package com.after_sunrise.oss.otdb.je.database.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.after_sunrise.oss.otdb.je.database.TickDatabase;
import com.after_sunrise.oss.otdb.je.database.TickDatabaseIterator;
import com.after_sunrise.oss.otdb.je.entity.TickEntry;
import com.after_sunrise.oss.otdb.je.entity.TickKey;
import com.after_sunrise.oss.otdb.je.entity.TickValue;
import com.sleepycat.je.Environment;

/**
 * @author takanori.takase
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/open-tickdb-context-test.xml" })
public class TickDatabaseIteratorTest {

	@Autowired
	private Environment environment;

	@Autowired
	private TickDatabase database;

	private TickDatabaseIterator target;

	@Before
	public void setUp() throws Exception {

		database.initialize(environment, null);

		Map<Integer, BigDecimal> d = Collections.emptyMap();

		Map<Integer, String> s = Collections.emptyMap();

		for (int i = 4; i <= 8; i++) {

			TickKey key = new TickKey(2, i, database.generateSequenceId(null));

			TickValue value = new TickValue(12, d, s);

			TickEntry tick = new TickEntry(key, value);

			database.persist(null, tick);

		}

	}

	@After
	public void tearDown() throws Exception {

		IOUtils.closeQuietly(target);

		IOUtils.closeQuietly(database);

		environment.close();

	}

	@Test
	public void testFirst() throws IOException {

		target = database.iterator(null, 2L, 4L, 8L);

		assertEquals(4, target.first().getKey().getTimestamp());
		assertEquals(4, target.first().getKey().getTimestamp());

		assertEquals(5, target.next().getKey().getTimestamp());
		assertEquals(4, target.first().getKey().getTimestamp());

		assertEquals(5, target.next().getKey().getTimestamp());
		assertEquals(6, target.next().getKey().getTimestamp());
		assertEquals(4, target.first().getKey().getTimestamp());

		assertEquals(5, target.next().getKey().getTimestamp());
		assertEquals(6, target.next().getKey().getTimestamp());
		assertEquals(7, target.next().getKey().getTimestamp());
		assertEquals(4, target.first().getKey().getTimestamp());

		assertEquals(5, target.next().getKey().getTimestamp());
		assertEquals(6, target.next().getKey().getTimestamp());
		assertEquals(7, target.next().getKey().getTimestamp());
		assertEquals(8, target.next().getKey().getTimestamp());
		assertEquals(4, target.first().getKey().getTimestamp());

		assertEquals(5, target.next().getKey().getTimestamp());
		assertEquals(6, target.next().getKey().getTimestamp());
		assertEquals(7, target.next().getKey().getTimestamp());
		assertEquals(8, target.next().getKey().getTimestamp());
		assertNull(null, target.next());
		assertEquals(4, target.first().getKey().getTimestamp());

	}

	@Test
	public void testPrevious() throws IOException {

		target = database.iterator(null, 2L, 4L, 8L);

		assertEquals(8, target.previous().getKey().getTimestamp());
		assertEquals(7, target.previous().getKey().getTimestamp());
		assertEquals(6, target.previous().getKey().getTimestamp());
		assertEquals(5, target.previous().getKey().getTimestamp());
		assertEquals(4, target.previous().getKey().getTimestamp());
		assertNull(null, target.previous());
		assertNull(null, target.previous());

	}

	@Test
	public void testPrevious_WithTimestamp() throws IOException {

		target = database.iterator(null, 2L, 4L, 8L);

		assertNull(null, target.previous(3));
		assertEquals(4, target.previous(4).getKey().getTimestamp());
		assertEquals(5, target.previous(5).getKey().getTimestamp());
		assertEquals(6, target.previous(6).getKey().getTimestamp());
		assertEquals(7, target.previous(7).getKey().getTimestamp());
		assertEquals(8, target.previous(8).getKey().getTimestamp());
		assertEquals(8, target.previous(9).getKey().getTimestamp());

		assertEquals(8, target.previous(9).getKey().getTimestamp());
		assertEquals(8, target.previous(8).getKey().getTimestamp());
		assertEquals(7, target.previous(7).getKey().getTimestamp());
		assertEquals(6, target.previous(6).getKey().getTimestamp());
		assertEquals(5, target.previous(5).getKey().getTimestamp());
		assertEquals(4, target.previous(4).getKey().getTimestamp());
		assertNull(null, target.previous(3));

	}

	@Test
	public void testNext() throws IOException {

		target = database.iterator(null, 2L, 4L, 8L);

		assertEquals(4, target.next().getKey().getTimestamp());
		assertEquals(5, target.next().getKey().getTimestamp());
		assertEquals(6, target.next().getKey().getTimestamp());
		assertEquals(7, target.next().getKey().getTimestamp());
		assertEquals(8, target.next().getKey().getTimestamp());
		assertNull(null, target.next());
		assertNull(null, target.next());

	}

	@Test
	public void testNext_WithTimestamp() throws IOException {

		target = database.iterator(null, 2L, 4L, 8L);

		assertEquals(4, target.next(3).getKey().getTimestamp());
		assertEquals(4, target.next(4).getKey().getTimestamp());
		assertEquals(5, target.next(5).getKey().getTimestamp());
		assertEquals(6, target.next(6).getKey().getTimestamp());
		assertEquals(7, target.next(7).getKey().getTimestamp());
		assertEquals(8, target.next(8).getKey().getTimestamp());
		assertNull(null, target.next(9));

		assertNull(null, target.next(9));
		assertEquals(8, target.next(8).getKey().getTimestamp());
		assertEquals(7, target.next(7).getKey().getTimestamp());
		assertEquals(6, target.next(6).getKey().getTimestamp());
		assertEquals(5, target.next(5).getKey().getTimestamp());
		assertEquals(4, target.next(4).getKey().getTimestamp());
		assertEquals(4, target.next(3).getKey().getTimestamp());

	}

	@Test
	public void testLast() throws IOException {

		target = database.iterator(null, 2L, 4L, 8L);

		assertEquals(8, target.last().getKey().getTimestamp());
		assertEquals(8, target.last().getKey().getTimestamp());

		assertEquals(7, target.previous().getKey().getTimestamp());
		assertEquals(8, target.last().getKey().getTimestamp());

		assertEquals(7, target.previous().getKey().getTimestamp());
		assertEquals(6, target.previous().getKey().getTimestamp());
		assertEquals(8, target.last().getKey().getTimestamp());

		assertEquals(7, target.previous().getKey().getTimestamp());
		assertEquals(6, target.previous().getKey().getTimestamp());
		assertEquals(5, target.previous().getKey().getTimestamp());
		assertEquals(8, target.last().getKey().getTimestamp());

		assertEquals(7, target.previous().getKey().getTimestamp());
		assertEquals(6, target.previous().getKey().getTimestamp());
		assertEquals(5, target.previous().getKey().getTimestamp());
		assertEquals(4, target.previous().getKey().getTimestamp());
		assertEquals(8, target.last().getKey().getTimestamp());

		assertEquals(7, target.previous().getKey().getTimestamp());
		assertEquals(6, target.previous().getKey().getTimestamp());
		assertEquals(5, target.previous().getKey().getTimestamp());
		assertEquals(4, target.previous().getKey().getTimestamp());
		assertNull(null, target.previous());
		assertEquals(8, target.last().getKey().getTimestamp());

	}

	@Test
	public void testScenario() throws IOException {

		target = database.iterator(null, 2L, 4L, 8L);

		// First => End
		assertEquals(4, target.first().getKey().getTimestamp());
		assertEquals(5, target.next().getKey().getTimestamp());
		assertEquals(6, target.next().getKey().getTimestamp());
		assertEquals(7, target.next().getKey().getTimestamp());
		assertEquals(8, target.next().getKey().getTimestamp());
		assertNull(null, target.next());
		assertNull(null, target.next());

		// End => Begin
		assertEquals(7, target.previous().getKey().getTimestamp());
		assertEquals(6, target.previous().getKey().getTimestamp());
		assertEquals(5, target.previous().getKey().getTimestamp());
		assertEquals(4, target.previous().getKey().getTimestamp());
		assertNull(null, target.previous());
		assertNull(null, target.previous());

		// Last => Begin
		assertEquals(8, target.last().getKey().getTimestamp());
		assertEquals(7, target.previous().getKey().getTimestamp());
		assertEquals(6, target.previous().getKey().getTimestamp());
		assertEquals(5, target.previous().getKey().getTimestamp());
		assertEquals(4, target.previous().getKey().getTimestamp());
		assertNull(null, target.previous());
		assertNull(null, target.previous());

		// Begin => End
		assertEquals(5, target.next().getKey().getTimestamp());
		assertEquals(6, target.next().getKey().getTimestamp());
		assertEquals(7, target.next().getKey().getTimestamp());
		assertEquals(8, target.next().getKey().getTimestamp());
		assertNull(null, target.next());
		assertNull(null, target.next());

		// Back and forth
		assertEquals(7, target.previous().getKey().getTimestamp());
		assertEquals(6, target.previous().getKey().getTimestamp());
		assertEquals(7, target.next().getKey().getTimestamp());
		assertEquals(6, target.previous().getKey().getTimestamp());
		assertEquals(5, target.previous().getKey().getTimestamp());
		assertEquals(6, target.next().getKey().getTimestamp());

	}

}
