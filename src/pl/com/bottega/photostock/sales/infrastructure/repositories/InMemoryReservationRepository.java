package pl.com.bottega.photostock.sales.infrastructure.repositories;

import pl.com.bottega.photostock.sales.model.Reservation;
import pl.com.bottega.photostock.sales.model.repositories.ReservationRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryReservationRepository implements ReservationRepository {

    private static final Map<String, Reservation> REPO = new HashMap<>();

    @Override
    public Reservation get(String number) {
        if(!REPO.containsKey(number))
            throw new IllegalArgumentException(String.format("No reservation %s found", number));
        return REPO.get(number);
    }

    @Override
    public void save(Reservation reservation) {
        REPO.put(reservation.getNumber(), reservation);
    }
}
