package com.after_sunrise.oss.otdb.je.database;

import java.io.IOException;

import com.sleepycat.je.Transaction;

/**
 * @author takanori.takase
 */
public interface CodeDatabase extends Database {

	String search(Transaction tx, long id) throws IOException;

	Long search(Transaction tx, String code) throws IOException;

	long persist(Transaction tx, String code) throws IOException;

}
