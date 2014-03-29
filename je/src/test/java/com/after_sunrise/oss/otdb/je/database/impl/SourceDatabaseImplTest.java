package com.after_sunrise.oss.otdb.je.database.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.after_sunrise.oss.otdb.je.entity.TickSource;
import com.sleepycat.je.Environment;

/**
 * @author takanori.takase
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/open-tickdb-context-test.xml" })
public class SourceDatabaseImplTest {

	@Autowired
	private Environment environment;

	@Autowired
	private SourceDatabaseImpl target;

	@Before
	public void setUp() throws Exception {
		target.initialize(environment, null);
	}

	@After
	public void tearDown() throws Exception {

		target.close();

		environment.close();

	}

	@DirtiesContext
	@Test
	public void testGenerateSequenceId() throws IOException {

		Set<Long> ids = new HashSet<>();

		for (int i = 0; i < 10; i++) {

			Long id = target.generateSequenceId(null);

			ids.add(id);

		}

		assertEquals(10, ids.size());

	}

	@DirtiesContext
	@Test
	public void testSearch() throws IOException {

		assertNull(target.search(null, null));
		assertNull(target.search(null, "foo"));

		TickSource source = target.insert(null, "foo");

		assertEquals(source, target.search(null, "foo"));
		assertEquals("foo", target.search(null, source.getId()));

		assertNull(target.search(null, "bar"));
		assertNull(target.search(null, source.getId() + 1L));

	}

	@DirtiesContext
	@Test(expected = IOException.class)
	public void testInsert_Duplicate() throws IOException {
		target.insert(null, "foo");
		target.insert(null, "foo");
	}

	@DirtiesContext
	@Test(expected = IOException.class)
	public void testInsert_Null() throws IOException {
		target.insert(null, null);
	}

	@DirtiesContext
	@Test
	public void testDelete() throws IOException {

		assertNotNull(target.insert(null, "foo"));

		assertNotNull(target.delete(null, "foo"));

		assertNull(target.delete(null, "foo"));

		assertNull(target.delete(null, null));

	}

}
