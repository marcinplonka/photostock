package pl.com.bottega.photostock.sales.ui;

import pl.com.bottega.photostock.sales.model.Client;
import pl.com.bottega.photostock.sales.model.repositories.ClientRepository;

import java.util.Optional;

public class AuthenticationManager {

    private ClientRepository clientRepository;
    private Client client;

    public AuthenticationManager(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client getClient() {
        return client;
    }

    public boolean authenticate(String login) {
        Optional<Client> clientOptional = clientRepository.getByLogin(login);
        if(clientOptional.isPresent()) {
            client = clientOptional.get();
            return true;
        }
        return false;
    }

    public String getClientNumber() {
        return client.getNumber();
    }
}
