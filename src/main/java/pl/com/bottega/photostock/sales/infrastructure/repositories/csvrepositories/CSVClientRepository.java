package pl.com.bottega.photostock.sales.infrastructure.repositories.csvrepositories;

import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.repositories.CSVRepository;
import pl.com.bottega.photostock.sales.model.repositories.ClientRepository;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CSVClientRepository implements ClientRepository, CSVRepository {
    private final String repoDirectoryPath;
    private final String FILE_NAME = "clients.csv";
    private String path;

    public CSVClientRepository(String repoDirectoryPath) {
        this.repoDirectoryPath = repoDirectoryPath;
        this.path = repoDirectoryPath + FILE_NAME;
    }

    @Override
    public Client get(String number) {

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br
                    .lines()
                    .map(this::toObject)
                    .filter(c -> c.getNumber().equals(number))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("No such client number %s", number)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Client client) {
        Map<String, Client> clients = new HashMap<>();
        if (fileExists(path))
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {

                clients = br.lines().map(this::toObject).collect(Collectors.toMap(Client::getNumber, c -> c));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        clients.put(client.getNumber(), client);

        try (OutputStream outputStream = new FileOutputStream(path, false);
             PrintStream printStream = new PrintStream(outputStream)) {
            for (Client cl : clients.values()) {
                printStream.println(String.join(",", toLine(cl)));
            }
            printStream.flush();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public Optional<Client> getByLogin(String login) {
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                return br
                        .lines()
                        .map(this::toObject)
                        .filter(c -> c.getName().equals(login))
                        .findFirst();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


    }


    private Client toObject(String CSVline) {
        String[] lineSplit = CSVline.split(",");
        String[] addressLines = lineSplit[2].split(";");
        String line1 = addressLines[0];
        String line2 = addressLines[1];
        String country = addressLines[2];
        String city = addressLines[3];
        String postalCode = addressLines[4];

        Address address = new Address(line1, line2, country, city, postalCode);
        String[] balance = lineSplit[4].trim().split(" ");
        if (lineSplit[3].equals("VIP")) {

            String[] creditLimit = lineSplit[5].trim().split(" ");
            return new VIPClient(
                    lineSplit[0],
                    lineSplit[1],
                    address,
                    ClientStatus.valueOf(lineSplit[3]),
                    Money.valueOf(Double.parseDouble(balance[0]), balance[1]),
                    Money.valueOf(Double.parseDouble(creditLimit[0]), creditLimit[1]));
        } else {
            return new StandardClient(
                    lineSplit[0],
                    lineSplit[1],
                    address,
                    ClientStatus.valueOf(lineSplit[3]),
                    Money.valueOf(Double.parseDouble(balance[0]), balance[1])
            );
        }
    }

    private String[] toLine(Client client) {

        String[] CSVLines = new String[]{
                client.getName(),
                client.getNumber(),
                client.getAddress().toString(),
                client.getStatus().toString(),
                client.balance().toString(),
                null
        };
        if (client instanceof VIPClient) {
            CSVLines[5] = String.valueOf(((VIPClient) client).getCreditLimit());
        }
        return CSVLines;

    }
}

