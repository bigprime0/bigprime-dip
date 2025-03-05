package com.bigprime.common.constant;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author lyw
 * @version 1.0
 */
@Getter
public enum DataBaseEnum {
    /**
     * 未知数据库类型
     */
    UNKNOWN("unknown", DataBaseGroupEnum.JDBC, null, null, null, null),

    /**
     * MySQL数据库类型
     */
    MYSQL("MySQL", DataBaseGroupEnum.JDBC, "MySql", "com.mysql.jdbc.Driver", "/* ping */ SELECT 1","jdbc:mysql://{host}:{port}/{database}?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true"),

    /**
     * Oracle数据库类型
     */
    ORACLE("Oracle", DataBaseGroupEnum.JDBC, "Oracle", "oracle.jdbc.driver.OracleDriver", "SELECT 'Hello' from DUAL", "jdbc:oracle:thin:@{host}:{port}:{database}"),

    /**
     * SQLServer数据库类型
     */
    SQLSERVER("Sqlserver", DataBaseGroupEnum.JDBC,"SQLServer", "com.microsoft.sqlserver.jdbc.SQLServerDriver", "SELECT 1+2 as a", "jdbc:sqlserver://{host}:{port};DatabaseName={database}"),

    /**
     * PostgreSQL数据库类型
     */
    POSTGRESQL("Postgresql", DataBaseGroupEnum.JDBC, "Postgresql","org.postgresql.Driver", "SELECT 1", "jdbc:postgresql://{host}:{port}/{database}"),

    /**
     * [国产]达梦数据库类型
     */
    DM("Dm", DataBaseGroupEnum.JDBC,"达梦数据库", "dm.jdbc.driver.DmDriver", "SELECT 'Hello' from DUAL", "jdbc:dm://{host}:{port}/{database}"),

    /**
     * [国产]南大通用数据库
     */
    GBASE("Gbase", DataBaseGroupEnum.JDBC,"南大通用数据库", "com.gbase.jdbc.Driver", "/* ping */ SELECT 1", "jdbc:gbase://{host}:{port}/{database}"),

    /**
     * HIVE数据库
     */
    HIVE("Hive", DataBaseGroupEnum.JDBC,"Hive", "org.apache.hive.jdbc.HiveDriver", "SELECT 1", "jdbc:hive2://{host}:{port}/{database}"),

    /**
     * Doris数据库
     */
    DORIS("Doris", DataBaseGroupEnum.JDBC,"Doris", "com.mysql.jdbc.Driver", "/* ping */ SELECT 1", "jdbc:mysql://{host}:{port}/{database}?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true"),

    /**
     * Clickhouse
     */
    CLICKHOUSE("Clickhouse", DataBaseGroupEnum.JDBC, "Clickhouse", "com.clickhouse.jdbc.ClickHouseDriver", "SELECT 1", "jdbc:clickhouse://{host}:{port}/{database}"),

    /**
     * Mongodb
     */
    MONGODB("Mongodb", DataBaseGroupEnum.NOSQL,"Mongodb", "com.gitee.jdbc.mongodb.JdbcDriver", "use admin;", "jdbc:mongodb://{host}:{port}/{database}?authSource=admin"),
    ;

    private String product;
    private DataBaseGroupEnum group;
    private String describe;
    private String driveClassName;
    private String testSql;
    private String url;

    DataBaseEnum(String product, DataBaseGroupEnum group, String describe, String driveClassName, String testSql, String url) {
        this.product = product;
        this.group = group;
        this.describe = describe;
        this.driveClassName = driveClassName;
        this.testSql = testSql;
        this.url = url;
    }

    public static DataBaseEnum getByProduct(String product){
        return Arrays.stream(DataBaseEnum.values()).filter(DataBaseEnum -> DataBaseEnum.getProduct().equalsIgnoreCase(product)).findFirst().orElse(DataBaseEnum.UNKNOWN);
    }

}
