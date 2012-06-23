package jp.gr.java_conf.afterthesunrise.open.tickdb.service;

import java.io.Closeable;
import java.io.IOException;

public interface CloseableIterator<T> extends Closeable {

	T first() throws IOException;

	T previous() throws IOException;

	T previous(long timestamp) throws IOException;

	T next() throws IOException;

	T next(long timestamp) throws IOException;

	T last() throws IOException;

}
