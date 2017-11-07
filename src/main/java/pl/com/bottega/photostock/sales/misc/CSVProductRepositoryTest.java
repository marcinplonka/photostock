package pl.com.bottega.photostock.sales.misc;

import pl.com.bottega.photostock.sales.infrastructure.repositories.csvrepositories.CSVClientRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.csvrepositories.CSVProductRepository;
import pl.com.bottega.photostock.sales.model.Money;
import pl.com.bottega.photostock.sales.model.Picture;
import pl.com.bottega.photostock.sales.model.IProduct;
import pl.com.bottega.photostock.sales.model.repositories.ClientRepository;
import pl.com.bottega.photostock.sales.model.repositories.ProductRepository;

import java.util.HashSet;
import java.util.Set;

public class CSVProductRepositoryTest {



    public static void main(String[] args) {

        Set<String> tags = new HashSet<>();
        tags.add("kotki");
        tags.add("pieski");
        IProduct p1 = new Picture(tags, Money.valueOf(10));
        IProduct p2 = new Picture(tags, Money.valueOf(5));
        IProduct p3 = new Picture(tags, Money.valueOf(15));

        ClientRepository clientRepository = new CSVClientRepository("/home/marcin/repo/clients.csv");
        ProductRepository productRepository = new CSVProductRepository(
                "/home/marcin/products.csv",
                clientRepository);
        productRepository.save(p1);
        productRepository.save(p2);
        productRepository.save(p3);
        IProduct product3 = productRepository.get(1L);
        IProduct product2 = productRepository.get(2L);
        IProduct product1 = productRepository.get(3L);

        System.out.println(product1.getNumber());
        System.out.println(product2.getNumber());
        System.out.println(product3.getNumber());
    }

}
