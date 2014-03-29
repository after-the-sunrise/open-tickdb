package com.after_sunrise.oss.otdb.je.database.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.after_sunrise.oss.otdb.je.database.ForwardIterator;
import com.after_sunrise.oss.otdb.je.database.TickDatabaseIterator;
import com.after_sunrise.oss.otdb.je.entity.TickEntry;
import com.after_sunrise.oss.otdb.je.entity.TickKey;
import com.after_sunrise.oss.otdb.je.entity.TickValue;
import com.sleepycat.je.Environment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/open-tickdb-context-test.xml" })
public class TickDatabaseImplTest {

	@Autowired
	private Environment environment;

	@Autowired
	private TickDatabaseImpl target;

	private TickValue value;

	@Before
	public void setUp() throws Exception {

		target.initialize(environment, null);

		Map<Integer, BigDecimal> d = Collections.emptyMap();

		Map<Integer, String> s = Collections.emptyMap();

		value = new TickValue((long) Math.random(), d, s);

	}

	@After
	public void tearDown() throws Exception {

		target.close();

		environment.close();

	}

	@DirtiesContext
	@Test
	public void testCount() throws IOException {

		assertEquals(0, target.count());

		int count = 20;

		Map<Integer, BigDecimal> decimals = Collections.emptyMap();

		Map<Integer, String> strings = Collections.emptyMap();

		for (int i = 1; i <= count; i++) {

			TickKey key = new TickKey(i, i, i);

			TickValue value = new TickValue(i, decimals, strings);

			TickEntry tick = new TickEntry(key, value);

			target.persist(null, tick);

		}

		assertEquals(count, target.count());

	}

	@DirtiesContext
	@Test
	public void testFind() throws IOException {

		final long code = 8L;

		assertEquals(0, target.find(null, code, 3L, 4L).size());

		TickKey key1 = new TickKey(code, 1, target.generateSequenceId(null));
		TickKey key2 = new TickKey(code, 2, target.generateSequenceId(null));
		TickKey key3 = new TickKey(code, 3, target.generateSequenceId(null));

		target.persist(null, new TickEntry(key2, value));
		target.persist(null, new TickEntry(key1, value));
		target.persist(null, new TickEntry(key3, value));

		// All in range
		assertEquals(3, target.find(null, code, 1L, 3L).size());
		assertEquals(3, target.find(null, code, 0L, 3L).size());
		assertEquals(3, target.find(null, code, 0L, 4L).size());
		assertEquals(3, target.find(null, code, 1L, 3L).size());
		assertEquals(3, target.find(null, code, 1L, 4L).size());

		// Partially in range
		assertEquals(1, target.find(null, code, 0L, 1L).size());
		assertEquals(2, target.find(null, code, 1L, 2L).size());
		assertEquals(2, target.find(null, code, 2L, 3L).size());
		assertEquals(1, target.find(null, code, 3L, 4L).size());

		// Start = End
		assertEquals(0, target.find(null, code, 0L, 0L).size());
		assertEquals(1, target.find(null, code, 1L, 1L).size());
		assertEquals(1, target.find(null, code, 2L, 2L).size());
		assertEquals(1, target.find(null, code, 3L, 3L).size());
		assertEquals(0, target.find(null, code, 4L, 4L).size());

		// Null start
		assertEquals(0, target.find(null, code, null, 0L).size());
		assertEquals(1, target.find(null, code, null, 1L).size());
		assertEquals(2, target.find(null, code, null, 2L).size());
		assertEquals(3, target.find(null, code, null, 3L).size());
		assertEquals(3, target.find(null, code, null, 4L).size());

		// Null end
		assertEquals(3, target.find(null, code, 0L, null).size());
		assertEquals(3, target.find(null, code, 1L, null).size());
		assertEquals(2, target.find(null, code, 2L, null).size());
		assertEquals(1, target.find(null, code, 3L, null).size());
		assertEquals(0, target.find(null, code, 4L, null).size());

		// Both null
		assertEquals(3, target.find(null, code, null, null).size());

		// Different symbol
		assertEquals(0, target.find(null, 7L, null, null).size());

	}

