package pl.com.bottega.photostock.sales.application;

import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.repositories.ClientRepository;
import pl.com.bottega.photostock.sales.model.repositories.LightBoxRepository;
import pl.com.bottega.photostock.sales.model.repositories.ProductRepository;
import pl.com.bottega.photostock.sales.model.repositories.ReservationRepository;

import java.util.List;
import java.util.Set;

public class LightBoxManagement {

    private LightBoxRepository lightBoxRepository;
    private ClientRepository clientRepository;
    private ProductRepository productRepository;
    private ReservationRepository reservationRepository;

    public LightBoxManagement(LightBoxRepository lightBoxRepository, ClientRepository clientRepository, ProductRepository productRepository, ReservationRepository reservationRepository) {
        this.lightBoxRepository = lightBoxRepository;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.reservationRepository = reservationRepository;
    }

    public String create(String clientNumber, String lightBoxName) {
        Client client = clientRepository.get(clientNumber);
        LightBox lbox = new LightBox(client, lightBoxName);
        lightBoxRepository.save(lbox);
        return lbox.getNumber();
    }

    public void add(String lightBoxNumber, Long productNumber, String clientNumber) {
        Client client = clientRepository.get(clientNumber);
        Product product = productRepository.get(productNumber);
        if (product.isAvailable(client))
            product.reservedPer(client);
        if(!(product instanceof Picture))
            throw new IllegalArgumentException("Can only add pictures to repository");
        LightBox lightBox = lightBoxRepository.get(lightBoxNumber);
        Picture picture = (Picture) product;
        lightBox.add(picture);
        lightBoxRepository.save(lightBox);
        productRepository.save(picture);


    }

    public void reserve(String lightBoxNumber, Set<Long> pictureNumbers, String reservationNumber) {
        LightBox lightBox = lightBoxRepository.get(lightBoxNumber);
        String clientNumber = lightBox.getOwner().getNumber();
        Client client = clientRepository.get(clientNumber);
        Reservation reservation = reservationRepository.get(reservationNumber, clientNumber);
        List<Picture> pictures = lightBox.getPictures(pictureNumbers);

        if(pictureNumbers.size() != pictures.size())
            throw new IllegalArgumentException("Invalid product numbers");
        for(Picture picture : pictures)
            picture.ensureAvailable(client);
        for(Picture picture : pictures) {
            reservation.add(picture);
            productRepository.save(picture);
        }
        reservationRepository.save(reservation);
    }

    public List<LightBox> getLightBoxes(String clientNumber) {
        return lightBoxRepository.getClientLightBoxes(clientNumber);
    }

    public LightBox get(String number) {
        return lightBoxRepository.get(number);
    }
}
