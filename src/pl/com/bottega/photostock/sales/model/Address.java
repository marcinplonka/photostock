package pl.com.bottega.photostock.sales.model;

public class Address {

    private String line1, line2, country, city, postalCode;

    public Address(String line1, String line2, String country, String city, String postalCode) {
        this.line1 = line1;
        this.line2 = line2;
        this.country = country;
        this.city = city;
        this.postalCode = postalCode;
    }

    public Address(String line1, String country, String city, String postalCode) {
        this(line1, null, country, city, postalCode);
    }
}
