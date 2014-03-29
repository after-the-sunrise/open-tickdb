package com.after_sunrise.oss.otdb.je.database.impl;

import static com.sleepycat.je.OperationStatus.SUCCESS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.after_sunrise.oss.otdb.je.binding.TickKeyTupleBinding;
import com.after_sunrise.oss.otdb.je.binding.TickValueTupleBinding;
import com.after_sunrise.oss.otdb.je.database.Databases;
import com.after_sunrise.oss.otdb.je.database.ForwardIterator;
import com.after_sunrise.oss.otdb.je.database.TickDatabase;
import com.after_sunrise.oss.otdb.je.database.TickDatabaseIterator;
import com.after_sunrise.oss.otdb.je.entity.TickEntry;
import com.after_sunrise.oss.otdb.je.entity.TickKey;
import com.after_sunrise.oss.otdb.je.entity.TickValue;
import com.google.common.base.Charsets;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DiskOrderedCursor;
import com.sleepycat.je.DiskOrderedCursorConfig;
import com.sleepycat.je.Environment;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Sequence;
import com.sleepycat.je.SequenceConfig;
import com.sleepycat.je.Transaction;

/**
 * @author takanori.takase
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TickDatabaseImpl implements TickDatabase {

	private final Log log = LogFactory.getLog(getClass());

	@Value("DB_Ticks")
	private String mainDbName;

	@Value("DB_TickSequences")
	private String seqDbName;

	@Value("SQ_Ticks")
	private String seqName;

	@Resource(name = "tickMainDbConfig")
	private DatabaseConfig mainDbConfig;

	@Resource(name = "tickSeqDbConfig")
	private DatabaseConfig seqDbConfig;

	@Resource(name = "tickSequenceConfig")
	private SequenceConfig seqConfig;

	@Value("#{T(com.sleepycat.je.LockMode).${open-tickdb.tick.lock}}")
	private LockMode lockMode;

	@Autowired
	private DiskOrderedCursorConfig cursorConfig;

	@Autowired
	private TickKeyTupleBinding keyBinding;

	@Autowired
	private TickValueTupleBinding valBinding;

	private Database database;

	private Database seqDatabase;

	@Override
	public void initialize(Environment env, Transaction tx) throws IOException {

		database = env.openDatabase(tx, mainDbName, mainDbConfig);

		log.info("Initialized db : " + database.getDatabaseName());

		seqDatabase = env.openDatabase(tx, seqDbName, seqDbConfig);

		log.info("Initialized db : " + seqDatabase.getDatabaseName());

	}

	@Override
	public void close() throws IOException {

		IOUtils.closeQuietly(seqDatabase);

		IOUtils.closeQuietly(database);

	}

	@Override
	public void sync() throws IOException {

		Databases.sync(seqDatabase);

		Databases.sync(database);

	}

	@Override
	public long count() {

		long count = 0L;

		try (DiskOrderedCursor cursor = database.openCursor(cursorConfig)) {

			DatabaseEntry key = new DatabaseEntry();

			DatabaseEntry val = new DatabaseEntry();

			while (cursor.getNext(key, val, null) == SUCCESS) {
				count++;
			}

		}

		return count;

	}

	@Override
	public List<TickEntry> find(Transaction tx, long id, Long start, Long end)
			throws IOException {

		ArrayList<TickEntry> ticks = new ArrayList<>();

		try (TickDatabaseIterator itr = iterator(tx, id, start, end)) {

			TickEntry tick = itr.first();

			while (tick != null) {

				ticks.add(tick);

				tick = itr.next();

			}

		}

		ticks.trimToSize();

		return ticks;

	}

	@Override
	public TickDatabaseIterator iterator(Transaction tx, long id, Long start,
			Long end) throws IOException {

		Cursor cursor = database.openCursor(tx, null);

		long s = start == null ? Long.MIN_VALUE : start;

		long e = end == null ? Long.MAX_VALUE : end;

		return new TickDatabaseIteratorImpl(cursor, lockMode, keyBinding,
				valBinding, id, s, e);

	}

	@Override
	public ForwardIterator<TickKey> keyIterator(Transaction tx)
			throws IOException {

		final DiskOrderedCursor cursor = database.openCursor(cursorConfig);

		final DatabaseEntry key = new DatabaseEntry();

		final DatabaseEntry val = new DatabaseEntry();

		return new ForwardIterator<TickKey>() {
			@Override
			public void close() throws IOException {
				cursor.close();
			}

			@Override
			public TickKey next() {
				if (SUCCESS != cursor.getNext(key, val, null)) {
					return null;
				}
				return keyBinding.entryToObject(key);
			}
		};

	}

	@Override
	public ForwardIterator<TickEntry> entryIterator(Transaction tx)
			throws IOException {

		final Cursor cursor = database.openCursor(tx, null);

		final DatabaseEntry key = new DatabaseEntry();

		final DatabaseEntry val = new DatabaseEntry();

		return new ForwardIterator<TickEntry>() {

			@Override
			public void close() throws IOException {
				cursor.close();
			}

			@Override
			public TickEntry next() {

				if (SUCCESS != cursor.getNext(key, val, lockMode)) {
					return null;
				}

				TickKey k = keyBinding.entryToObject(key);

				TickValue v = valBinding.entryToObject(val);

				return new TickEntry(k, v);

			}

		};

	}

	@Override
	public long generateSequenceId(Transaction txn) throws IOException {
		return generateSequenceId(txn, 1);
	}

	@Override
	public long generateSequenceId(Transaction txn, int delta)
			throws IOException {

		DatabaseEntry key = new DatabaseEntry(seqName.getBytes(Charsets.UTF_8));

		Sequence sequence = seqDatabase.openSequence(txn, key, seqConfig);

		try {
			return sequence.get(txn, delta);
		} finally {
			IOUtils.closeQuietly(sequence);
		}

	}

	@Override
	public void persist(Transaction tx, TickEntry tick) throws IOException {
		persist(tx, tick, false);
	}

	@Override
	public void persist(Transaction tx, TickEntry tick, boolean overwrite)
			throws IOException {

		if (tick == null) {
			throw new IOException("Cannot persist null.");
		}

		DatabaseEntry key = new DatabaseEntry();
		keyBinding.objectToEntry(tick.getKey(), key);

		DatabaseEntry val = new DatabaseEntry();
		valBinding.objectToEntry(tick.getValue(), val);

		OperationStatus status;

		if (overwrite) {
			status = database.put(tx, key, val);
		} else {
			status = database.putNoOverwrite(tx, key, val);
		}

		if (SUCCESS == status) {
			return;
		}

		throw new IOException("Failed to persist tick : " + tick);

	}

	@Override
	public boolean delete(Transaction tx, TickKey key) throws IOException {

		if (key == null) {
			return false;
		}

		DatabaseEntry entry = new DatabaseEntry();

		keyBinding.objectToEntry(key, entry);

		return database.delete(tx, entry) == SUCCESS;

	}

}
