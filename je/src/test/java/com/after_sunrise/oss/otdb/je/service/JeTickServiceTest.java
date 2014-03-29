package com.after_sunrise.oss.otdb.je.service;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.after_sunrise.oss.otdb.api.service.TickIterator;

/**
 * @author takanori.takase
 */
public class JeTickServiceTest {

	private JeTickService target;

	@Before
	public void setUp() throws Exception {

		target = new JeTickService();

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
	public void testFind() throws IOException {
		assertTrue(target.find("foo", null, null).isEmpty());
	}

	@Test
	public void testFind_WithFields() throws IOException {
		assertTrue(target.find("foo", null, null, null).isEmpty());
	}

	@Test
	public void testIterate() throws IOException {

		TickIterator itr = target.iterate("foo", null, null);

		itr.close();

	}

	@Test
	public void testIterate_WithFields() throws IOException {

		TickIterator itr = target.iterate("foo", null, null, null);

		itr.close();

	}

}
