package pl.com.bottega.photostock.sales.infrastructure.repositories.jparepositories;

import org.springframework.stereotype.Component;
import pl.com.bottega.photostock.sales.model.Client;
import pl.com.bottega.photostock.sales.model.repositories.ClientRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;

@Component
public class JPAClientRepository implements ClientRepository {
    EntityManager em;

    @Override
    public Client get(String number) {
        return em.find(Client.class, number);
    }

    @Override
    @Transactional
    public void save(Client client) {

    }

    @Override
    public Optional<Client> getByLogin(String login) {
        return null;
    }
}
