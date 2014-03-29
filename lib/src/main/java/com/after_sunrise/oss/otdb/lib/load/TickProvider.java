package com.after_sunrise.oss.otdb.lib.load;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import com.after_sunrise.oss.otdb.api.loader.LoadableTickIterator;

/**
 * @author takanori.takase
 */
public interface TickProvider extends Closeable {

	void initialize() throws IOException;

	List<String> listSources() throws IOException;

	LoadableTickIterator iterator(String source) throws IOException;

}
