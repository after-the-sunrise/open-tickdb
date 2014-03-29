package com.after_sunrise.oss.otdb.je.database;

import java.io.IOException;
import java.util.List;

import com.after_sunrise.oss.otdb.je.entity.TickEntry;
import com.after_sunrise.oss.otdb.je.entity.TickKey;
import com.sleepycat.je.Transaction;

/**
 * @author takanori.takase
 */
public interface TickDatabase extends Database {

	long count();

	List<TickEntry> find(Transaction tx, long codeId, Long start, Long end)
			throws IOException;

	TickDatabaseIterator iterator(Transaction tx, long codeId, Long start,
			Long end) throws IOException;

	ForwardIterator<TickKey> keyIterator(Transaction tx) throws IOException;

	ForwardIterator<TickEntry> entryIterator(Transaction tx) throws IOException;

	long generateSequenceId(Transaction tx) throws IOException;

	long generateSequenceId(Transaction tx, int delta) throws IOException;

	void persist(Transaction tx, TickEntry tick) throws IOException;

	void persist(Transaction tx, TickEntry tick, boolean overwrite)
			throws IOException;

	boolean delete(Transaction tx, TickKey key) throws IOException;

}
