package com.after_sunrise.oss.otdb.je.database;

import java.io.Closeable;

/**
 * @author takanori.takase
 */
public interface ForwardIterator<T> extends Closeable {

	T next();

}
