package pl.com.bottega.photostock.sales.application;

import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.repositories.*;

import java.util.Collection;

public class PurchaseProcess {

    private static final Money TOLERANCE = Money.valueOf(10);

    private ClientRepository clientRepository;
    private ReservationRepository reservationRepository;
    private ProductRepository productRepository;
    private PurchaseRepository purchaseRepository;

    public PurchaseProcess(ClientRepository repository, ReservationRepository reservationRepository,
                           ProductRepository productRepository, PurchaseRepository purchaseRepository) {
        this.clientRepository = repository;
        this.reservationRepository = reservationRepository;
        this.productRepository = productRepository;
        this.purchaseRepository = purchaseRepository;
    }

    public String createReservation(String clientNumber) {
        Client client = clientRepository.get(clientNumber);
        Reservation reservation = new Reservation(client);
        reservationRepository.save(reservation);
        return reservation.getNumber();
    }

    public void add(String reservationNumber, Long productNumber, String clientNumber) {
        Reservation reservation = reservationRepository.get(reservationNumber, clientNumber);
        Product product = productRepository.get(productNumber);
        reservation.add(product);
        reservationRepository.save(reservation);
        productRepository.save(product);
    }

    public Offer calculateOffer(String reservationNumber, String clientNumber) {
        Reservation reservation = reservationRepository.get(reservationNumber, clientNumber);
        Offer offer = reservation.generateOffer();
        return offer;
    }

    public PurchaseStatus confirm(String reservationNumber, Offer clientOffer, String clientNumber) {
        Offer currentOffer = calculateOffer(reservationNumber, clientNumber);
        if (currentOffer.sameAs(clientOffer, TOLERANCE)) {
            Client client = currentOffer.getOwner();
            Money offerCost = clientOffer.getTotalCost();
            if (client.canAfford(offerCost)) {
                Purchase purchase = clientOffer.purchase();
                purchaseRepository.save(purchase);
                clientRepository.save(clientOffer.getOwner());
                Collection<IProduct> products = clientOffer.getItems();
                for (IProduct product : products) {
                    product.soldPer(client);
                    productRepository.save(product);
                }
                return PurchaseStatus.SUCCESS;
            } else
                return PurchaseStatus.NOT_ENOUGH_FOUNDS;
        } else
            return PurchaseStatus.OFFER_MISMATCH;
    }

}
