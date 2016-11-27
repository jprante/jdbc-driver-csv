package org.xbib.jdbc.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Example encryption filter that XOR's the all data with a secret seed value.
 */
public class XORCipher implements CryptoFilter {
    private int keyCounter;
    private int[] scrambleKey;

    public XORCipher(String seed) {
        scrambleKey = new int[seed.length()];
        for (int i = 0; i < seed.length(); i++) {
            scrambleKey[i] = seed.charAt(i);
        }
        keyCounter = 0;
    }

    @Override
    public int read(InputStream in) throws IOException {
        if (in.available() > 0) {
            return (byte) scrambleInt(in.read());
        } else {
            return -1;
        }
    }

    @Override
    public int read(InputStream in, byte[] b) throws IOException {
        int len;
        len = in.read(b, 0, b.length);
        scrambleArray(b);
        return len;
    }

    @Override
    public int read(InputStream in, byte[] b, int off, int len)
            throws IOException {
        len = in.read(b, off, len);
        scrambleArray(b);
        return len;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("XORCipher(");
        sb.append(scrambleKey.length);
        sb.append("):'");
        for (int i = 0; i < scrambleKey.length; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(i);
        }
        sb.append("'");
        return sb.toString();
    }

    /**
     * Perform the scrambling algorithm (named bitwise XOR encryption) using
     * bitwise exclusive OR (byte) encrypted character = (byte) original
     * character ^ (byte) key character.
     * <p/>
     * Note that ^ is the symbol for XOR (and not the mathematical power)
     *
     * @param org is the original value
     * @return
     */
    private int scrambleInt(int org) {
        int encrDataChar = org;
        if (scrambleKey.length > 0) {
            encrDataChar = org ^ scrambleKey[keyCounter];
            keyCounter++;
            keyCounter %= scrambleKey.length;
        }
        return encrDataChar;
    }

    /**
     * Perform the scrambling algorithm (named bitwise XOR encryption) using
     * bitwise exclusive OR (byte) encrypted character = (byte) original
     * character ^ (byte) key character.
     * <p/>
     * Note that ^ is the symbol for XOR (and not the mathematical power)
     *
     * @param org is the original byte array
     * @return
     */
    private void scrambleArray(byte[] org) {
        if (scrambleKey.length > 0) {
            for (int i = 0; i < org.length; i++) {
                org[i] ^= scrambleKey[keyCounter];
                keyCounter++;
                keyCounter %= scrambleKey.length;
            }
        }
    }

    @Override
    public void write(OutputStream out, int ch) throws IOException {
        out.write(scrambleInt(ch));
    }

    @Override
    public void reset() {
        keyCounter = 0;
    }
}
