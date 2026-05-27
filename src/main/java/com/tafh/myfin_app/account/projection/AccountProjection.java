package com.tafh.myfin_app.account.projection;

import java.math.BigDecimal;

public interface AccountProjection {

    String getId();
    String getName();
    BigDecimal getOpeningBalance();
    BigDecimal getCurrentBalance();
    boolean getActive();
    long getUsageCount();

}
