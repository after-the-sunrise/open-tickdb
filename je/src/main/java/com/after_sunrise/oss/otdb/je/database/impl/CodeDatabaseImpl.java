package com.after_sunrise.oss.otdb.je.database.impl;

import static com.sleepycat.je.OperationStatus.SUCCESS;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.after_sunrise.oss.otdb.je.database.CodeDatabase;
import com.after_sunrise.oss.otdb.je.database.Databases;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.sleepycat.bind.tuple.PackedLongBinding;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.SecondaryConfig;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.Sequence;
import com.sleepycat.je.SequenceConfig;
import com.sleepycat.je.Transaction;

/**
 * @author takanori.takase
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CodeDatabaseImpl implements CodeDatabase {

	private final Log log = LogFactory.getLog(getClass());

	private final StringBinding keyBinding = new StringBinding();

	private final PackedLongBinding valBinding = new PackedLongBinding();

	@Value("DB_Codes")
	private String mainDbName;

	@Value("DB_CodesIndex")
	private String secDbName;

	@Value("DB_CodesSequences")
	private String seqDbName;

	@Value("SQ_Codes")
	private String seqName;

	@Resource(name = "codeMainDbConfig")
	private DatabaseConfig mainDbConfig;

	@Resource(name = "codeSecDbConfig")
	private SecondaryConfig secDbConfig;

	@Resource(name = "codeSeqDbConfig")
	private DatabaseConfig seqDbConfig;

	@Resource(name = "codeSequenceConfig")
	private SequenceConfig seqConfig;

	@Value("#{T(com.sleepycat.je.LockMode).${open-tickdb.code.lock}}")
	private LockMode lockMode;

	private Database database;

	private SecondaryDatabase secDatabase;

	private Database seqDatabase;

	@Override
	public void initialize(Environment environment, Transaction tx)
			throws IOException {

		database = environment.openDatabase(tx, mainDbName, mainDbConfig);

		log.info("Initialized db : " + database.getDatabaseName());

		secDatabase = environment.openSecondaryDatabase(tx, secDbName,
				database, secDbConfig);

		log.info("Initialized db : " + secDatabase.getDatabaseName());

		seqDatabase = environment.openDatabase(tx, seqDbName, seqDbConfig);

		log.info("Initialized db : " + seqDatabase.getDatabaseName());

	}

	@Override
	public void close() throws IOException {

		IOUtils.closeQuietly(seqDatabase);

		IOUtils.closeQuietly(secDatabase);

		IOUtils.closeQuietly(database);

	}

	@Override
	public void sync() throws IOException {

		Databases.sync(seqDatabase);

		Databases.sync(secDatabase);

		Databases.sync(database);

	}

	@VisibleForTesting
	long generateSequenceId(Transaction txn) throws IOException {

		DatabaseEntry key = new DatabaseEntry(seqName.getBytes(Charsets.UTF_8));

		Sequence sequence = seqDatabase.openSequence(txn, key, seqConfig);

		try {
			return sequence.get(txn, 1);
		} finally {
			IOUtils.closeQuietly(sequence);
		}

	}

	@Override
	public Long search(Transaction tx, String code) throws IOException {

		if (StringUtils.isBlank(code)) {
			return null;
		}

		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry val = new DatabaseEntry();

		keyBinding.objectToEntry(code, key);

		if (SUCCESS != database.get(tx, key, val, lockMode)) {
			return null;
		}

		return valBinding.entryToObject(val);

	}

	@Override
	public String search(Transaction tx, long id) throws IOException {

		DatabaseEntry secKey = new DatabaseEntry();
		valBinding.objectToEntry(id, secKey);

		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry val = new DatabaseEntry();

		if (SUCCESS != secDatabase.get(tx, secKey, key, val, lockMode)) {
			return null;
		}

		return keyBinding.entryToObject(key);

	}

	@Override
	public long persist(Transaction tx, String code) throws IOException {

		if (StringUtils.isBlank(code)) {
			throw new IOException("Code cannot be blank.");
		}

		long id = generateSequenceId(tx);

		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry val = new DatabaseEntry();

		keyBinding.objectToEntry(code, key);
		valBinding.objectToEntry(id, val);

		if (SUCCESS != database.putNoOverwrite(tx, key, val)) {
			throw new IOException("Duplicate code : " + code);
		}

		return id;

	}

}
