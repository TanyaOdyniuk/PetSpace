package com.netcracker.service.currency;

import java.math.BigDecimal;

public interface CurrencyService {
    String increaseBalance(BigDecimal money);

    String currencySpend(BigDecimal money);

    boolean checkBalance(BigDecimal money);

    String increaseBalanceMoney(BigDecimal money);

    int checkAmount();

    String checkCard();

    BigDecimal changeRate(BigDecimal rate);
}
