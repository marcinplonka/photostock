package pl.com.bottega.photostock.sales.infrastructure.repositories;

import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.repositories.ClientRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryClientRepository implements ClientRepository {

    private static final Map<String, Client> REPO = new HashMap<>();

    static {
        Client c = new VIPClient("Jan Nowak",
                new Address("ul. Północna 11", "Poland", "Lublin", "20-001"),
                ClientStatus.VIP, Money.valueOf(200), Money.valueOf(200));
        REPO.put(c.getNumber(), c);
    }

    @Override
    public Client get(String number) {
        if (!REPO.containsKey(number))
            throw new IllegalArgumentException(String.format("No client %s found", number));
        return REPO.get(number);
    }

    @Override
    public void save(Client client) {
        REPO.put(client.getNumber(), client);
    }

    @Override
    public Optional<Client> getByLogin(String login) {
        for (Client client : REPO.values())
            if (client.hasLogin(login))
                return Optional.of(client);
        return Optional.empty();
    }
}
