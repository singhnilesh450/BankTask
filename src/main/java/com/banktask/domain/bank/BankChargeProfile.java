package com.banktask.domain.bank;

import java.math.BigDecimal;
import java.util.Objects;

public record BankChargeProfile(BigDecimal rtgsCharge, BigDecimal impsCharge) {
    public BankChargeProfile {
        Objects.requireNonNull(rtgsCharge, "rtgsCharge");
        Objects.requireNonNull(impsCharge, "impsCharge");
    }
}
