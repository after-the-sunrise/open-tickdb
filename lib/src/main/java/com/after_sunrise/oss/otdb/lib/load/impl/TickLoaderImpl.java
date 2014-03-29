package com.after_sunrise.oss.otdb.lib.load.impl;

import static com.after_sunrise.commons.log.object.Logs.logDebug;
import static com.after_sunrise.commons.log.object.Logs.logErrors;
import static com.after_sunrise.commons.log.object.Logs.logInfo;
import static com.after_sunrise.commons.log.object.Logs.logTrace;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.after_sunrise.oss.otdb.api.loader.LoadableTickIterator;
import com.after_sunrise.oss.otdb.api.loader.LoadableTickLoader;
import com.after_sunrise.oss.otdb.lib.load.TickLoader;
import com.after_sunrise.oss.otdb.lib.load.TickProvider;
import com.google.common.annotations.VisibleForTesting;

/**
 * @author takanori.takase
 */
public class TickLoaderImpl implements TickLoader {

	private final Log log = LogFactory.getLog(getClass());

	private Collection<TickProvider> providers;

	private Collection<LoadableTickLoader> loaders;

	public void setProviders(Collection<TickProvider> providers) {
		this.providers = providers;
	}

	public void setLoader(Collection<LoadableTickLoader> loaders) {
		this.loaders = loaders;
	}

	@Override
	public void load() {

		logInfo(log, "Loading loaders [%s]", loaders.size());

		for (LoadableTickLoader loader : loaders) {

			logInfo(log, "Loading providers [%s]", providers.size());

			for (TickProvider p : providers) {

				try {

					logInfo(log, "Processing provider [%s]", p);

					long total = process(loader, p);

					logInfo(log, "Processed provider [%s] [%s]", p, total);

				} catch (Exception e) {

					logErrors(log, "Failed to process provider [%s}", e, p);

				}

			}

			logInfo(log, "Loaded providers [%s]", providers.size());

		}

		logInfo(log, "Loading loaders [%s]", loaders.size());

	}

	@VisibleForTesting
	long process(LoadableTickLoader loader, TickProvider provider)
			throws IOException {

		List<String> sources = provider.listSources();

		logDebug(log, "Listed sources [%s]", sources.size());

		long total = 0;

		for (String source : sources) {

			Long id = loader.find(source);

			if (id != null) {

				logTrace(log, "Skipping source [%s]", source);

				continue;

			}

			logInfo(log, "Loading source [%s]", source);

			try (LoadableTickIterator itr = provider.iterator(source)) {

				long count = loader.load(source, itr);

				logInfo(log, "Loaded source [%s] [%,3d]", source, count);

				total += count;

			} catch (IOException e) {

				logErrors(log, "Failed to load source [%s]", e, source);

			}

		}

		return total;

	}

	public static void main(String[] args) {

		String config = System.getProperty(CONFIG_KEY, CONFIG_VAL);

		ApplicationContext c = new ClassPathXmlApplicationContext(config);

		try {

			TickLoader loader = c.getBean(TickLoader.class);

			loader.load();

		} finally {

			((ConfigurableApplicationContext) c).close();

		}

	}

}
