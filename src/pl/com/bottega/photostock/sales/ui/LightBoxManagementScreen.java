package pl.com.bottega.photostock.sales.ui;

import pl.com.bottega.photostock.sales.application.LightBoxManagement;
import pl.com.bottega.photostock.sales.model.LightBox;

import java.util.List;
import java.util.Scanner;

public class LightBoxManagementScreen {

    private Scanner scanner;
    private LightBoxManagement lightBoxManagement;
    private AuthenticationManager authenticationManager;
    private List<LightBox> lightBoxes;
    private LightBox lightBox;
    private AddProductToLightBoxScreen addProductToLightBoxScreen;
    private PurchaseLightBoxScreen purchaseLightBoxScreen;

    public LightBoxManagementScreen(Scanner scanner, LightBoxManagement lightBoxManagement,
                                    AuthenticationManager authenticationManager, AddProductToLightBoxScreen addProductToLightBoxScreen, PurchaseLightBoxScreen purchaseLightBoxScreen) {
        this.scanner = scanner;
        this.lightBoxManagement = lightBoxManagement;
        this.authenticationManager = authenticationManager;
        this.addProductToLightBoxScreen = addProductToLightBoxScreen;
        this.purchaseLightBoxScreen = purchaseLightBoxScreen;
    }

    public void show() {
        System.out.println("Twoje lajt boksy:");
        lightBoxes = lightBoxManagement.getLightBoxes(authenticationManager.getClientNumber());
        if (lightBoxes.isEmpty())
            System.out.println("Nie masz aktualnie żadnych lajt boksów");
        else {
            int index = 1;
            for (LightBox lightBox : lightBoxes)
                System.out.println(String.format("%d. %s", index++, lightBox.getName()));
        }
        lightBoxActions();
    }

    private void lightBoxActions() {
        while (true) {
            showMenu();
            int decission = scanner.nextInt();
            scanner.nextLine();

            switch (decission) {
                case 1:
                    addNewLightBox();
                    break;
                case 2:
                    if (lightBoxes.size() > 0) {
                        showLightBox();
                    }
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Sorry, ale nie rozumiem.");
            }
        }
    }

    private void showLightBox() {
        System.out.println("Podaj index Lightbox'a: ");
        int index = scanner.nextInt();
        scanner.nextLine();
        lightBox = lightBoxes.get(index - 1);
        LightBoxPresenter presenter = new LightBoxPresenter();
        presenter.show(lightBox);
        selectedLightBoxActions();
    }

    private void selectedLightBoxActions() {
        while (true) {
            showLightBoxMenu();
            int decission = scanner.nextInt();
            scanner.nextLine();
            switch (decission) {
                case 1:
                    addProductToLightBoxScreen.show(lightBox);
                    break;
                case 2:
                    purchaseLightBoxScreen.show(lightBox);
                case 3:
                    return;
                default:
                    System.out.println("Sorry, ale nie rozumiem.");
            }
        }
    }

    private void showLightBoxMenu() {
        System.out.println("1. Dodaj produkt do LightBox'a");
        System.out.println("2. Zakup produkty z lajt boxa.");
        System.out.println("3. Wróć do poprzedniego menu.");
        System.out.println("Co chcesz zrobić?");
    }

    private void addNewLightBox() {
        System.out.println("Podaj nazwę nowego LighBox'a: ");

        String name = scanner.nextLine();
        String clientNumber = authenticationManager.getClientNumber();

        lightBoxManagement.create(clientNumber, name);

        lightBoxes = lightBoxManagement.getLightBoxes(clientNumber);

        System.out.println(String.format("LightBox %s został dodany.", name));
    }

    private void showMenu() {
        System.out.println("1. Dodaj nowy LightBox.");
        if (lightBoxes.size() > 0)
            System.out.println("2. Wyświetl LightBox.");
        System.out.println("3. Wróć do menu.");
        System.out.println("Co chcesz zrobić?");
    }
}