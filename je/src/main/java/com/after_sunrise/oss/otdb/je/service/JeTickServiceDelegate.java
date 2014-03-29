package com.after_sunrise.oss.otdb.je.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.after_sunrise.oss.otdb.api.service.TickIterator;
import com.after_sunrise.oss.otdb.api.service.TickService;
import com.after_sunrise.oss.otdb.api.tick.Tick;
import com.after_sunrise.oss.otdb.je.database.CodeDatabase;
import com.after_sunrise.oss.otdb.je.database.TickDatabase;
import com.after_sunrise.oss.otdb.je.database.TickDatabaseIterator;
import com.after_sunrise.oss.otdb.je.database.impl.TickIteratorAdapter;
import com.after_sunrise.oss.otdb.je.database.impl.TickIteratorImpl;
import com.after_sunrise.oss.otdb.je.entity.TickEntry;
import com.after_sunrise.oss.otdb.je.entity.TickImpl;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.sleepycat.je.Environment;
import com.sleepycat.je.Transaction;

/**
 * @author takanori.takase
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JeTickServiceDelegate implements TickService {

	private final Log log = LogFactory.getLog(getClass());

	@Autowired
	private Environment environment;

	@Autowired
	private CodeDatabase codeDatabase;

	@Autowired
	private TickDatabase tickDatabase;

	// TODO Handle transaction if needed.
	private Transaction tx;

	@VisibleForTesting
	void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@VisibleForTesting
	void setCodeDatabase(CodeDatabase codeDatabase) {
		this.codeDatabase = codeDatabase;
	}

	@VisibleForTesting
	void setTickDatabase(TickDatabase tickDatabase) {
		this.tickDatabase = tickDatabase;
	}

	@VisibleForTesting
	void setTx(Transaction tx) {
		this.tx = tx;
	}

	@Override
	public void initialize() throws IOException {

		log.info("Initializing service.");

		codeDatabase.initialize(environment, tx);

		tickDatabase.initialize(environment, tx);

		log.info("Initialized service.");

	}

	@Override
	public void close() throws IOException {

		log.info("Closing service.");

		IOUtils.closeQuietly(tickDatabase);

		IOUtils.closeQuietly(codeDatabase);

		log.info("Closed service.");

	}

	@Override
	public List<Tick> find(final String code, Long start, Long end)
			throws IOException {

		return find(code, start, end, null);

	}

	@Override
	public List<Tick> find(final String code, Long start, Long end,
			Set<Integer> fields) throws IOException {

		Long id = codeDatabase.search(tx, code);

		if (id == null) {
			return Collections.emptyList();
		}

		List<TickEntry> entries = tickDatabase.find(tx, id, start, end);

		if (entries == null) {
			return Collections.emptyList();
		}

		return Lists.transform(entries, new Function<TickEntry, Tick>() {
			@Override
			public Tick apply(TickEntry input) {
				return new TickImpl(code, input);
			}
		});

	}

	@Override
	public TickIterator iterate(String code, Long start, Long end)
			throws IOException {

		return iterate(code, start, end, null);

	}

	@Override
	public TickIterator iterate(String code, Long start, Long end,
			Set<Integer> fields) throws IOException {

		Long id = codeDatabase.search(tx, code);

		if (id == null) {
			return TickIteratorAdapter.getInstance();
		}

		TickDatabaseIterator itr = tickDatabase.iterator(tx, id, start, end);

		if (itr == null) {
			return TickIteratorAdapter.getInstance();
		}

		return new TickIteratorImpl(code, itr);

	}

}
