package pl.com.bottega.photostock.sales.infrastructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.com.bottega.photostock.sales.application.LightBoxManagement;
import pl.com.bottega.photostock.sales.application.ProductCatalog;
import pl.com.bottega.photostock.sales.application.PurchaseProcess;
import pl.com.bottega.photostock.sales.infrastructure.repositories.csvrepositories.*;
import pl.com.bottega.photostock.sales.infrastructure.repositories.jparepositories.JPAClientRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.jparepositories.NoSuchEntityExeption;
import pl.com.bottega.photostock.sales.model.repositories.*;
import pl.com.bottega.photostock.sales.ui.*;

import java.util.Scanner;
@SpringBootApplication
public class PhotostockApp {


    public static void main(String[] args) {
        SpringApplication.run(PhotostockApp.class, args);
    }
//    private final String REPO_DIRECTORY_PATH = "/home/marcin/repo/";
//    Scanner scanner = new Scanner(System.in);



//    public void run() {
//
//        ClientRepository clientRepository = new JPAClientRepository();
//        ProductRepository productRepository = new CSVProductRepository(REPO_DIRECTORY_PATH, clientRepository);
//        LightBoxRepository lightBoxRepository = new CSVLightBoxRepository(clientRepository, productRepository, REPO_DIRECTORY_PATH);
//        ReservationRepository reservationRepository = new CSVReservationRepository(REPO_DIRECTORY_PATH,clientRepository,productRepository);
//        PurchaseRepository purchaseRepository = new CSVPurchaseRepository(REPO_DIRECTORY_PATH, clientRepository, productRepository);
//        LightBoxManagement lightBoxManagement = new LightBoxManagement(lightBoxRepository, clientRepository, productRepository, reservationRepository);
//        AuthenticationManager authenticationManager = new AuthenticationManager(clientRepository);
//        AddProductToLightBoxScreen addProductToLightBoxScreen = new AddProductToLightBoxScreen(lightBoxManagement, scanner);
//        PurchaseProcess purchaseProcess = new PurchaseProcess(clientRepository, reservationRepository, productRepository, purchaseRepository);
//        PurchaseLightBoxScreen purchaseLightBoxScreen = new PurchaseLightBoxScreen(lightBoxManagement, purchaseProcess, scanner);
//        LightBoxPresenter presenter = new LightBoxPresenter();
//        ProductCatalog productCatalog = new ProductCatalog(productRepository);
//        SearchScreen searchScreen = new SearchScreen(scanner, authenticationManager, productCatalog);
//        LightBoxManagementScreen lightBoxManagementScreen = new LightBoxManagementScreen(scanner, lightBoxManagement,
//                authenticationManager, addProductToLightBoxScreen, purchaseLightBoxScreen, presenter);
//        MainScreen mainScreen = new MainScreen(scanner, lightBoxManagementScreen, searchScreen);
//        AuthenticationScreen authenticationScreen = new AuthenticationScreen(scanner, authenticationManager);
//
//        authenticationScreen.show();

//    }

}
