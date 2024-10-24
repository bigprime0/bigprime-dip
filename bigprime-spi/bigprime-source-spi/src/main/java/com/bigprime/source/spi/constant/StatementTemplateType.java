package com.bigprime.source.spi.constant;

public enum StatementTemplateType {
    DB_STATEMENT {
        @Override
        public String getDefault() {
            return "SELECT\n" +
                    "    CATALOG_NAME AS \"catalog\",\n" +
                    "    SCHEMA_NAME AS \"name\",\n" +
                    "    DEFAULT_CHARACTER_SET_NAME AS \"characterSet\",\n" +
                    "    DEFAULT_COLLATION_NAME AS \"collation\"\n" +
                    "FROM\n" +
                    "    INFORMATION_SCHEMA.SCHEMATA";
        }
    },
    TABLE_STATEMENT {
        @Override
        public String getDefault() {
            return "SELECT\n" +
                    "    TABLE_CATALOG AS \"catalog\",\n" +
                    "    TABLE_SCHEMA AS \"schema\",\n" +
                    "    TABLE_NAME AS \"name\",\n" +
                    "    CASE TABLE_TYPE\n" +
                    "        WHEN 'BASE TABLE' THEN 'TABLE'\n" +
                    "        WHEN 'VIEW' THEN 'VIEW'\n" +
                    "        WHEN 'SYSTEM VIEW' THEN 'SYSTEM'\n" +
                    "    END AS \"type\",\n" +
                    "    ENGINE AS \"engine\",\n" +
                    "    ROW_FORMAT AS \"format\",\n" +
                    "    TABLE_ROWS AS \"rows\",\n" +
                    "    CREATE_TIME AS \"createTime\",\n" +
                    "    UPDATE_TIME AS \"updateTime\",\n" +
                    "    TABLE_COLLATION AS \"collation\",\n" +
                    "    TABLE_COMMENT AS \"comment\",\n" +
                    "    AVG_ROW_LENGTH AS \"avgRowLength\",\n" +
                    "    DATA_LENGTH AS \"dataLength\",\n" +
                    "    INDEX_LENGTH AS \"indexLength\"\n" +
                    "FROM\n" +
                    "    information_schema.TABLES\n";
        }
    },
    COLUMN_STATEMENT {
        @Override
        public String getDefault() {
            return "SELECT\n" +
                    "    TABLE_NAME AS \"tableName\",\n" +
                    "    COLUMN_NAME AS \"column\",\n" +
                    "    COLUMN_DEFAULT AS \"defaultValue\",\n" +
                    "    ORDINAL_POSITION AS \"position\",\n" +
                    "    CASE IS_NULLABLE\n" +
                    "        WHEN 'YES' THEN 'true'\n" +
                    "        WHEN 'NO' THEN 'false'\n" +
                    "    END AS \"isNullable\",\n" +
                    "    DATA_TYPE AS \"dataType\",\n" +
                    "    EXTRA AS \"extra\",\n" +
                    "    CHARACTER_MAXIMUM_LENGTH AS \"maximumLength\",\n" +
                    "    NUMERIC_PRECISION AS \"precision\",\n" +
                    "    NUMERIC_SCALE AS \"scale\",\n" +
                    "    COLLATION_NAME AS \"collation\",\n" +
                    "\t\tCASE COLUMN_KEY\n" +
                    "        WHEN 'PRI' THEN 'true'\n" +
                    "        WHEN 'AGG' THEN 'true'\n" +
                    "\t\t\t\tELSE 'false'\n" +
                    "    END AS \"isKey\",\n" +
                    "    PRIVILEGES AS \"privileges\",\n" +
                    "    COLUMN_COMMENT AS \"comment\"\n" +
                    "FROM\n" +
                    "    information_schema.COLUMNS\n";
        }
    },
    INDEX_STATEMENT {
        @Override
        public String getDefault() {
            return "SELECT TABLE_NAME as \"tableName\",  \n" +
                    "   INDEX_NAME as \"indexName\",  \n" +
                    " COLUMN_NAME as \"columnName\",  \n" +
                    " INDEX_TYPE as \"indexType\",  \n" +
                    " INDEX_COMMENT as \"indexComment\",  \n" +
                    " NON_UNIQUE as \"nonUnique\",  \n" +
                    " SEQ_IN_INDEX as \"seqIndex\"\n" +
                    "FROM INFORMATION_SCHEMA.STATISTICS  \n";
        }
    },
    VIEW_STATEMENT {
        @Override
        public String getDefault() {
            return "SELECT table_name as \"viewName\",\n" +
                    "\t\t\t view_definition as \"viewDefinition\",\n" +
                    "\t\t\t check_option as \"checkOption\",\n" +
                    "\t\t\t is_updatable as \"isUpdatable\",\n" +
                    "\t\t\t character_set_client as \"characterSet\",\n" +
                    "\t\t\t collation_connection as \"collation\"\n" +
                    "FROM information_schema.VIEWS  ";
        }
    },
    FUNCTION_STATEMENT {
        @Override
        public String getDefault() {
            return "SELECT ROUTINE_NAME as \"functionName\",\n" +
                    "\t\tROUTINE_DEFINITION as \"functionDefinition\",\n" +
                    "\t\tDTD_IDENTIFIER as \"identifier\"\n" +
                    "FROM information_schema.ROUTINES  \n" +
                    "WHERE\tROUTINE_TYPE = 'FUNCTION'";
        }
    };

    public abstract String getDefault();
}
