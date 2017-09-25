package pl.com.bottega.photostock.sales.ui;

import pl.com.bottega.photostock.sales.model.LightBox;
import pl.com.bottega.photostock.sales.model.Client;
import pl.com.bottega.photostock.sales.model.Product;

public class LightBoxPresenter {

    public void show(LightBox lightBox) {
        System.out.println(lightBox.getName());
        System.out.println("-------------------------------------");
        int i = 1;
        Client client = lightBox.getOwner();
        for(Product product : lightBox.getItems())
            System.out.println(
                    String.format("%s %d. %d | %s",
                        product.isAvailable() ? "" : "X",
                        i++, product.getNumber(),
                        product.calculatePrice(client)
                    )
            );
    }

}
