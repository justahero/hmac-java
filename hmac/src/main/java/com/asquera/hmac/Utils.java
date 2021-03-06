package com.asquera.hmac;

import java.util.Iterator;
import java.util.Map;

public class Utils {
    
    /**
     * Interpolates a string and replaces all occurrences of "%{label}" with replacements
     * available in the Map.
     * 
     * For example if in the replacements map there is a "auth_header" => "Authorization" then in a given
     * input string "auth_header is %{auth_header}" becomes "auth_header is Authorization".
     * 
     * See http://jira.codehaus.org/browse/JRUBY-5338 for more details.
     * 
     * @param input The input string which contains place holders "%{var}"
     * @param replacements A map of possible replacements for the input string
     * 
     * @return A string with all found replacements done.
     * 
     * @throws IllegalArgumentException
     */
    public static String interpolateString(final String input, final Map<String, String> replacements)
        throws IllegalArgumentException {
        
        if (input == null)
            throw new IllegalArgumentException();
        if (replacements == null)
            throw new IllegalArgumentException();
        
        String result = input;
        for (final String key : replacements.keySet()) {
            String regex = "%\\{" + key + "\\}";
            String value = replacements.get(key);
            result = result.replaceAll(regex, value);
        }
        
        return result;
    }
    
    /**
     * Joins a collection of strings to a single string, all parts are merged with a delimiter string.
     * 
     * @param container
     * @param delimiter
     * @return
     */
    public static String join(final Iterable<String> container, final String delimiter) {
        StringBuilder builder = new StringBuilder();
        Iterator<String> it = container.iterator();
        while (it.hasNext()) {
            builder.append(it.next());
            if (it.hasNext())
                builder.append(delimiter);
        }
        return builder.toString();
    }
}
