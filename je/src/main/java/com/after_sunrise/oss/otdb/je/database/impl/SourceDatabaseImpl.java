package com.after_sunrise.oss.otdb.je.database.impl;

import static com.sleepycat.je.OperationStatus.SUCCESS;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.after_sunrise.oss.otdb.je.binding.TickSourceBinding;
import com.after_sunrise.oss.otdb.je.database.Databases;
import com.after_sunrise.oss.otdb.je.database.ForwardIterator;
import com.after_sunrise.oss.otdb.je.database.SourceDatabase;
import com.after_sunrise.oss.otdb.je.entity.TickSource;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.sleepycat.bind.tuple.PackedLongBinding;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DiskOrderedCursor;
import com.sleepycat.je.DiskOrderedCursorConfig;
import com.sleepycat.je.Environment;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.SecondaryConfig;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.Sequence;
import com.sleepycat.je.SequenceConfig;
import com.sleepycat.je.Transaction;

/**
 * @author takanori.takase
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SourceDatabaseImpl implements SourceDatabase {

	private final Log log = LogFactory.getLog(getClass());

	private final StringBinding keyBinding = new StringBinding();

	private final TickSourceBinding valBinding = new TickSourceBinding();

	private final PackedLongBinding idBinding = new PackedLongBinding();

	@Value("DB_Sources")
	private String mainDbName;

	@Value("DB_SourcesIndex")
	private String secDbName;

	@Value("DB_SourcesSequences")
	private String seqDbName;

	@Value("SQ_Sources")
	private String seqName;

	@Resource(name = "sourceMainDbConfig")
	private DatabaseConfig mainDbConfig;

	@Resource(name = "sourceSecDbConfig")
	private SecondaryConfig secDbConfig;

	@Resource(name = "sourceSeqDbConfig")
	private DatabaseConfig seqDbConfig;

	@Resource(name = "sourceSequenceConfig")
	private SequenceConfig seqConfig;

	@Autowired
	private DiskOrderedCursorConfig cursorConfig;

	@Value("#{T(com.sleepycat.je.LockMode).${open-tickdb.source.lock}}")
	private LockMode lockMode;

	private Database database;

	private SecondaryDatabase secDatabase;

	private Database seqDatabase;

	@Override
	public void initialize(Environment environment, Transaction tx)
			throws IOException {

		database = environment.openDatabase(tx, mainDbName, mainDbConfig);

		log.info("Initialized db : " + database.getDatabaseName());

		secDatabase = environment.openSecondaryDatabase(tx, secDbName,
				database, secDbConfig);

		log.info("Initialized db : " + secDatabase.getDatabaseName());

		seqDatabase = environment.openDatabase(tx, seqDbName, seqDbConfig);

		log.info("Initialized db : " + seqDatabase.getDatabaseName());

	}

	@Override
	public void close() throws IOException {

		IOUtils.closeQuietly(seqDatabase);

		IOUtils.closeQuietly(secDatabase);

		IOUtils.closeQuietly(database);

	}

	@Override
	public void sync() throws IOException {

		Databases.sync(seqDatabase);

		Databases.sync(secDatabase);

		Databases.sync(database);

	}

	@VisibleForTesting
	long generateSequenceId(Transaction txn) throws IOException {

		DatabaseEntry key = new DatabaseEntry(seqName.getBytes(Charsets.UTF_8));

		Sequence sequence = seqDatabase.openSequence(txn, key, seqConfig);

		try {
			return sequence.get(txn, 1);
		} finally {
			IOUtils.closeQuietly(sequence);
		}

	}

	@Override
	public ForwardIterator<String> keyIterator(Transaction tx)
			throws IOException {

		final DiskOrderedCursor cursor = database.openCursor(cursorConfig);

		final DatabaseEntry key = new DatabaseEntry();

		final DatabaseEntry val = new DatabaseEntry();

		return new ForwardIterator<String>() {
			@Override
			public void close() throws IOException {
				cursor.close();
			}

			@Override
			public String next() {
				if (SUCCESS != cursor.getNext(key, val, null)) {
					return null;
				}
				return keyBinding.entryToObject(key);
			}
		};

	}

	@Override
	public TickSource search(Transaction tx, String source) throws IOException {

		if (StringUtils.isBlank(source)) {
			return null;
		}

		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry val = new DatabaseEntry();

		keyBinding.objectToEntry(source, key);

		if (SUCCESS != database.get(tx, key, val, lockMode)) {
			return null;
		}

		return valBinding.entryToObject(val);

	}

	@Override
	public String search(Transaction tx, long id) throws IOException {

		DatabaseEntry secKey = new DatabaseEntry();
		idBinding.objectToEntry(id, secKey);

		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry val = new DatabaseEntry();

		// Skip value retrieval.
		val.setPartial(0, 0, true);

		if (SUCCESS != secDatabase.get(tx, secKey, key, val, lockMode)) {
			return null;
		}

		return keyBinding.entryToObject(key);

	}

	@Override
	public TickSource insert(Transaction tx, String source) throws IOException {

		if (StringUtils.isBlank(source)) {
			throw new IOException("Source cannot be blank.");
		}

		long id = generateSequenceId(tx);

		TickSource tickSource = new TickSource(id);

		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry val = new DatabaseEntry();

		keyBinding.objectToEntry(source, key);
		valBinding.objectToEntry(tickSource, val);

		if (SUCCESS != database.putNoOverwrite(tx, key, val)) {
			throw new IOException("Duplicate source : " + source);
		}

		return tickSource;

	}

	@Override
	public void persist(Transaction tx, String source, TickSource tickSource)
			throws IOException {

		if (StringUtils.isBlank(source)) {
			throw new IOException("Source cannot be blank.");
		}
		if (tickSource == null) {
			throw new IOException("TickSource cannot be null.");
		}

		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry val = new DatabaseEntry();

		keyBinding.objectToEntry(source, key);
		valBinding.objectToEntry(tickSource, val);

		if (SUCCESS != database.put(tx, key, val)) {
			throw new IOException("Failed to persist source : " + source);
		}

	}

	@Override
	public TickSource delete(Transaction tx, String source) throws IOException {

		if (StringUtils.isBlank(source)) {
			return null;
		}

		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry val = new DatabaseEntry();

		keyBinding.objectToEntry(source, key);

		if (SUCCESS != database.get(tx, key, val, lockMode)) {
			return null;
		}

		TickSource tickSource = valBinding.entryToObject(val);

		if (Boolean.TRUE.equals(tickSource.getDeleted())) {
			return null;
		}

		tickSource.setDeleted(true);

		persist(tx, source, tickSource);

		return tickSource;

	}

	@Override
	public void truncate(Transaction tx, String source) throws IOException {

		if (StringUtils.isBlank(source)) {
			return;
		}

		DatabaseEntry key = new DatabaseEntry();

		keyBinding.objectToEntry(source, key);

		if (SUCCESS != database.delete(tx, key)) {
			throw new IOException("Failed to truncate source : " + source);
		}

	}

}
