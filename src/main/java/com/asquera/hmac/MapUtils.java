package com.asquera.hmac;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {

    /**
     * Merges the content of both arrays
     * 
     * @param leftMap
     * @param rightMap
     * @return
     */
    public static Map<String, Object> mergeMaps(final Map<String, Object> leftMap, final Map<String, Object> rightMap) {
        Map<String, Object> result = new HashMap<String, Object>(leftMap);
        result.putAll(rightMap);
        return result;
    }
}
