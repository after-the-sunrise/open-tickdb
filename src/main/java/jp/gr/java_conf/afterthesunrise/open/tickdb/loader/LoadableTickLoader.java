package jp.gr.java_conf.afterthesunrise.open.tickdb.loader;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author takanori.takase
 */
public interface LoadableTickLoader extends Closeable {

	void initialize() throws IOException;

	Long find(String source) throws IOException;

	Long delete(String source) throws IOException;

	long load(String source, LoadableTickIterator iterator) throws IOException;

}
