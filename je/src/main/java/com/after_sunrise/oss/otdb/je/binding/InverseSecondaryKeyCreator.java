package com.after_sunrise.oss.otdb.je.binding;

import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryKeyCreator;

/**
 * @author takanori.takase
 */
public class InverseSecondaryKeyCreator implements SecondaryKeyCreator {

	@Override
	public boolean createSecondaryKey(SecondaryDatabase secondary,
			DatabaseEntry key, DatabaseEntry data, DatabaseEntry result) {

		result.setData(data.getData(), data.getOffset(), data.getSize());

		return true;

	}

}
