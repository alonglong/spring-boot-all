package com.along.config.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 数据源路由实现类
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSource = DataSourceContextHolder.getDataSource();
        if (dataSource == null) {
            logger.info("当前数据源为[primary]");
        } else {
            logger.info("当前数据源为{}", dataSource);
        }
        return dataSource;
    }

}