package com.after_sunrise.oss.otdb.lib.load.impl.source;

import static com.google.common.base.MoreObjects.firstNonNull;
import static org.apache.commons.lang.ArrayUtils.EMPTY_STRING_ARRAY;
import static org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.input.ProxyInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;

import com.after_sunrise.commons.log.object.Logs;
import com.after_sunrise.oss.otdb.lib.load.TickSourceFactory;

/**
 * @author takanori.takase
 */
public class FtpTickSourceFactory implements TickSourceFactory {

	private static final String[] EMPTY = EMPTY_STRING_ARRAY;

	private final Log log = LogFactory.getLog(getClass());

	private InetAddress address;

	private String username;

	private String password;

	private String pathname;

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPathname(String pathname) {
		this.pathname = pathname;
	}

	@Override
	public List<String> list() throws IOException {

		Logs.logDebug(log, "Listing ftp files : %s", address);

		FTPClient client = new FTPClient();

		try {

			client.connect(address);

			client.login(username, password);

			client.changeWorkingDirectory(pathname);

			String[] files = firstNonNull(client.listNames(), EMPTY);

			Logs.logDebug(log, "Listed ftp files : %s", files.length);

			client.logout();

			return Arrays.asList(files);

		} finally {

			client.disconnect();

		}

	}

	@Override
	public InputStream openInputStream(String source) throws IOException {

		Logs.logDebug(log, "Opening ftp file stream : %s", source);

		final FTPClient client = new FTPClient();

		InputStream in;

		try {

			client.connect(address);

			client.login(username, password);

			client.changeWorkingDirectory(pathname);

			client.setFileTransferMode(BINARY_FILE_TYPE);

			in = client.retrieveFileStream(source);

		} catch (IOException e) {

			client.disconnect();

			throw e;

		}

		return new ProxyInputStream(in) {
			@Override
			public void close() throws IOException {
				try {
					super.close();
				} finally {
					client.disconnect();
				}
			}
		};

	}

}
