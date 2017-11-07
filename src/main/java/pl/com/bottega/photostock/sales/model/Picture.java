package pl.com.bottega.photostock.sales.model;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
@Entity
public class Picture extends Product {

    @Column
    @OneToMany
    private Set<Tag> tags;

    public Picture(Set<String> tags, Money price) {
        this(tags, price, true);
    }

    public Picture(Set<String> tags, Money price, Boolean active) {
        super(price, active);
        this.tags = tags.stream().map(Tag::new).collect(Collectors.toSet());
    }

    public Picture(String[] tags, Money price, Client reservedBy, Client owner, boolean active) {
        super(price, reservedBy, owner, active);
        this.tags = Arrays.stream(tags).map(Tag::new).collect(Collectors.toSet());
    }

    public Picture() {
    }

    public boolean hasTags(Set<String> tags) {
        return this.tags.containsAll(tags);
    }

    public Set<String> getTags() {
        return tags.stream().map(Tag::getTag).collect(Collectors.toSet());
    }


}
