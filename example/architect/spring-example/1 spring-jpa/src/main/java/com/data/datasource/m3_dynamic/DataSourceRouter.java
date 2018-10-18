package com.data.datasource.m3_dynamic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Slf4j
@Profile("multi-3")
public class DataSourceRouter
        extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        log.debug("determineCurrentLookupKey: {}", DataSourceContextHolder.get());
        return DataSourceContextHolder.get();
    }
}