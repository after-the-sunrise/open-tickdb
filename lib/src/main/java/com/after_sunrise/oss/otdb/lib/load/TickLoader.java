package com.after_sunrise.oss.otdb.lib.load;

/**
 * @author takanori.takase
 */
public interface TickLoader {

	String CONFIG_KEY = "open-tickdb-lib.config";

	String CONFIG_VAL = "open-tickdb-lib-context.xml";

	void load();

}
