package pl.com.bottega.photostock.sales.infrastructure.repositories;

import pl.com.bottega.photostock.sales.model.Client;
import pl.com.bottega.photostock.sales.model.Money;
import pl.com.bottega.photostock.sales.model.Picture;
import pl.com.bottega.photostock.sales.model.Product;
import pl.com.bottega.photostock.sales.model.repositories.ClientRepository;
import pl.com.bottega.photostock.sales.model.repositories.ProductRepository;

import java.io.*;
import java.util.*;

public class CSVProductRepository implements ProductRepository {

    private String path;
    private ClientRepository clientRepository;

    public CSVProductRepository(String path, ClientRepository clientRepository) {
        this.path = path;
        this.clientRepository = clientRepository;
    }

    @Override
    public Product get(Long number) {
        return getOptional(number).orElseThrow(() -> new IllegalArgumentException("No such product in repo"));
    }

    private Client findClient(String number) {
        if (number.equals("null"))
            return null;
        else
            return clientRepository.get(number);
    }

    @Override
    public Optional<Product> getOptional(Long number) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                Product product = convertToProduct(line);
                if (product.getNumber().equals(number))
                    return Optional.of(product);
            }
            return Optional.empty();
        } catch (FileNotFoundException e) {
            return Optional.empty();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Product convertToProduct(String line) {
        String[] lineSplit = line.split(",");
        Long nr = Long.parseLong(lineSplit[0]);
        String[] tags = lineSplit[1].split(";");
        String[] valueAndCurrency = lineSplit[2].split(" ");
        Money price = Money.valueOf(Double.parseDouble(valueAndCurrency[0]), valueAndCurrency[1]);
        boolean active = Boolean.valueOf(lineSplit[3]);
        String reservedByNumber = lineSplit[4];
        String ownerNumber = lineSplit[5];
        return new Picture(
                nr,
                tags,
                price,
                clientRepository.get(reservedByNumber),
                clientRepository.get(ownerNumber),
                active
        );
    }


    @Override
    public void save(Product product) {
        Map<Long, Product> products = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            if (br.ready()) {
                br
                        .lines()
                        .map(this::convertToProduct)
                        .forEach(p -> products.put(p.getNumber(), p));
            }
            products.put(product.getNumber(), product);

        } catch (FileNotFoundException e) {
            System.out.println("CSV file not found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (OutputStream outputStream = new FileOutputStream(path, false);
             PrintStream printStream = new PrintStream(outputStream)) {
            for (Product p : products.values()) {
                printStream.println(String.join(",", toLine(p)));
            }
            printStream.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> find(Client client, Set<String> tags, Money from, Money to) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            List<Product> results = new LinkedList<>();
            String line;
            while ((line = br.readLine()) != null) {
                Product product = convertToProduct(line);
                if (product instanceof Picture) {
                    Picture picture = (Picture) product;
                    if (matchesCriteria(picture, client, tags, from, to))
                        results.add(picture);
                }
            }
            if (results.size() == 0)
                System.out.println("Niczego nie znaleziono. Zmień kryteria wyszukiwania");
            return results;
        } catch (FileNotFoundException e) {
            return Collections.emptyList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    private String[] toLine(Product p) {
        if (p instanceof Picture) {
            Picture pic = (Picture) p;
            return new String[] {
                    pic.getNumber().toString(),
                    String.join(";", pic.getTags()),
                    pic.getPrice().toString(),
                    pic.getActive().toString(),
                    pic.getReservedByNumber(),
                    pic.getOwnerNumber()
            };
        }
        return new String[6];
    }
}
