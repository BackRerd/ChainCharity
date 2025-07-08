package site.backrer.backed.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MoneyDonateStats {
    BigDecimal totalAmount;
    Long approvedCount;
    Long pendingCount;
    Long rejectedCount;
}
