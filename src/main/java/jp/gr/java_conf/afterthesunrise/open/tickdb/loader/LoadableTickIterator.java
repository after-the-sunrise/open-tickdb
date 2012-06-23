package jp.gr.java_conf.afterthesunrise.open.tickdb.loader;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author takanori.takase
 */
public interface LoadableTickIterator extends Closeable {

	boolean hasNext() throws IOException;

	LoadableTick getNext() throws IOException;

}
