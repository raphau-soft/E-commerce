package com.devraf.e_commerce.utils;

import java.util.Map;

public class Constants {
    private static final int MINUTE = 1000 * 60;
    private static final int HOUR = MINUTE * 60;
    private static final int DAY = HOUR * 24;

    public static long VALID_TIME_TOKEN_MINUTES = 30 * MINUTE;
    public static long VALID_TIME_REFRESH_TOKEN_MINUTES = HOUR;
    public static long VALID_TIME_CONFIRM_ACCOUNT_TOKEN_MINUTES = 24 * HOUR;
    public static long VALID_TIME_RESET_PASSWORD_TOKEN_MINUTES = 15 * MINUTE;
    public static long VALID_TIME_CSRF_TOKEN_MINUTES = HOUR;
    public static long VALID_TIME_REMEMBER_ME_TOKEN_DAYS = 30L * DAY;

    public static final Map<TokenEnum, Long> EXPIRATION_TIMES = Map.of(
            TokenEnum.AUTH_TOKEN, VALID_TIME_TOKEN_MINUTES,
            TokenEnum.REFRESH_TOKEN, VALID_TIME_REFRESH_TOKEN_MINUTES,
            TokenEnum.RESET_PASSWORD_TOKEN, VALID_TIME_RESET_PASSWORD_TOKEN_MINUTES,
            TokenEnum.CONFIRM_ACCOUNT_TOKEN, VALID_TIME_CONFIRM_ACCOUNT_TOKEN_MINUTES,
            TokenEnum.CSRF_TOKEN, VALID_TIME_CSRF_TOKEN_MINUTES,
            TokenEnum.REMEMBER_ME_TOKEN, VALID_TIME_REMEMBER_ME_TOKEN_DAYS
    );
}
