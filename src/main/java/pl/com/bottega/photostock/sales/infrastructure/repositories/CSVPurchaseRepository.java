package pl.com.bottega.photostock.sales.infrastructure.repositories;

import pl.com.bottega.photostock.sales.model.Client;
import pl.com.bottega.photostock.sales.model.Product;
import pl.com.bottega.photostock.sales.model.Purchase;
import pl.com.bottega.photostock.sales.model.repositories.CSVRepository;
import pl.com.bottega.photostock.sales.model.repositories.ClientRepository;
import pl.com.bottega.photostock.sales.model.repositories.ProductRepository;
import pl.com.bottega.photostock.sales.model.repositories.PurchaseRepository;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class CSVPurchaseRepository implements PurchaseRepository, CSVRepository {

    private String repoDirectoryPath;
    private ClientRepository clientRepository;
    private ProductRepository productRepository;
    private final String FILE_NAME = "-transactions.csv";

    public CSVPurchaseRepository(String repoDirectoryPath, ClientRepository clientRepository, ProductRepository productRepository) {
        this.repoDirectoryPath = repoDirectoryPath;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void save(Purchase purchase) {
        String clientNumber = purchase.getBuyer().getNumber();
        String path = repoDirectoryPath+clientNumber+FILE_NAME;
        Map<String, Purchase> purchases = new HashMap<>();
        if (fileExists(path))
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            purchases = br
                    .lines()
                    .map(this::toObject)
                    .collect(Collectors.toMap(Purchase::getNumber, p -> p));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        purchases.put(purchase.getNumber(), purchase);

        try (OutputStream outputStream = new FileOutputStream(repoDirectoryPath + clientNumber + FILE_NAME, false);
             PrintStream printStream = new PrintStream(outputStream)) {
            for (Purchase p : purchases.values()) {
                printStream.println(String.join(",", toLine(p)));
            }
            printStream.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Purchase get(String number, String clientNumber) {
        try (BufferedReader br = new BufferedReader(new FileReader(repoDirectoryPath + clientNumber + FILE_NAME))) {
            return br.lines().map(this::toObject)
                    .filter(p -> p.getNumber().equals(number)).findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Purchase nr %s not found", number)));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String[] toLine(Purchase purchase) {
        return new String[]{
                purchase.getNumber(),
                String.join(";", purchase.getItems()
                        .stream()
                        .map(p -> p.getNumber().toString()).collect(Collectors.toList())),
                purchase.getBuyer().getNumber(),
                purchase.getPurchaseDate().toString()
        };

    }

    private Purchase toObject(String CSVLine) {
        String[] linesplit = CSVLine.split(",");
        Collection<Product> items = new HashSet<>();
        String number = linesplit[0];
        Arrays.stream(linesplit[1].split(";"))
                .forEach(n -> items.add(productRepository.get(Long.parseLong(n))));
        Client client = clientRepository.get(linesplit[2]);
        LocalDateTime dateTime = LocalDateTime.parse(linesplit[3]);
        return new Purchase(
                number,
                items,
                client,
                dateTime
        );
    }
}
