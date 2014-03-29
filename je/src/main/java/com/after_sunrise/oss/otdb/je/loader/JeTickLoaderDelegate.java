package com.after_sunrise.oss.otdb.je.loader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.after_sunrise.commons.log.object.Logs;
import com.after_sunrise.oss.otdb.api.loader.LoadableTick;
import com.after_sunrise.oss.otdb.api.loader.LoadableTickIterator;
import com.after_sunrise.oss.otdb.api.loader.LoadableTickLoader;
import com.after_sunrise.oss.otdb.je.database.CodeDatabase;
import com.after_sunrise.oss.otdb.je.database.ForwardIterator;
import com.after_sunrise.oss.otdb.je.database.SourceDatabase;
import com.after_sunrise.oss.otdb.je.database.TickDatabase;
import com.after_sunrise.oss.otdb.je.entity.TickEntry;
import com.after_sunrise.oss.otdb.je.entity.TickKey;
import com.after_sunrise.oss.otdb.je.entity.TickSource;
import com.after_sunrise.oss.otdb.je.entity.TickValue;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.sleepycat.je.Environment;
import com.sleepycat.je.Transaction;

/**
 * @author takanori.takase
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JeTickLoaderDelegate implements LoadableTickLoader {

	private static final String TEMP = "open-tickdb-context-temp.xml";

	private final Log log = LogFactory.getLog(getClass());

	@Autowired
	private Environment environment;

	@Autowired
	private SourceDatabase sourceDatabase;

	@Autowired
	private CodeDatabase codeDatabase;

	@Autowired
	private TickDatabase tickDatabase;

	// TODO Handle transaction if needed.
	private Transaction tx;

	@VisibleForTesting
	SourceDatabase getSourceDatabase() {
		return sourceDatabase;
	}

	@VisibleForTesting
	CodeDatabase getCodeDatabase() {
		return codeDatabase;
	}

	@VisibleForTesting
	TickDatabase getTickDatabase() {
		return tickDatabase;
	}

	@VisibleForTesting
	void setTx(Transaction tx) {
		this.tx = tx;
	}

	@Override
	public void initialize() throws IOException {

		log.info("Initializing service.");

		sourceDatabase.initialize(environment, tx);

		codeDatabase.initialize(environment, tx);

		tickDatabase.initialize(environment, tx);

		log.info("Initialized service.");

	}

	@Override
	public void close() throws IOException {

		log.info("Closing service.");

		IOUtils.closeQuietly(tickDatabase);

		IOUtils.closeQuietly(codeDatabase);

		IOUtils.closeQuietly(sourceDatabase);

		environment.close();

		log.info("Closed service.");

	}

	@Override
	public List<String> list() throws IOException {

		ForwardIterator<String> itr = sourceDatabase.keyIterator(tx);

		ArrayList<String> list = new ArrayList<>();

		try {

			String key = itr.next();

			while (key != null) {

				list.add(key);

				key = itr.next();

			}

		} finally {
			IOUtils.closeQuietly(itr);
		}

		list.trimToSize();

		return list;

	}

	@Override
	public Long find(String source) throws IOException {

		TickSource tickSource = sourceDatabase.search(tx, source);

		if (tickSource == null) {
			return null;
		}

		return Objects.firstNonNull(tickSource.getCount(), Long.valueOf(0L));

	}

	@Override
	public Long delete(String source) throws IOException {

		TickSource tickSource = sourceDatabase.search(tx, source);

		if (tickSource == null) {
			return null;
		}

		long count = 0;

		long id = tickSource.getId();

		try (ForwardIterator<TickEntry> itr = tickDatabase.entryIterator(tx)) {

			for (TickEntry tick = itr.next(); tick != null; tick = itr.next()) {

				if (id != tick.getValue().getSourceId()) {
					continue;
				}

				tickDatabase.delete(tx, tick.getKey());

				count++;

			}

			if (count > 0L) {
				tickDatabase.sync();
			}

			sourceDatabase.delete(tx, source);

			sourceDatabase.sync();

			int cleaned;

			while ((cleaned = environment.cleanLog()) > 0) {
				Logs.logInfo(log, "Cleaning logs... %s", cleaned);
			}

			environment.compress();

		} catch (IOException e) {

			String msg = "Failed to delete ticks. source[%s] id[%s] deleted[%,3d]";

			log.error(String.format(msg, source, id, count), e);

			throw e;

		}

		return count;

	}

	@Override
	public long load(String source, LoadableTickIterator iterator)
			throws IOException {

		if (iterator == null) {
			throw new IOException("Iterator cannot be null.");
		}

		if (StringUtils.isBlank(source)) {
			throw new IOException("Source cannot be null.");
		}

		try {

			iterator.initialize();

			TickSource tickSource = sourceDatabase.insert(tx, source);

			sourceDatabase.sync();

			persist(tickSource, iterator);

			sourceDatabase.persist(tx, source, tickSource);

			sourceDatabase.sync();

			return tickSource.getCount();

		} finally {
			IOUtils.closeQuietly(iterator);
		}

	}

	@VisibleForTesting
	void persist(TickSource tickSource, final LoadableTickIterator itr)
			throws IOException {

		long count = 0L;

		ApplicationContext context = new ClassPathXmlApplicationContext(TEMP);

		try (JeTickLoaderDelegate temp = context.getBean(getClass())) {

			TickDatabase tempDb = retrieveTempDatabase(temp);

			long min = Long.MAX_VALUE;

			long max = Long.MIN_VALUE;

			while (itr.hasNext()) {

				LoadableTick tick = itr.getNext();

				TickEntry entry = convert(tickSource.getId(), count++, tick);

				tempDb.persist(tx, entry);

				min = Math.min(min, entry.getKey().getTimestamp());

				max = Math.max(max, entry.getKey().getTimestamp());

			}

			tickSource.setStartTime(min);

			tickSource.setEndTime(max);

			tickSource.setCount(count);

			try (ForwardIterator<TickEntry> iterator = tempDb.entryIterator(tx)) {
				persist(count, iterator);
			}

		} finally {
			((ConfigurableApplicationContext) context).close();
		}

	}

	@VisibleForTesting
	TickDatabase retrieveTempDatabase(JeTickLoaderDelegate temp)
			throws IOException {

		Environment env = temp.environment;

		for (String dbName : env.getDatabaseNames()) {

			// Clean garbage from previous import.
			env.truncateDatabase(tx, dbName, false);

		}

		temp.initialize();

		return temp.tickDatabase;

	}

	@VisibleForTesting
	TickEntry convert(long id, long sequence, LoadableTick tick)
			throws IOException {

		if (tick == null) {
			throw new IOException("Tick cannot be null.");
		}

		String code = tick.getCode();
		if (code == null) {
			throw new IOException("Code cannot be null.");
		}

		Long codeId = codeDatabase.search(tx, code);
		if (codeId == null) {
			codeId = codeDatabase.persist(tx, code);
		}

		TickKey k = new TickKey(codeId, tick.getTimestamp(), sequence);

		TickValue v = new TickValue(id, tick.getDecimals(), tick.getStrings());

		return new TickEntry(k, v);

	}

	@VisibleForTesting
	void persist(long count, ForwardIterator<TickEntry> itr) throws IOException {

		// Persisted ticks
		long persisted = 0L;

		// Current sequence id
		long currentId = 0L;

		// Allocated sequence id
		int allocated = 0;

		for (TickEntry tick = itr.next(); tick != null; tick = itr.next()) {

			if (allocated == 0L) {

				long remaining = count - persisted;

				allocated = (int) Math.min(remaining, Integer.MAX_VALUE);

				currentId = tickDatabase.generateSequenceId(tx, allocated);

			}

			long codeId = tick.getKey().getCodeId();

			long timestamp = tick.getKey().getTimestamp();

			TickKey newKey = new TickKey(codeId, timestamp, currentId);

			TickEntry entry = new TickEntry(newKey, tick.getValue());

			tickDatabase.persist(tx, entry);

			persisted++;

			currentId++;

			allocated--;

		}

		if (persisted > 0L) {
			tickDatabase.sync();
		}

	}

}
