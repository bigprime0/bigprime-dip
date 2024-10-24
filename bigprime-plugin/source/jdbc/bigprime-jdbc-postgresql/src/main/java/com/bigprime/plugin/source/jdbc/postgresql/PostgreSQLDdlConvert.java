package com.bigprime.plugin.source.jdbc.postgresql;

import cn.hutool.core.util.StrUtil;
import com.bigprime.source.spi.constant.StatementTemplateType;
import com.bigprime.source.spi.internals.AbstractConvertDdlStatement;
import com.bigprime.source.spi.model.ConvertDdlConfig;
import com.bigprime.source.spi.model.ConvertDmlConfig;
import com.bigprime.source.spi.model.SafeAppendable;

/**
 * @author lyw
 * @version 1.0
 */
public class PostgreSQLDdlConvert extends AbstractConvertDdlStatement {


    protected PostgreSQLDdlConvert(ConvertDdlConfig config) {
        super(config);
    }


    @Override
    protected void getDbStatementBuild(SafeAppendable builder) {
        String template = "SELECT\n" +
                "    catalog_name AS \"catalog\",\n" +
                "    schema_name AS \"name\"\n" +
                "FROM\n" +
                "    information_schema.schemata;";
        builder.append(template);
    }

    @Override
    protected void getTableStatementBuild(SafeAppendable builder) {
        String template = "SELECT t.table_catalog                                        AS \"catalog\"\n" +
                "     , n.nspname                                                 AS \"schema\"\n" +
                "     , c.relname                                                 AS \"name\"\n" +
                "     , CASE t.table_type\n" +
                "           WHEN 'BASE TABLE' THEN 'TABLE'\n" +
                "           WHEN 'VIEW' THEN 'VIEW'\n" +
                "           WHEN 'MATERIALIZED VIEW' THEN 'MATERIALIZED VIEW' END AS \"type\"\n" +
                "     , NULL                                                      AS \"format\"\n" +
                "     , NULL                                               AS \"rows\"\n" +
                "     , NULL                                                      AS \"createTime\"\n" +
                "     , NULL                                                      AS \"updateTime\"\n" +
                "     , NULL                                                      AS \"collation\"\n" +
                "     , obj_description(c.oid)                                    AS \"comment\"\n" +
                "     , NULL                                                      AS \"avgRowLength\"\n" +
                "     , NULL                                                      AS \"dataLength\"\n" +
                "     , NULL                                                      AS \"indexLength\"\n" +
                "FROM pg_class c\n" +
                "         LEFT JOIN pg_namespace n ON n.oid = c.relnamespace\n" +
                "         LEFT JOIN information_schema.tables t ON t.table_schema = n.nspname and t.table_name = c.relname\n" +
                "WHERE ((c.relkind = 'r'::\"char\") OR (c.relkind = 'f'::\"char\") OR (c.relkind = 'p'::\"char\"))\n";
        builder.append(template);
        if (StrUtil.isNotBlank(config.getDatabase())) {
            builder.append(String.format("  AND n.nspname = '%s'\n", config.getDatabase()));
            if (StrUtil.isNotBlank(config.getTableName())) {
                builder.append(String.format("  AND c.relname = '%s'\n", config.getTableName()));
            }
        } else if (StrUtil.isNotBlank(config.getTableName())) {
            builder.append(String.format("  AND c.relname = '%s'\n", config.getTableName()));
        }
        builder.append("ORDER BY n.nspname, name");

    }

    @Override
    protected void getColumnStatementBuild(SafeAppendable builder) {
        String template = "SELECT col.column_name                                                as \"column\"\n" +
                "     , col.table_name                                                 as \"tableName\"\n" +
                "     , col.column_default                                             as \"defaultValue\"\n" +
                "     , col.ordinal_position                                           as \"position\"\n" +
                "     , CASE WHEN col.is_nullable = 'YES' THEN 'true' ELSE 'false' END as \"isNullable\"\n" +
                "     , col.udt_name                                                   as \"dataType\"\n" +
                "     , NULL                                                           as \"extra\"\n" +
                "     , COALESCE(col.character_maximum_length, datetime_precision)     as \"maximumLength\"\n" +
                "     , col.numeric_precision                                          as \"precision\"\n" +
                "     , col.numeric_scale                                              as \"scale\"\n" +
                "     , col.collation_name                                             as \"collation\"\n" +
                "     , (CASE\n" +
                "            WHEN (SELECT COUNT(*)\n" +
                "                  FROM pg_constraint AS PC\n" +
                "                  WHERE b.attnum\n" +
                "                      = ANY (PC.conkey)\n" +
                "                    AND PC.contype = 'p'\n" +
                "                    and PC.conrelid = c.oid) > 0\n" +
                "                THEN 'true'\n" +
                "            ELSE 'false' END)                                         as \"isKey\"\n" +
                "     , NULL                                                           as \"privileges\"\n" +
                "     , col_description(c.oid, col.ordinal_position)                   as \"comment\"\n" +
                "FROM information_schema.columns AS col\n" +
                "         LEFT JOIN pg_namespace ns ON ns.nspname = col.table_schema\n" +
                "         LEFT JOIN pg_class c ON col.table_name = c.relname AND c.relnamespace = ns.oid\n" +
                "         LEFT JOIN pg_attribute b ON b.attrelid = c.oid AND b.attname = col.column_name\n" +
                "WHERE col.table_schema = '${database:String}'\n" +
                "  AND col.table_name = '${table:String}'\n" +
                "ORDER BY col.table_schema, col.table_name, col.ordinal_position";
        builder.append(template);
        if (StrUtil.isNotBlank(config.getDatabase())) {
            builder.append(String.format("WHERE col.table_schema = '%s'\n", config.getDatabase()));
            if (StrUtil.isNotBlank(config.getTableName())) {
                builder.append(String.format("  AND col.table_name = '%s'\n", config.getTableName()));
            }
        } else if (StrUtil.isNotBlank(config.getTableName())) {
            builder.append(String.format("  AND col.table_name = '%s'\n", config.getTableName()));
        }
        builder.append("ORDER BY col.table_schema, col.table_name, col.ordinal_position");
    }

}
