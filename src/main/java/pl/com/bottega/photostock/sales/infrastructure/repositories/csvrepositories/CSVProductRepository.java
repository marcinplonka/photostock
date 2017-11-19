package pl.com.bottega.photostock.sales.infrastructure.repositories.csvrepositories;
import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.repositories.CSVRepository;
import pl.com.bottega.photostock.sales.model.repositories.ClientRepository;
import pl.com.bottega.photostock.sales.model.repositories.ProductRepository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CSVProductRepository implements ProductRepository, CSVRepository {

    private String repoDirectoryPath;
    private ClientRepository clientRepository;
    private String path;
    private final String FILE_NAME = "products.csv";

    public CSVProductRepository(String repoDirectoryPath, ClientRepository clientRepository) {
        this.repoDirectoryPath = repoDirectoryPath;
        this.clientRepository = clientRepository;
        this.path = repoDirectoryPath+FILE_NAME;
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
        try (BufferedReader br = new BufferedReader(new FileReader(repoDirectoryPath+FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                Product product = toObject(line);
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


    @Override
    public void save(Product product) {
        Map<Long, IProduct> products = new HashMap<>();
        if (fileExists(path))
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            if (br.ready()) {
                products = br
                        .lines()
                        .map(this::toObject)
                        .collect(Collectors.toMap(IProduct::getNumber, p -> p));
            }
            products.put(product.getNumber(), product);

        } catch (FileNotFoundException e) {
            System.out.println("CSV file not found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (OutputStream outputStream = new FileOutputStream(path, false);
             PrintStream printStream = new PrintStream(outputStream)) {
            for (IProduct p : products.values()) {
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
                Product product = toObject(line);
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

        if (!picture.isActive() && !picture.getReservedByNumber().equals(client.getNumber()))
            return false;

        if (tags != null && !picture.hasTags(tags))
            return false;

        Money price = picture.calculatePrice(client);

        if (from != null && from.gt(price))
            return false;

        if (to != null && to.lt(price))
            return false;

        return true;
    }

    private String[] toLine(IProduct p) {
        if (p instanceof Picture) {
            Picture pic = (Picture) p;
            return new String[] {
                    pic.getNumber().toString(),
                    String.join(";", pic.getTags()),
                    pic.getPrice().toString(),
                    pic.isActive().toString(),
                    pic.getReservedByNumber(),
                    pic.getOwnerNumber()
            };
        }
        return new String[6];
    }

    private Product toObject(String lineCSV) {
        String[] lineSplit = lineCSV.split(",");
        Long nr = Long.parseLong(lineSplit[0]);
        String[] tags = lineSplit[1].split(";");
        String[] valueAndCurrency = lineSplit[2].split(" ");
        Money price = Money.valueOf(Double.parseDouble(valueAndCurrency[0]), valueAndCurrency[1]);
        boolean active = Boolean.valueOf(lineSplit[3]);
        Client reservedBy = null;
        Client owner = null;
        if (!(lineSplit[4].equals("null")))
            reservedBy = clientRepository.get(lineSplit[4]);
        if (!(lineSplit[5].equals("null")))
            owner = clientRepository.get(lineSplit[5]);
        return new Picture(
                tags,
                price,
                reservedBy,
                owner,
                active
        );
    }
}
