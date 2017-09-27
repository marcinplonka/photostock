package pl.com.bottega.photostock.sales.model;

import java.util.*;

public class LightBox {

    private String name;
    private List<Picture> items = new LinkedList<>();

    private Client owner;
    private String number;

    public LightBox(Client owner, String name) {
        this.owner = owner;
        this.name = name;
        this.number = UUID.randomUUID().toString();
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

    public void add(Picture pciture) {
        if(items.contains(pciture))
            throw new IllegalStateException("Product already added");
        pciture.ensureAvailable();
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

    public List<Product> getItems() {
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
