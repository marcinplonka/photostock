package pl.com.bottega.photostock.sales.infrastructure.repositories;

import pl.com.bottega.photostock.sales.model.LightBox;
import pl.com.bottega.photostock.sales.model.repositories.LightBoxRepository;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InMemoryLightBoxRepository implements LightBoxRepository {

    private static final Map<String, LightBox> REPO = new HashMap<>();

    @Override
    public void save(LightBox lightBox) {
        REPO.put(lightBox.getNumber(), lightBox);
    }

    @Override
    public LightBox get(String number) {
        if (!REPO.containsKey(number))
            throw new IllegalArgumentException(String.format("No light box %s found", number));
        return REPO.get(number);
    }

    @Override
    public List<LightBox> getClientLightBoxes(String clientNumber) {
        List<LightBox> lboxes = new LinkedList<>();
        for(LightBox lightBox : REPO.values())
            if (lightBox.getOwner().getNumber().equals(clientNumber))
                lboxes.add(lightBox);
        return lboxes;
    }
}
