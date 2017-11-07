package pl.com.bottega.photostock.sales.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Address implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "line_1")
    private String line1;
    @Column(name = "line_2")
    private String line2;
    @Column(name = "country")
    private String country;
    @Column(name = "city")
    private String city;
    @Column(name = "postal_code")
    private String postalCode;

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

    public Address() {
    }

    @Override
    public String toString() {
                return "address id: "+id+"\n"+line1+"\n"+line2+"\n"+country+"\n"+city+"\n"+postalCode;
    }
}
