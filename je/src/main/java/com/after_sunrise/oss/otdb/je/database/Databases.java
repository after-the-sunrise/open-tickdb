package com.after_sunrise.oss.otdb.je.database;

import com.sleepycat.je.Database;

/**
 * @author takanori.takase
 */
public final class Databases {

	private Databases() {
		throw new IllegalAccessError("Utility class shouldn't be instantiated.");
	}

	public static void sync(Database database) {

		if (database == null || !database.getConfig().getDeferredWrite()) {
			return;
		}

		database.sync();

	}

}
