package pl.com.bottega.photostock.sales.model.repositories;

import pl.com.bottega.photostock.sales.model.LightBox;

import java.util.List;

public interface LightBoxRepository {

    void save(LightBox lightBox);

    LightBox get(String number);

    List<LightBox> getClientLightBoxes(String clientNumber);
}
