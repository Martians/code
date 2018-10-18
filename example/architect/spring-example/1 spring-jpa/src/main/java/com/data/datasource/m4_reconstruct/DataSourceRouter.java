package com.data.datasource.m4_reconstruct;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Slf4j
@Profile("multi-4")
public class DataSourceRouter
        extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        log.debug("determineCurrentLookupKey: {}", DataSourceContextHolder.get());
        return DataSourceContextHolder.get();
    }
}