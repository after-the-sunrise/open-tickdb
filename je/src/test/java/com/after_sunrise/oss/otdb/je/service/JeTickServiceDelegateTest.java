package com.after_sunrise.oss.otdb.je.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.after_sunrise.oss.otdb.api.service.TickIterator;
import com.after_sunrise.oss.otdb.api.tick.Tick;
import com.after_sunrise.oss.otdb.je.database.CodeDatabase;
import com.after_sunrise.oss.otdb.je.database.TickDatabase;
import com.after_sunrise.oss.otdb.je.database.TickDatabaseIterator;
import com.after_sunrise.oss.otdb.je.entity.TickEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.Transaction;

/**
 * @author takanori.takase
 */
public class JeTickServiceDelegateTest {

	private JeTickServiceDelegate target;

	private Environment environment;

	private CodeDatabase codeDatabase;

	private TickDatabase tickDatabase;

	private Transaction tx;

	@Before
	public void setUp() throws Exception {

		environment = mock(Environment.class);
		codeDatabase = mock(CodeDatabase.class);
		tickDatabase = mock(TickDatabase.class);
		tx = mock(Transaction.class);

		target = new JeTickServiceDelegate();
		target.setEnvironment(environment);
		target.setCodeDatabase(codeDatabase);
		target.setTickDatabase(tickDatabase);
		target.setTx(tx);

	}

	@Test
	public void testInitialize() throws IOException {

		target.initialize();

		verify(codeDatabase).initialize(environment, tx);

		verify(tickDatabase).initialize(environment, tx);

	}

	@Test
	public void testClose() throws IOException {

		target.close();

		verify(codeDatabase).close();

		verify(tickDatabase).close();

	}

	@Test
	public void testFind() throws IOException {

		List<TickEntry> entries = Arrays.asList(mock(TickEntry.class));

		when(codeDatabase.search(tx, "foo")).thenReturn(1L);

		when(tickDatabase.find(tx, 1L, 2L, 4L)).thenReturn(entries);

		List<Tick> ticks = target.find("foo", 2L, 4L);

		assertEquals(1, ticks.size());

	}

	@Test
	public void testFind_UnknownCode() throws IOException {

		List<TickEntry> entries = Arrays.asList(mock(TickEntry.class));

		when(codeDatabase.search(tx, "foo")).thenReturn(null);

		when(tickDatabase.find(tx, 1L, 2L, 4L)).thenReturn(entries);

		List<Tick> ticks = target.find("foo", 2L, 4L);

		assertTrue(ticks.isEmpty());

	}

	@Test
	public void testFind_NoEntries() throws IOException {

		when(codeDatabase.search(tx, "foo")).thenReturn(1L);

		when(tickDatabase.find(tx, 1L, 2L, 4L)).thenReturn(null);

		List<Tick> ticks = target.find("foo", 2L, 4L);

		assertTrue(ticks.isEmpty());

	}

	@Test
	public void testIterate() throws IOException {

		TickDatabaseIterator itr = mock(TickDatabaseIterator.class);

		when(codeDatabase.search(tx, "foo")).thenReturn(1L);

		when(tickDatabase.iterator(tx, 1L, 2l, 4L)).thenReturn(itr);

		TickIterator result = target.iterate("foo", 2L, 4L);

		result.close();

	}

	@Test
	public void testIterate_UnknownCode() throws IOException {

		TickDatabaseIterator itr = mock(TickDatabaseIterator.class);

		when(codeDatabase.search(tx, "foo")).thenReturn(null);

		when(tickDatabase.iterator(tx, 1L, 2l, 4L)).thenReturn(itr);

		TickIterator result = target.iterate("foo", 2L, 4L);

		result.close();

	}

	@Test
	public void testIterate_NullIterator() throws IOException {

		when(codeDatabase.search(tx, "foo")).thenReturn(1L);

		when(tickDatabase.iterator(tx, 1L, 2l, 4L)).thenReturn(null);

		TickIterator result = target.iterate("foo", 2L, 4L);

		result.close();

	}

}
