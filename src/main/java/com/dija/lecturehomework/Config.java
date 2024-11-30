package com.dija.lecturehomework;

import com.oanda.v20.account.AccountID;

public class Config {
    private Config() {}

    public static final String URL = "https://api-fxpractice.oanda.com"; // URL de prática
    public static final String TOKEN = "5f5036f59934645111ab53c85b6149d9-44e5618b7dd38fe118681d87b77717d7"; // Substitua por seu token gerado na OANDA
    public static final AccountID ACCOUNTID = new AccountID("101-004-30416899-001"); // Substitua pelo seu ID da conta
}
