package com.after_sunrise.oss.open.tickdb.loader;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * @author takanori.takase
 */
public interface LoadableTickLoader extends Closeable {

	void initialize() throws IOException;

	List<String> list() throws IOException;

	Long find(String source) throws IOException;

	Long delete(String source) throws IOException;

	long load(String source, LoadableTickIterator iterator) throws IOException;

}
