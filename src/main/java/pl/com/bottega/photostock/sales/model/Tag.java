package pl.com.bottega.photostock.sales.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Tag {

    @Id
    private String tag;

    public Tag() {
    }

    public Tag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
