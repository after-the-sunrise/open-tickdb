package com.after_sunrise.oss.otdb.je.database.impl;

import static org.junit.Assert.assertEquals;
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

import com.sleepycat.je.Environment;

/**
 * @author takanori.takase
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/open-tickdb-context-test.xml" })
public class CodeDatabaseImplTest {

	@Autowired
	private Environment environment;

	@Autowired
	private CodeDatabaseImpl target;

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

		Long id = target.persist(null, "foo");

		assertEquals(id, target.search(null, "foo"));
		assertEquals("foo", target.search(null, id));

		assertNull(target.search(null, "bar"));
		assertNull(target.search(null, id + 1L));

	}

	@DirtiesContext
	@Test(expected = IOException.class)
	public void testPersist_Duplicate() throws IOException {
		target.persist(null, "foo");
		target.persist(null, "foo");
	}

	@DirtiesContext
	@Test(expected = IOException.class)
	public void testPersist_Null() throws IOException {
		target.persist(null, null);
	}

}
