package com.after_sunrise.oss.otdb.lib.load.impl.source;

import static org.apache.commons.io.FileUtils.listFiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.after_sunrise.commons.log.object.Logs;
import com.after_sunrise.oss.otdb.lib.load.TickSourceFactory;

/**
 * @author takanori.takase
 */
public class FileTickSourceFactory implements TickSourceFactory {

	private final Log log = LogFactory.getLog(getClass());

	private File file;

	private IOFileFilter fileFilter;

	private IOFileFilter dirFilter;

	public void setFile(File file) {
		this.file = file;
	}

	public void setFileFilter(IOFileFilter fileFilter) {
		this.fileFilter = fileFilter;
	}

	public void setDirFilter(IOFileFilter dirFilter) {
		this.dirFilter = dirFilter;
	}

	@Override
	public List<String> list() throws IOException {

		String parent = file.getAbsolutePath();

		Logs.logDebug(log, "Listing files : %s", parent);

		Collection<File> files = listFiles(file, fileFilter, dirFilter);

		List<String> list = new ArrayList<>(files.size());

		for (File f : files) {

			String child = f.getAbsolutePath();

			String path = StringUtils.removeStart(child, parent);

			list.add(path);

			Logs.logTrace(log, "File found : %s", path);

		}

		Logs.logDebug(log, "Listed files : [%,3d] %s", list.size(), parent);

		return list;

	}

	@Override
	public InputStream openInputStream(String source) throws IOException {

		File sourceFile = new File(file, source);

		Logs.logDebug(log, "Opening file stream : [%,3d bytes] %s",
				sourceFile.length(), sourceFile.getAbsolutePath());

		return new FileInputStream(sourceFile);

	}

}
