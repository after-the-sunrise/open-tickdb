package com.after_sunrise.oss.open.tickdb.loader;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author takanori.takase
 */
public interface LoadableTickIterator extends Closeable {

	void initialize() throws IOException;

	boolean hasNext() throws IOException;

	LoadableTick getNext() throws IOException;

}
