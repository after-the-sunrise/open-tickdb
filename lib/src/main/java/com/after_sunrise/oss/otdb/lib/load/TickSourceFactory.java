package com.after_sunrise.oss.otdb.lib.load;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author takanori.takase
 */
public interface TickSourceFactory {

	List<String> list() throws IOException;

	InputStream openInputStream(String source) throws IOException;

}
