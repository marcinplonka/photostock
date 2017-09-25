package pl.com.bottega.photostock.sales.model;

import java.time.LocalDateTime;

public class Transaction {

    private Money amount;
    private String description;
    private LocalDateTime dateTime = LocalDateTime.now();

    public Transaction(Money amount, String description) {
        this.amount = amount;
        this.description = description;
    }

    public Money getAmount() {
        return amount;
    }
}
