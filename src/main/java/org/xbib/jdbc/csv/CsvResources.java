package org.xbib.jdbc.csv;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 *
 */
public class CsvResources {
    private static ResourceBundle messages = PropertyResourceBundle.getBundle("org.xbib.jdbc.csv.messages", Locale.getDefault());

    public static String getString(String key) {
        try {
            return messages.getString(key);
        } catch (MissingResourceException e) {
            return "[" + key + "]";
        }
    }
}
