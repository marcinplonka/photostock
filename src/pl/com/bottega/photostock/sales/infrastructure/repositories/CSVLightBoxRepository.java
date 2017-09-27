package pl.com.bottega.photostock.sales.infrastructure.repositories;
import pl.com.bottega.photostock.sales.model.Client;
import pl.com.bottega.photostock.sales.model.LightBox;
import pl.com.bottega.photostock.sales.model.Picture;
import pl.com.bottega.photostock.sales.model.Product;
import pl.com.bottega.photostock.sales.model.repositories.ClientRepository;
import pl.com.bottega.photostock.sales.model.repositories.LightBoxRepository;
import pl.com.bottega.photostock.sales.model.repositories.ProductRepository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CSVLightBoxRepository implements LightBoxRepository {
    private ClientRepository clientRepository;
    private ProductRepository productRepository;
    private String path;

    public CSVLightBoxRepository(ClientRepository clientRepository, ProductRepository productRepository, String path) {
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.path = path;
    }

    @Override
    public void save(LightBox lightBox) {
        Map<String, LightBox> items = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.lines()
                    .map(this::toObject)
                    .forEach(l -> items.put(l.getNumber(), l));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        items.put(lightBox.getNumber(), lightBox);

        try (OutputStream outputStream = new FileOutputStream(path, false);
             PrintStream printStream = new PrintStream(outputStream)) {
            for (LightBox lb : items.values()) {
                printStream.println(String.join(",", toLine(lb)));
            }
            printStream.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public LightBox get(String number) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br.lines()
                    .map(this::toObject)
                    .filter(l -> l.getNumber().equals(number))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("No Such light box nr: %s", number)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<LightBox> getClientLightBoxes(String clientNumber) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

           return br.lines()
                   .map(this::toObject)
                   .filter(l -> l.getOwner().getNumber().equals(clientNumber))
                   .collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String[] toLine(LightBox lightBox) {
        if (lightBox != null) {
            return new String[] {
                    lightBox.getName(),
                    lightBox.getNumber(),
                    lightBox.getOwner().getNumber(),
                    String.join(";",
                            lightBox.getItems()
                            .stream()
                            .map(p -> p.getNumber().toString())
                            .collect(Collectors.toList()))
            };
        } else {
            throw new IllegalArgumentException("Light box does not exist");
        }
    }

    private LightBox toObject(String CSVline) {
        String[] lineSplit = CSVline.split(",");
        String name = lineSplit[0];
        String number = lineSplit[1];
        Client client = clientRepository.get(lineSplit[2]);
        if (lineSplit.length < 4)
            return new LightBox(client, name, number);

        String[] productNumbers = lineSplit[3].split(";");
        List<Picture> items = new LinkedList<>();
        for (String nr : productNumbers) {
            Product product;
            if ((product = productRepository.get(Long.parseLong(nr))) instanceof Picture) {
                Picture picture = (Picture) product;
                items.add(picture);
            }
        }
        return new LightBox(client, name, number, items);
    }
}
