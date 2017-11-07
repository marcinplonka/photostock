package pl.com.bottega.photostock.sales.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class Clip extends Product implements Serializable {

    @Column(name = "length")
    private Long length;

    public Clip(Money price, Boolean active, Long length) {
        super(price, active);
        this.length = length;
    }

    public Clip(Money price, Long length) {
        this(price, true, length);
    }

    public Clip() {
    }
}
