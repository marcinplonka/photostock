package pl.com.bottega.photostock.sales.misc;

import pl.com.bottega.photostock.sales.infrastructure.repositories.CSVProductRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.InMemoryClientRepository;
import pl.com.bottega.photostock.sales.model.Client;
import pl.com.bottega.photostock.sales.model.Money;
import pl.com.bottega.photostock.sales.model.Picture;
import pl.com.bottega.photostock.sales.model.Product;
import pl.com.bottega.photostock.sales.model.repositories.ClientRepository;
import pl.com.bottega.photostock.sales.model.repositories.ProductRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CSVProductRepositoryTest {



    public static void main(String[] args) {

        Set<String> tags = new HashSet<>();
        tags.add("kotki");
        tags.add("pieski");
        Product p1 = new Picture(1L, tags, Money.valueOf(10));
        Product p2 = new Picture(2L, tags, Money.valueOf(5));
        Product p3 = new Picture(3L, tags, Money.valueOf(15));

        ClientRepository clientRepository = new InMemoryClientRepository();
        ProductRepository productRepository = new CSVProductRepository(
                "/home/marcin/products.csv",
                clientRepository);
        productRepository.save(p1);
        productRepository.save(p2);
        productRepository.save(p3);
        Product product3 = productRepository.get(1L);
        Product product2 = productRepository.get(2L);
        Product product1 = productRepository.get(3L);

        System.out.println(product1.getNumber());
        System.out.println(product2.getNumber());
        System.out.println(product3.getNumber());
    }

}
