package com.after_sunrise.oss.otdb.je.database;

import java.io.Closeable;
import java.io.IOException;

import com.sleepycat.je.Environment;
import com.sleepycat.je.Transaction;

/**
 * @author takanori.takase
 */
public interface Database extends Closeable {

	void initialize(Environment environment, Transaction tx) throws IOException;

	void sync() throws IOException;

}
