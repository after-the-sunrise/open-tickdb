package com.after_sunrise.oss.otdb.je.database.impl;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class TickIteratorAdapterTest {

	private TickIteratorAdapter target;

	@Before
	public void setUp() {
		target = new TickIteratorAdapter();
	}

	@Test
	public void testFirst() throws IOException {
		target.first();
	}

	@Test
	public void testPrevious() throws IOException {
		target.previous();
	}

	@Test
	public void testPreviousLong() throws IOException {
		target.previous(1);
	}

	@Test
	public void testNext() throws IOException {
		target.next();
	}

	@Test
	public void testNextLong() throws IOException {
		target.next(1);
	}

	@Test
	public void testLast() throws IOException {
		target.last();
	}

	@Test
	public void testClose() throws IOException {
		target.close();
	}

}
