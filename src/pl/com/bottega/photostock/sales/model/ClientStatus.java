package pl.com.bottega.photostock.sales.model;

public enum ClientStatus {

    STANDARD(0), VIP(0), SILVER(5), GOLD(10), PLATINUM(15);

    private int discountPercent;

    ClientStatus(int discount) {
        this.discountPercent = discount;
    }

    public int discountPercent() {
        return discountPercent;
    }

}
