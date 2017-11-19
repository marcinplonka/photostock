package pl.com.bottega.photostock.sales.infrastructure.repositories.jparepositories;

import org.springframework.stereotype.Component;
import pl.com.bottega.photostock.sales.model.Client;
import pl.com.bottega.photostock.sales.model.LightBox;
import pl.com.bottega.photostock.sales.model.repositories.LightBoxRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
@Component
public class JPALightBoxRepository implements LightBoxRepository{
    EntityManager em;

    public JPALightBoxRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public void save(LightBox lightBox) {
        em.persist(lightBox);
    }

    @Override
    public LightBox get(String number) throws NoSuchEntityExeption {
        LightBox lightBox = em.find(LightBox.class, number);
        if (lightBox == null)
            throw new NoSuchEntityExeption();
        return lightBox;
    }

    @Override
    public List<LightBox> getClientLightBoxes(String clientNumber) {
        return em.find(Client.class, clientNumber).getLightBoxes();
    }
}
