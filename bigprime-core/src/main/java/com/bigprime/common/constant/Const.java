package com.bigprime.common.constant;

/**
 * @author lyw
 * @version 1.0
 */
public final class Const {

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    public static final String PATH_SEPARATOR = System.getProperty("path.separator");

    public static final String CR = System.getProperty("line.separator");

    public static final String DOCS = "\n\r";

    public static final String EMPTY_STRING = "";

    public static final String JAVA_VERSION = System.getProperty("java.vm.version");

    public static final String CREATE_TABLE = "CREATE TABLE ";

    public static final String DROP_TABLE = "DROP TABLE ";

    public static final String IF_NOT_EXISTS = "IF NOT EXISTS ";

    public static final String IF_EXISTS = "IF EXISTS ";

    private Const() {
    }
}
