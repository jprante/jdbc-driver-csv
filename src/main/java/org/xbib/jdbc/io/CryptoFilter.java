package org.xbib.jdbc.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Minimalistic approach to decrypting a file.
 * A class implementing this interface is required to return one character at a
 * time from the given InputStream. the InputStream is encrypted, the client
 * receives it clear-text.
 * The encryption used may require you to read more than one character from the
 * InputStream, but this is your business, as is all initialization required by
 * your cipher, the client will be offered one deciphered character at a time.
 *
 */
public interface CryptoFilter {
    int read(InputStream in) throws IOException;

    void write(OutputStream out, int ch) throws IOException;

    int read(InputStream in, byte[] b, int off, int len) throws IOException;

    int read(InputStream in, byte[] b) throws IOException;

    void reset();
}
