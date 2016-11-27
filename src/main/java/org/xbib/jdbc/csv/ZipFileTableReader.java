package org.xbib.jdbc.csv;

import org.xbib.jdbc.io.TableReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Enables reading CSV files packed in a ZIP file as database tables.
 */
public class ZipFileTableReader implements TableReader {
    private String zipFilename;
    private ZipFile zipFile;
    private String fileExtension;
    private String charset;

    public ZipFileTableReader(String zipFilename, String charset) throws IOException {
        this.zipFilename = zipFilename;
        this.zipFile = new ZipFile(zipFilename);
        this.charset = charset;
    }

    public void setExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getZipFilename() {
        return zipFilename;
    }

    @Override
    public Reader getReader(Statement statement, String tableName) throws SQLException {
        try {
            ZipEntry zipEntry = zipFile.getEntry(tableName + fileExtension);
            if (zipEntry == null) {
                throw new SQLException(CsvResources.getString("tableNotFound") + ": " + tableName);
            }

            Reader reader;
            if (charset != null) {
                reader = new InputStreamReader(zipFile.getInputStream(zipEntry), charset);
            } else {
                reader = new InputStreamReader(zipFile.getInputStream(zipEntry));
            }
            return reader;
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<String> getTableNames(Connection connection) throws SQLException {
        Vector<String> v = new Vector<String>();
        Enumeration<? extends ZipEntry> en = zipFile.entries();
        while (en.hasMoreElements()) {
            /*
			 * Strip file extensions.
			 */
            String name = ((ZipEntry) en.nextElement()).getName();
            if (name.endsWith(fileExtension)) {
                name = name.substring(0, name.length() - fileExtension.length());
            }
            v.add(name);
        }
        return v;
    }
}
