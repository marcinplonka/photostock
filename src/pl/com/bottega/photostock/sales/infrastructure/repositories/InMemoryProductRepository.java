package pl.com.bottega.photostock.sales.infrastructure.repositories;

import java.util.*;
import java.util.stream.Collectors;

import pl.com.bottega.photostock.sales.model.Money;
import pl.com.bottega.photostock.sales.model.Picture;
import pl.com.bottega.photostock.sales.model.repositories.ProductRepository;
import pl.com.bottega.photostock.sales.model.Product;
import pl.com.bottega.photostock.sales.model.Client;

public class InMemoryProductRepository implements ProductRepository {

    private static final Map<Long, Product> REPO;

    static {
        REPO = new HashMap<>();
        Set<String> tags = new HashSet<>();
        tags.add("kotki");
        Product p1 = new Picture(1L, tags, Money.valueOf(10));
        Product p2 = new Picture(2L, tags, Money.valueOf(5));
        Product p3 = new Picture(3L, tags, Money.valueOf(15));
        REPO.put(1L, p1);
        REPO.put(2L, p2);
        REPO.put(3L, p3);
    }

    @Override
    public Product get(Long number) {
        if (!REPO.containsKey(number))
            throw new IllegalArgumentException("No such object in repository");
        return REPO.get(number);
    }

    @Override
    public Optional<Product> getOptional(Long number) {
        if (REPO.containsKey(number))
            return Optional.of(REPO.get(number));
        else
            return Optional.empty();
    }


    @Override
    public void save(Product product) {
        REPO.put(product.getNumber(), product);
    }

    @Override
    public List<Product> find(Client client, Set<String> tags, Money from, Money to) {
        return REPO.values().stream().filter((product) -> {
            if (product instanceof Picture) {
                Picture picture = (Picture) product;
                return matchesCriteria(picture, client, tags, from, to);
            }
            return false;
        }).collect(Collectors.toList());
    }

    private boolean matchesCriteria(Picture picture, Client client, Set<String> tags, Money from, Money to) {
        if (tags != null && !picture.hasTags(tags))
            return false;

        Money price = picture.calculatePrice(client);

        if (from != null && from.gt(price))
            return false;

        if (to != null && to.lt(price))
            return false;

        return true;
    }
}
