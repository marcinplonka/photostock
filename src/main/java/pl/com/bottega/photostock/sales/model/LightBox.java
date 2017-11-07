package pl.com.bottega.photostock.sales.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


@Entity
@Table
public class LightBox implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private String number;
    @Column(name = "name")
    private String name;
    @OneToMany
    private List<Picture> items = new LinkedList<>();

    private Client owner;

    public LightBox(Client owner, String name) {
        this.owner = owner;
        this.name = name;
    }

    public LightBox(Client owner, String name,  String number, List<Picture> items) {
        this(owner, name);
        this.number = number;
        this.items = items;
    }

    public LightBox(Client owner, String name, String number) {
        this(owner, name);
        this.number = number;
    }

    public LightBox() {
    }

    public void add(Picture pciture) {
        if(items.contains(pciture))
            throw new IllegalStateException("Product already added");
        pciture.ensureAvailable(owner);
        items.add(pciture);
    }

    public void remove(Picture picture) {
        if(!items.remove(picture))
            throw new IllegalArgumentException("Product not added to lightbox");
    }

    public String getName() {
        return name;
    }

    public Client getOwner() {
        return owner;
    }

    public List<IProduct> getItems() {
        return Collections.unmodifiableList(items);
    }

    public String getNumber() {
        return number;
    }

    public List<Picture> getPictures(Set<Long> pictureNumbers) {
        List<Picture> results = new LinkedList<>();
        for(Picture pic : items)
            if(pictureNumbers.contains(pic.getNumber()))
                results.add(pic);
        return results;
    }
}
