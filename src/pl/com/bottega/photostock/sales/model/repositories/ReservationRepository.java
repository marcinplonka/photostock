package pl.com.bottega.photostock.sales.model.repositories;

import pl.com.bottega.photostock.sales.model.Reservation;

public interface ReservationRepository {

    Reservation get(String number);

    void save(Reservation reservation);

}