	@DirtiesContext
	@Test
	public void testIterator() throws IOException {

		final long code = 8L;

		assertEquals(0, target.find(null, code, 3L, 4L).size());

		TickKey key1 = new TickKey(code, 1, target.generateSequenceId(null));
		TickKey key2 = new TickKey(code, 2, target.generateSequenceId(null));
		TickKey key3 = new TickKey(code, 3, target.generateSequenceId(null));

		target.persist(null, new TickEntry(key2, value));
		target.persist(null, new TickEntry(key1, value));
		target.persist(null, new TickEntry(key3, value));

		TickDatabaseIterator itr = target.iterator(null, code, null, null);

		try {
			assertEquals(1, itr.next().getKey().getTimestamp());
			assertEquals(2, itr.next().getKey().getTimestamp());
			assertEquals(3, itr.next().getKey().getTimestamp());
			assertNull(null, itr.next());
		} finally {
			itr.close();
		}

	}

	@DirtiesContext
	@Test
	public void testKeyIterator() throws IOException {

		final long code = 8L;

		assertEquals(0, target.find(null, code, 3L, 4L).size());

		TickKey key1 = new TickKey(code, 1, target.generateSequenceId(null));
		TickKey key2 = new TickKey(code, 2, target.generateSequenceId(null));
		TickKey key3 = new TickKey(code, 3, target.generateSequenceId(null));

		target.persist(null, new TickEntry(key2, value));
		target.persist(null, new TickEntry(key1, value));
		target.persist(null, new TickEntry(key3, value));

		ForwardIterator<TickKey> itr = target.keyIterator(null);

		try {
			assertNotNull(itr.next());
			assertNotNull(itr.next());
			assertNotNull(itr.next());
			assertNull(itr.next());
		} finally {
			itr.close();
		}

	}

	@DirtiesContext
	@Test
	public void testEntryIterator() throws IOException {

		final long code = 8L;

		assertEquals(0, target.find(null, code, 3L, 4L).size());

		TickKey key1 = new TickKey(code, 1, target.generateSequenceId(null));
		TickKey key2 = new TickKey(code, 2, target.generateSequenceId(null));
		TickKey key3 = new TickKey(code, 3, target.generateSequenceId(null));

		target.persist(null, new TickEntry(key2, value));
		target.persist(null, new TickEntry(key1, value));
		target.persist(null, new TickEntry(key3, value));

		ForwardIterator<TickEntry> itr = target.entryIterator(null);

		try {
			assertEquals(1, itr.next().getKey().getTimestamp());
			assertEquals(2, itr.next().getKey().getTimestamp());
			assertEquals(3, itr.next().getKey().getTimestamp());
			assertNull(null, itr.next());
		} finally {
			itr.close();
		}

	}

	@DirtiesContext
	@Test
	public void testGenerateSequence() throws IOException {
		long first = target.generateSequenceId(null);
		assertEquals(++first, target.generateSequenceId(null));
		assertEquals(++first, target.generateSequenceId(null));
		assertEquals(++first, target.generateSequenceId(null));
	}

	@DirtiesContext
	@Test
	public void testGenerateSequence_WithDelta() throws IOException {

		int delta = 20;

		long first = target.generateSequenceId(null, delta);

		for (int i = 1; i <= 10; i++) {

			long sequence = target.generateSequenceId(null, delta);

			assertEquals(first + i * delta, sequence);

		}

	}

	@DirtiesContext
	@Test
	public void testPersist() throws IOException {

		assertEquals(0, target.count());

		TickEntry tick = new TickEntry(new TickKey(1, 2, 4), value);

		target.persist(null, tick);

		assertEquals(1, target.count());

	}

	@DirtiesContext
	@Test(expected = IOException.class)
	public void testPersist_Duplicate() throws IOException {

		assertEquals(0, target.count());

		TickEntry tick = new TickEntry(new TickKey(1, 2, 4), value);

		target.persist(null, tick);

		target.persist(null, tick);

	}

	@DirtiesContext
	@Test
	public void testPersist_Duplicate_Overwrite() throws IOException {

		assertEquals(0, target.count());

		TickEntry tick = new TickEntry(new TickKey(1, 2, 4), value);

		target.persist(null, tick);

		target.persist(null, tick, true);

		assertEquals(1, target.count());

	}

	@DirtiesContext
	@Test
	public void testDelete() throws IOException {

		TickKey key = new TickKey(1, 2, 4);

		TickEntry tick = new TickEntry(key, value);

		target.persist(null, tick);

		assertEquals(1, target.count());

		assertTrue(target.delete(null, key));

		assertEquals(0, target.count());

		assertFalse(target.delete(null, key));

		assertFalse(target.delete(null, null));

	}

}
