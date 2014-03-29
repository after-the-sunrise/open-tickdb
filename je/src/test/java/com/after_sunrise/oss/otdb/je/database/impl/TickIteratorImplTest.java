package com.after_sunrise.oss.otdb.je.database.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.after_sunrise.oss.otdb.je.database.TickDatabaseIterator;
import com.after_sunrise.oss.otdb.je.entity.TickEntry;

/**
 * @author takanori.takase
 */
public class TickIteratorImplTest {

	private TickIteratorImpl target;

	private TickDatabaseIterator delegate;

	private TickEntry entry;

	@Before
	public void setUp() throws Exception {

		delegate = mock(TickDatabaseIterator.class);

		entry = mock(TickEntry.class);

		target = new TickIteratorImpl("foo", delegate);

	}

	@Test(expected = NullPointerException.class)
	public void testTickIteratorImpl_NullCode() {
		target = new TickIteratorImpl(null, delegate);
	}

	@Test(expected = NullPointerException.class)
	public void testTickIteratorImpl_NullDelegate() {
		target = new TickIteratorImpl("foo", null);
	}

	@Test
	public void testFirst() throws IOException {

		assertNull(target.first());

		when(delegate.first()).thenReturn(entry);

		assertNotNull(target.first());

	}

	@Test
	public void testPrevious() throws IOException {

		assertNull(target.previous());

		when(delegate.previous()).thenReturn(entry);

		assertNotNull(target.previous());

	}

	@Test
	public void testPreviousLong() throws IOException {

		assertNull(target.previous(1));

		when(delegate.previous(1)).thenReturn(entry);

		assertNotNull(target.previous(1));

	}

	@Test
	public void testNext() throws IOException {

		assertNull(target.next());

		when(delegate.next()).thenReturn(entry);

		assertNotNull(target.next());

	}

	@Test
	public void testNextLong() throws IOException {

		assertNull(target.next(1));

		when(delegate.next(1)).thenReturn(entry);

		assertNotNull(target.next(1));

	}

	@Test
	public void testLast() throws IOException {

		assertNull(target.last());

		when(delegate.last()).thenReturn(entry);

		assertNotNull(target.last());

	}

	@Test
	public void testClose() throws IOException {

		target.close();

		verify(delegate).close();

	}

}
