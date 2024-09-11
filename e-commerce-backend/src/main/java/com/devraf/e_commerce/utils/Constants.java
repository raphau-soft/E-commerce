package com.devraf.e_commerce.utils;

import java.util.Map;

public class Constants {
    public static int VALID_TIME_TOKEN_MINUTES = 1000 * 60 * 30;
    public static int VALID_TIME_REFRESH_TOKEN_MINUTES = 1000 * 60 * 60;
    public static int VALID_TIME_CONFIRM_ACCOUNT_TOKEN_MINUTES = 1000 * 60 * 60 * 24;
    public static int VALID_TIME_RESET_PASSWORD_TOKEN_MINUTES = 1000 * 60 * 15;

    public static final Map<TokenEnum, Integer> EXPIRATION_TIMES = Map.of(
            TokenEnum.AUTH, VALID_TIME_TOKEN_MINUTES,
            TokenEnum.REFRESH, VALID_TIME_REFRESH_TOKEN_MINUTES,
            TokenEnum.RESET_PASSWORD, VALID_TIME_RESET_PASSWORD_TOKEN_MINUTES,
            TokenEnum.CONFIRM_ACCOUNT, VALID_TIME_CONFIRM_ACCOUNT_TOKEN_MINUTES
    );
}
