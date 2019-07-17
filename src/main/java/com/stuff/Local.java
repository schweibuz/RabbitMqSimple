package com.stuff;

public abstract class Local {

    private static final String EXCHANGE_NAME = "logs";
    private static final String HOST = "localhost";

    public static String getExchangeName() {
        return EXCHANGE_NAME;
    }

    public static String getHOST() {
        return HOST;
    }

}
