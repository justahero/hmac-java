package com.asquera.hmac;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

/**
 * Comparator for NameValuePair objects, used for sorting.
 */
class PairComparator implements Comparator<NameValuePair> {
    public int compare(final NameValuePair left, final NameValuePair right) {
        if (left.getName().equalsIgnoreCase(right.getName())) {
            return left.getValue().compareToIgnoreCase(right.getValue());
        }
        return left.getName().compareToIgnoreCase(right.getName());
    }
}

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
     * Returns a list of sorted name value pairs of the url's query.
     * 
     * @param url
     * @return
     * @throws URISyntaxException
     */
    public static List<NameValuePair> getQueryStrings(final String url) throws URISyntaxException {
        URI uri = new URI(url);
        List<NameValuePair> queries = URLEncodedUtils.parse(uri, "UTF-8");
        Collections.sort(queries, new PairComparator());
        return queries;
    }

}
