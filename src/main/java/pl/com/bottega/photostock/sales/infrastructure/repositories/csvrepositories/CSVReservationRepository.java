package pl.com.bottega.photostock.sales.infrastructure.repositories.csvrepositories;

import pl.com.bottega.photostock.sales.model.Client;
import pl.com.bottega.photostock.sales.model.IProduct;
import pl.com.bottega.photostock.sales.model.Product;
import pl.com.bottega.photostock.sales.model.Reservation;
import pl.com.bottega.photostock.sales.model.repositories.CSVRepository;
import pl.com.bottega.photostock.sales.model.repositories.ClientRepository;
import pl.com.bottega.photostock.sales.model.repositories.ProductRepository;
import pl.com.bottega.photostock.sales.model.repositories.ReservationRepository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CSVReservationRepository implements ReservationRepository, CSVRepository {
    private String repoDirectoryPath;
    private ClientRepository clientRepository;
    private ProductRepository productRepository;
    private final String FILE_NAME = "-reservations.csv";

    public CSVReservationRepository(String repoDirectoryPath, ClientRepository clientRepository, ProductRepository productRepository) {
        this.repoDirectoryPath = repoDirectoryPath;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Reservation get(String number, String clientNumber) {
        try (BufferedReader br = new BufferedReader(new FileReader(repoDirectoryPath + clientNumber + FILE_NAME))) {
            return br.lines()
                    .map(this::toObject).filter(r -> r.getNumber().equals(number))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Reservation nr %s not found", number)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void save(Reservation reservation) {
        Map<String, Reservation> reservations = new HashMap<>();
        String clientNumber = reservation.getOwner().getNumber();
        String path = repoDirectoryPath + clientNumber + FILE_NAME;
        if (fileExists(path))
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                if (br.ready())
                    reservations = br.lines()
                            .map(this::toObject)
                            .collect(Collectors.toMap(Reservation::getNumber, r -> r));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        reservations.put(reservation.getNumber(), reservation);

        try (OutputStream outputStream = new FileOutputStream(path, false);
             PrintStream printStream = new PrintStream(outputStream)) {
            for (Reservation res : reservations.values()) {
                printStream.println(String.join(",", toLine(res)));
            }
            printStream.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private String[] toLine(Reservation reservation) {
        String itemsCSV;
        if (!(reservation.getItems().isEmpty())) {
            itemsCSV = String.join(";",
                    reservation.getItems()
                            .stream()
                            .map(r -> r.getNumber().toString())
                            .collect(Collectors.toList()));
        } else {
            itemsCSV = " ";
        }
        System.out.println(itemsCSV);
        return new String[]{
                reservation.getNumber(),
                reservation.getOwner().getNumber(),
                itemsCSV
        };
    }


    private Reservation toObject(String CSVLine) {
        String[] lineSplit = CSVLine.split(",");
        String reservationNumber = lineSplit[0];
        Client client = clientRepository.get(lineSplit[1]);
        Collection<Product> items = new LinkedList<>();
        if (!lineSplit[2].equals(" "))
            Arrays.stream(lineSplit[2].trim().split(";"))
                    .forEach(i -> items.add(productRepository.get(Long.parseLong(i))));

        return new Reservation(
                reservationNumber,
                client,
                items
        );
    }
}

