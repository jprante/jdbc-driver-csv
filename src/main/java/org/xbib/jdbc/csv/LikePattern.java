package org.xbib.jdbc.csv;

import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * Performs string matching for SQL LIKE patterns.
 */
public class LikePattern {
    public static final String DEFAULT_ESCAPE_STRING = "\\";

    /**
     * Global lookup table of LIKE pattern to compiled regular expression.
     */
    private static Hashtable<String, Pattern> compiledRegexs = new Hashtable<String, Pattern>();

    /**
     * @param likePattern an SQL LIKE pattern including % and _ characters.
     * @param escape SQL ESCAPE character, or empty string for no escaping.
     * @param input       string to be matched.
     * @return true if input string matches LIKE pattern.
     */
    public static boolean matches(String likePattern, String escape, CharSequence input) {
        boolean retval;
        int percentIndex = likePattern.indexOf('%');
        int underscoreIndex = likePattern.indexOf('_');
        if (percentIndex < 0 && underscoreIndex < 0) {
            /*
			 * No wildcards in pattern so we can just compare strings.
			 */
            retval = likePattern.equals(input);
        } else {
            Pattern p = compiledRegexs.get(likePattern);
            if (p == null) {
				/*
				 * First convert LIKE pattern to a regular expression.
				 */
                boolean isEscaped = false;
                StringBuilder regex = new StringBuilder();
                StringTokenizer tokenizer = new StringTokenizer(likePattern, "%_" + escape, true);
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    if (token.equals(escape)) {
                        if (isEscaped) {
							/*
							 * Two escaped characters in a row result match a
							 * single literal escape character.
							 */
                            regex.append(Pattern.quote(token));
                        } else {
                            isEscaped = true;
                        }
                    } else {
                        if (isEscaped) {
                            regex.append(Pattern.quote(token));
                        } else if (token.equals("%")) {
                            regex.append(".*");
                        } else if (token.equals("_")) {
                            regex.append(".");
                        } else {
                            regex.append(Pattern.quote(token));
                        }
                        isEscaped = false;
                    }
                }

				/*
				 * Cache compiled regular expression because we will probably be
				 * using the same one again and again.
				 */
                p = Pattern.compile(regex.toString());
                compiledRegexs.put(likePattern, p);
            }
            retval = p.matcher(input).matches();
        }
        return retval;
    }
}
