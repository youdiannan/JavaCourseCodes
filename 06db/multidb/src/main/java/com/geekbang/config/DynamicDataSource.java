package com.geekbang.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

    public static final String DEFAULT_DATASOURCE = "mall";

    public static final String SHADOW_DATASOURCE = "mall_shadow";

    private static ThreadLocal<String> curLookupKey = new ThreadLocal<>();

    @Override
    protected Object determineCurrentLookupKey() {
        return curLookupKey.get();
    }

    public static void setLookupKey(String key) {
        curLookupKey.set(key);
    }
}
