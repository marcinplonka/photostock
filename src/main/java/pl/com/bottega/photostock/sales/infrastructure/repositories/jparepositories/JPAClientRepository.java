package pl.com.bottega.photostock.sales.infrastructure.repositories.jparepositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.com.bottega.photostock.sales.model.Client;
import pl.com.bottega.photostock.sales.model.repositories.ClientRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Component
public class JPAClientRepository implements ClientRepository {
    @Autowired
    EntityManager em;

    public JPAClientRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Client get(String number) {
        return em.find(Client.class, number);
    }

    @Override
    @Transactional
    public void save(Client client) {
        em.merge(client);
    }

    @Override
    public Optional<Client> getByLogin(String login) {
        Query q = em.createNativeQuery("SELECT * FROM Client c WHERE c.name LIKE :login", Client.class)
                .setParameter("login", login);
        Client client = (Client) q.getSingleResult();
        return Optional.of(client);
    }
}
