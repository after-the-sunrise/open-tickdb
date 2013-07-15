package com.after_sunrise.oss.open.tickdb.service;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author takanori.takase
 */
public interface CloseableIterator<T> extends Closeable {

	T first() throws IOException;

	T previous() throws IOException;

	T previous(long timestamp) throws IOException;

	T next() throws IOException;

	T next(long timestamp) throws IOException;

	T last() throws IOException;

}
