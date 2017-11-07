package pl.com.bottega.photostock.sales.infrastructure.repositories.jparepositories;

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
    EntityManager em;

    @Override
    public Client get(String number) {
        return em.find(Client.class, number);
    }

    @Override
    @Transactional
    public void save(Client client) {
        em.persist(client);

    }

    @Override
    public Optional<Client> getByLogin(String login) {
        return Optional.of((Client) em.createNativeQuery("SELECT * FROM Client c WHERE c.login LIKE :login"
                , Client.class).setParameter("login", login).getSingleResult());
    }
}
