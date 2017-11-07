package pl.com.bottega.photostock.sales.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
@Entity
public class Transaction {

    @Id
    @GeneratedValue
    private Long clientId;
    @Column(name = "cents")
    private Long cents;
    @Column(name = "currency")
    private String currency;
    @Column(name = "description")
    private String description;
    @Column(name = "date_time")
    private LocalDateTime dateTime = LocalDateTime.now();

    public Transaction(Money amount, String description) {
        this.cents = amount.value();
        this.currency = amount.currency();
        this.description = description;
    }

    public Transaction() {
    }

    public Money getAmount() {
        return Money.valueOf(cents, currency);
    }
}
