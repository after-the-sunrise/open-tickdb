package com.after_sunrise.oss.otdb.je.database;

import java.io.IOException;

import com.after_sunrise.oss.otdb.je.entity.TickSource;
import com.sleepycat.je.Transaction;

/**
 * @author takanori.takase
 */
public interface SourceDatabase extends Database {

	ForwardIterator<String> keyIterator(Transaction tx) throws IOException;

	String search(Transaction tx, long id) throws IOException;

	TickSource search(Transaction tx, String source) throws IOException;

	TickSource insert(Transaction tx, String source) throws IOException;

	void persist(Transaction tx, String source, TickSource tickSource)
			throws IOException;

	TickSource delete(Transaction tx, String source) throws IOException;

	void truncate(Transaction tx, String source) throws IOException;

}
