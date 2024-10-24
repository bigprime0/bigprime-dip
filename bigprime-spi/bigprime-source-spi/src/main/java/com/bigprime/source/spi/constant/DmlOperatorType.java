package com.bigprime.source.spi.constant;

public enum DmlOperatorType
{
    EQ("="),
    NEQ("<>"),
    GT(">"),
    GTE(">="),
    LT("<"),
    LTE("<="),
    LIKE("LIKE"),
    NLIKE("NOT LIKE"),
    NULL("IS NULL"),
    NNULL("IS NOT NULL");

    private final String symbol;

    DmlOperatorType(String symbol)
    {
        this.symbol = symbol;
    }

    public String getSymbol()
    {
        return symbol;
    }
}
