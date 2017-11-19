package pl.com.bottega.photostock.sales.infrastructure.repositories.jparepositories;

import org.springframework.stereotype.Component;
import pl.com.bottega.photostock.sales.model.Client;
import pl.com.bottega.photostock.sales.model.Purchase;
import pl.com.bottega.photostock.sales.model.repositories.PurchaseRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Component
public class JPAPurchaseRepository implements PurchaseRepository {

    EntityManager em;

    public JPAPurchaseRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(Purchase purchase) {
        em.persist(purchase);

    }

    @Override
    public Purchase get(String number, String clientNumber) {
       return em.find(Purchase.class, number);
    }
}
