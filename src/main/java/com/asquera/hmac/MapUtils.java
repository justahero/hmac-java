package com.asquera.hmac;

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
        if (leftMap.isEmpty())
            return rightMap;
        return leftMap;
    }
}
