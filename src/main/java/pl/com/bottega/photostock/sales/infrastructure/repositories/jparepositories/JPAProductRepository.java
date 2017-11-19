package pl.com.bottega.photostock.sales.infrastructure.repositories.jparepositories;

import pl.com.bottega.photostock.sales.model.Client;
import pl.com.bottega.photostock.sales.model.Money;
import pl.com.bottega.photostock.sales.model.Product;
import pl.com.bottega.photostock.sales.model.repositories.ProductRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class JPAProductRepository implements ProductRepository {
    EntityManager em;

    public JPAProductRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Product get(Long number) {
        return em.find(Product.class, number);
    }

    @Override
    public Optional<Product> getOptional(Long number) {
        return Optional.of(get(number));
    }

    @Override
    public void save(Product product) {
        em.persist(product);
    }

    @Override
    public List<Product> find(Client client, Set<String> tags, Money from, Money to) {
        int discount = 1;
        if (client != null)
        discount = (100 - client.discountPercent())/100;

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root product = cq.from(Product.class);

        Predicate predicate = cb.conjunction();
        if (tags != null && !tags.isEmpty())
            predicate = cb.and(predicate, product.get("tags").in(tags));
        if (from != null && from.value() != 0)
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(product.get("priceAmount"), from.value() /discount));
        if (to != null && to.value() != 0)
            predicate = cb.and(predicate, cb.lessThanOrEqualTo(product.get("priceAmount"), from.value() /discount));
        cq.where(predicate);
        Query q = em.createQuery(cq);

        return q.getResultList();
    }
}
