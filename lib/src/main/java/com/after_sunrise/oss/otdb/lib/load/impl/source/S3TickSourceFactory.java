package com.after_sunrise.oss.otdb.lib.load.impl.source;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.after_sunrise.commons.log.object.Logs;
import com.after_sunrise.oss.otdb.lib.load.TickSourceFactory;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * @author takanori.takase
 */
public class S3TickSourceFactory implements TickSourceFactory {

	private final Log log = LogFactory.getLog(getClass());

	private String accessKey;

	private String secretKey;

	private String bucketName;

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	@Override
	public List<String> list() throws IOException {

		Logs.logDebug(log, "Listing s3 objects : %s", bucketName);

		AWSCredentials credentials = new BasicAWSCredentials(accessKey,
				secretKey);

		AmazonS3Client client = new AmazonS3Client(credentials);

		List<String> list = new ArrayList<>();

		ObjectListing l = client.listObjects(bucketName);

		for (S3ObjectSummary s : l.getObjectSummaries()) {

			String key = s.getKey();

			list.add(key);

			Logs.logTrace(log, "S3 object found : %s", key);

		}

		Logs.logDebug(log, "Listed s3 objects : [%,3d] %s", list.size(),
				bucketName);

		return list;

	}

	@Override
	public InputStream openInputStream(String source) throws IOException {

		AWSCredentials credentials = new BasicAWSCredentials(accessKey,
				secretKey);

		AmazonS3Client client = new AmazonS3Client(credentials);

		S3Object object = client.getObject(bucketName, source);

		Logs.logDebug(log, "Opening s3 object stream : [%,3d bytes] %s", object
				.getObjectMetadata().getContentLength(), source);

		return object.getObjectContent();

	}

}
