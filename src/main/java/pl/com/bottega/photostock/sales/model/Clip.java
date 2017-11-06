package pl.com.bottega.photostock.sales.model;

public class Clip extends AbstractProduct {

    private Long length;

    public Clip(Money price, Boolean active, Long number, Long length) {
        super(price, active, number);
        this.length = length;
    }

    public Clip(Money price, Long number, Long length) {
        this(price, true, number, length);
    }

}
