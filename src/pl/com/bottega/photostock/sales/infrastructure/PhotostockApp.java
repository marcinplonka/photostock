package pl.com.bottega.photostock.sales.infrastructure;
import pl.com.bottega.photostock.sales.application.LightBoxManagement;
import pl.com.bottega.photostock.sales.application.ProductCatalog;
import pl.com.bottega.photostock.sales.application.PurchaseProcess;
import pl.com.bottega.photostock.sales.infrastructure.repositories.*;
import pl.com.bottega.photostock.sales.model.repositories.*;
import pl.com.bottega.photostock.sales.ui.*;

import java.util.Scanner;

public class PhotostockApp {

    public static void main(String[] args) {
        new PhotostockApp().start();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        ClientRepository clientRepository = new CSVClientRepository("/home/marcin/repo/clients.csv");
        ProductRepository productRepository = new CSVProductRepository("/home/marcin/repo/products.csv", clientRepository);
        LightBoxRepository lightBoxRepository = new CSVLightBoxRepository(clientRepository, productRepository, "/home/marcin/repo/lightBoxes.csv");
        ReservationRepository reservationRepository = new InMemoryReservationRepository();
        PurchaseRepository purchaseRepository = new InMemoryPurchaseRepository();
        LightBoxManagement lightBoxManagement = new LightBoxManagement(lightBoxRepository, clientRepository,
                productRepository, reservationRepository);
        AuthenticationManager authenticationManager = new AuthenticationManager(clientRepository);
        AddProductToLightBoxScreen addProductToLightBoxScreen = new AddProductToLightBoxScreen(lightBoxManagement, scanner);
        PurchaseProcess purchaseProcess = new PurchaseProcess(clientRepository, reservationRepository, productRepository, purchaseRepository);
        PurchaseLightBoxScreen purchaseLightBoxScreen = new PurchaseLightBoxScreen(lightBoxManagement, purchaseProcess, scanner);
        LightBoxPresenter presenter = new LightBoxPresenter();
        ProductCatalog productCatalog = new ProductCatalog(productRepository);
        SearchScreen searchScreen = new SearchScreen(scanner, authenticationManager, productCatalog);
        LightBoxManagementScreen lightBoxManagementScreen = new LightBoxManagementScreen(scanner, lightBoxManagement,
                authenticationManager, addProductToLightBoxScreen, purchaseLightBoxScreen, presenter);
        MainScreen mainScreen = new MainScreen(scanner, lightBoxManagementScreen, searchScreen);
        AuthenticationScreen authenticationScreen = new AuthenticationScreen(scanner, authenticationManager);

        authenticationScreen.show();
        mainScreen.show();
    }

}
