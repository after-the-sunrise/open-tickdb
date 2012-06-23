package jp.gr.java_conf.afterthesunrise.open.tickdb.loader;

import java.io.IOException;

/**
 * @author takanori.takase
 */
public interface LoadableTickLoader {

	boolean exists(String source) throws IOException;

	long count(String source) throws IOException;

	long delete(String source) throws IOException;

	long load(String source, LoadableTickIterator iterator) throws IOException;

}
