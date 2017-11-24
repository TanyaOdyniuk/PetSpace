package com.netcracker.service.currency;

public interface CurrencyService {
    String increaseBalance(double money);

    String currencySpend(double money);

    boolean CheckBalance(double money);

    String increaseBalanceMoney(double money);

    int checkAmount();

    String checkCard();

    double changeRate(double rate);
}
