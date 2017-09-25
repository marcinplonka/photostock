package pl.com.bottega.photostock.sales.misc;

import pl.com.bottega.photostock.sales.infrastructure.repositories.CSVProductRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.InMemoryClientRepository;
import pl.com.bottega.photostock.sales.model.Product;
import pl.com.bottega.photostock.sales.model.repositories.ProductRepository;

public class CSVProductRepositoryTest {

    public static void main(String[] args) {
        ProductRepository productRepository = new CSVProductRepository(
                "/home/maciuch/products.csv",
                new InMemoryClientRepository());
        Product product = productRepository.get(3L);
        System.out.println(product.getNumber());
    }

}
