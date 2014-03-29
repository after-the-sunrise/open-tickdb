package com.after_sunrise.oss.otdb.je;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Iterator;
import java.util.ServiceLoader;

import org.junit.Test;

import com.after_sunrise.oss.otdb.api.loader.LoadableTickLoader;
import com.after_sunrise.oss.otdb.api.service.TickService;
import com.after_sunrise.oss.otdb.je.loader.JeTickLoader;
import com.after_sunrise.oss.otdb.je.service.JeTickService;

/**
 * @author takanori.takase
 */
public class SpiTest {

	@Test
	public void testTickService() throws IOException {

		JeTickService tickService = null;

		// Load services defined in `/META-INF/services/*`
		ServiceLoader<TickService> l = ServiceLoader.load(TickService.class);

		Iterator<TickService> itr = l.iterator();

		while (itr.hasNext()) {

			TickService service = itr.next();

			if (service instanceof JeTickService) {

				tickService = (JeTickService) service;

				break;

			}

		}

		assertNotNull(tickService);

	}

	@Test
	public void testTickLoader() throws IOException {

		JeTickLoader tickLoader = null;

		// Load services defined in `/META-INF/services/*`
		ServiceLoader<LoadableTickLoader> l = ServiceLoader
				.load(LoadableTickLoader.class);

		Iterator<LoadableTickLoader> itr = l.iterator();

		while (itr.hasNext()) {

			LoadableTickLoader loader = itr.next();

			if (loader instanceof JeTickLoader) {

				tickLoader = (JeTickLoader) loader;

				break;

			}

		}

		assertNotNull(tickLoader);

	}

}
