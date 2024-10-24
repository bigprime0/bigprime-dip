package com.bigprime.source.spi.internals.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base32;
import cn.hutool.core.util.StrUtil;
import com.bigprime.source.spi.internals.AbstractConnection;
import com.bigprime.source.spi.model.Response;
import com.bigprime.source.spi.model.SourceConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Slf4j
public class JdbcConnection
        extends AbstractConnection {
    private Response response;
    private Connection connection;

    public JdbcConnection(SourceConfig sourceConfig, Response response) {
        super(sourceConfig, response);
    }

    protected String formatJdbcUrl() {
        if (StrUtil.isBlank(this.getSourceConfig().getHost()) && StrUtil.isNotBlank(this.getSourceConfig().getUrl())) {
            return this.getSourceConfig().getUrl();
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append("jdbc:");
        buffer.append(this.getSourceConfig().getJdbcType());
        if (getSourceConfig().getJdbcType().equals("influxdb")) {
            buffer.append(":");
        } else {
            buffer.append("://");
        }
        buffer.append(this.getSourceConfig().getHost());
        buffer.append(":");
        buffer.append(this.getSourceConfig().getPort());
        if (StrUtil.isNotBlank(this.getSourceConfig().getDatabase())) {
            buffer.append("/");
            buffer.append(this.getSourceConfig().getDatabase());
        }
        if (BeanUtil.isNotEmpty(this.getSourceConfig().getSsl())) {
            buffer.append(String.format("?ssl=%s", this.getSourceConfig().getSsl()));
        }
        if (BeanUtil.isNotEmpty(this.getSourceConfig().getConfigures())) {
            Map<String, Object> env = this.getSourceConfig().getConfigures();
            List<String> flatEnv = env.entrySet()
                    .stream()
                    .map(value -> String.format("%s=%s", value.getKey(), value.getValue()))
                    .collect(Collectors.toList());
            if (BeanUtil.isEmpty(this.getSourceConfig().getSsl())) {
                buffer.append("?");
            } else {
                buffer.append("&");
            }
            buffer.append(String.join("&", flatEnv));
        }
        return buffer.toString();
    }

    protected Connection openConnection() {
        try {
            this.response = getResponse();
            String url = formatJdbcUrl();
            String userName = "";
            String passWord = "";
            if (StrUtil.isNotBlank(getSourceConfig().getUsername())) {
                userName = getSourceConfig().getUsername();
            }
            if (StrUtil.isNotBlank(getSourceConfig().getPassword())) {
                passWord = getSourceConfig().getPassword();
            }
            this.connection = getConnection(getSourceConfig().getUrl(), url, userName, passWord, getSourceConfig().getJdbcDriver());
            response.setIsConnected(Boolean.TRUE);
        } catch (Exception ex) {
            log.warn("Connection failed {}", ex.getMessage());
            response.setIsConnected(Boolean.FALSE);
            response.setMessage(ex.getMessage());
        }
        return this.connection;
    }

    public void destroy() {
        if (ObjectUtils.isNotEmpty(this.connection)) {
            try {
                this.connection.close();
            } catch (SQLException ex) {
                log.error("Connection close failed ", ex);
            }
        }
    }

    private static ConcurrentMap<String, HikariDataSource> maps = new ConcurrentHashMap();

    private static List<String> connectionCodes = new ArrayList();

    private static Connection getConnection(String oldUrl, String url, String username, String password, String driverName) throws SQLException {
        if (!StrUtil.isBlank(oldUrl) && !oldUrl.equals(url)) {
            String oldCode = Base32.encode(String.format("%s:%s:%s", oldUrl, username, password));
            if (maps.containsKey(oldCode)) {
                maps.get(oldCode).close();
                maps.remove(oldCode);
            }
            if (connectionCodes.contains(oldCode)) {
                connectionCodes.remove(oldCode);
            }
        }

        String code = Base32.encode(String.format("%s:%s:%s", url, username, password));
        if (connectionCodes.contains(code)) {
            return getConnection(url, username, password);
        }
        if (!maps.containsKey(code)) {
            Driver driver = null;
            try {
                driver = (Driver) Class.forName(driverName)
                        .getDeclaredConstructor()
                        .newInstance();
                DriverManager.registerDriver(driver);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                HikariConfig config = getHikariConfig(url, username, password, driverName);
                HikariDataSource manager = new HikariDataSource(config);
                maps.put(code, manager);
                return manager.getConnection();
            } catch (Exception ex) {
                log.warn("Pool connection failed:{}", ex.getMessage());
                Connection connection = getConnection(url, username, password);
                connectionCodes.add(code);
                return connection;
            }
        } else {
            return maps.get(code).getConnection();
        }
    }

    private static Connection getConnection(String url, String username, String password) {
        try {
            Connection connection = null;
            if (StrUtil.isNotBlank(username) && StrUtil.isNotBlank(password)) {
                connection = DriverManager.getConnection(url, username, password);
            } else {
                Properties properties = new Properties();
                if (StrUtil.isNotBlank(username)) {
                    properties.put("user", username);
                }
                if (StrUtil.isNotBlank(password)) {
                    properties.put("password", password);
                }
                connection = DriverManager.getConnection(url, properties);
            }
            return connection;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static HikariConfig getHikariConfig(String url, String username, String password, String driverName) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverName);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        //连接池大小
        config.setMaximumPoolSize(60);
        config.setConnectionTimeout(60000);
        config.setIdleTimeout(60000);
        config.setValidationTimeout(3000);
        config.setMaxLifetime(60000);
        config.setMinimumIdle(10);
        return config;
    }
}
