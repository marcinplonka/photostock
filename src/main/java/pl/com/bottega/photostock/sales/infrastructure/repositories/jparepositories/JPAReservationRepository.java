package pl.com.bottega.photostock.sales.infrastructure.repositories.jparepositories;

import pl.com.bottega.photostock.sales.model.Reservation;
import pl.com.bottega.photostock.sales.model.repositories.ReservationRepository;

import javax.persistence.EntityManager;

public class JPAReservationRepository implements ReservationRepository {

    EntityManager em;

    public JPAReservationRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Reservation get(String number, String clientNumber) {
        return em.find(Reservation.class, number);
    }

    @Override
    public void save(Reservation reservation) {
            em.persist(reservation);
    }
}
