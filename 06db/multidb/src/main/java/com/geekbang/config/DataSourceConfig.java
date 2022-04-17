package com.geekbang.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.RuleConfiguration;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.shadow.api.config.ShadowRuleConfiguration;
import org.apache.shardingsphere.shadow.api.config.datasource.ShadowDataSourceConfiguration;
import org.apache.shardingsphere.shadow.api.config.table.ShadowTableConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.shadow.url}")
    private String shadowUrl;

    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        DataSource defaultDataSource = hikariDataSource();
        DataSource shadowDataSource = shadowDataSource();
        targetDataSources.put(DynamicDataSource.DEFAULT_DATASOURCE, defaultDataSource);
        targetDataSources.put(DynamicDataSource.SHADOW_DATASOURCE, shadowDataSource);

        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);
        return dynamicDataSource;
    }

    @Bean("shardingDataSource")
    public DataSource shardingDataSource() throws SQLException {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put(DynamicDataSource.DEFAULT_DATASOURCE, hikariDataSource());
        dataSourceMap.put(DynamicDataSource.SHADOW_DATASOURCE, shadowDataSource());
        return ShardingSphereDataSourceFactory.createDataSource(dataSourceMap, createRuleConfigs(), null);
    }

    private Collection<RuleConfiguration> createRuleConfigs() {
        Collection<RuleConfiguration> rules = new LinkedList<>();
        rules.add(createShadowRuleConfig());
        return rules;
    }

    private RuleConfiguration createShadowRuleConfig() {
        ShadowRuleConfiguration result = new ShadowRuleConfiguration();
        result.setDataSources(createShadowDataSources());
        result.setShadowAlgorithms(createShadowAlgorithms());
        result.setTables(createShadowTables());
        return result;
    }

    private Map<String, ShadowDataSourceConfiguration> createShadowDataSources() {
        Map<String, ShadowDataSourceConfiguration> result = new HashMap<>();
        result.put("shadow-data-source", new ShadowDataSourceConfiguration(
                DynamicDataSource.DEFAULT_DATASOURCE, DynamicDataSource.SHADOW_DATASOURCE));
        return result;
    }

    private Map<String, ShardingSphereAlgorithmConfiguration> createShadowAlgorithms() {
        Map<String, ShardingSphereAlgorithmConfiguration> result = new HashMap<>();
        Properties orderIdSelectProps = new Properties();
        orderIdSelectProps.put("operation", "select");
        orderIdSelectProps.put("column", "Id");
        // 以5结束的id命中影子库
        orderIdSelectProps.put("regex", ".*5");
        result.put("select-regex-match-algorithm",
                new ShardingSphereAlgorithmConfiguration("REGEX_MATCH", orderIdSelectProps));
        return result;
    }

    private Map<String, ShadowTableConfiguration> createShadowTables() {
        Map<String, ShadowTableConfiguration> result = new HashMap<>();
        result.put("TB_MALL_ORDER", new ShadowTableConfiguration(new ArrayList<String>(){{add("shadow-data-source");}},
               new ArrayList<String>(){{add("select-regex-match-algorithm");}}));
        return result;
    }

    public DataSource hikariDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return new HikariDataSource(hikariConfig);
    }

    public DataSource shadowDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setJdbcUrl(shadowUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return new HikariDataSource(hikariConfig);
    }


    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
