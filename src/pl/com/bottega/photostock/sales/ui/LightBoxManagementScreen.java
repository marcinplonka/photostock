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
    private LightBoxPresenter presenter;

    public LightBoxManagementScreen(Scanner scanner, LightBoxManagement lightBoxManagement, AuthenticationManager authenticationManager,
                                    AddProductToLightBoxScreen addProductToLightBoxScreen, PurchaseLightBoxScreen purchaseLightBoxScreen,
                                    LightBoxPresenter presenter) {
        this.scanner = scanner;
        this.lightBoxManagement = lightBoxManagement;
        this.authenticationManager = authenticationManager;
        this.addProductToLightBoxScreen = addProductToLightBoxScreen;
        this.purchaseLightBoxScreen = purchaseLightBoxScreen;
        this.presenter = presenter;
    }

    public void show() {
        System.out.println("Twoje light box'y:");
        lightBoxes = lightBoxManagement.getLightBoxes(authenticationManager.getClientNumber());
        if (lightBoxes.isEmpty())
            System.out.println("Nie masz aktualnie żadnych light box'ów");
        else {
            printLightBoxList();
        }
        lightBoxActions();
    }

    private void printLightBoxList() {
        int index = 1;
        for (LightBox lightBox : lightBoxes)
            System.out.println(String.format("%d. %s", index++, lightBox.getName()));
    }

    private void lightBoxActions() {
        Menu menu = new Menu(scanner);
        menu.setTitleLabel("Zarządzanie light box'ami.");
        menu.addItem("Dodaj nowy light box.", this::addNewLightBox);
        menu.addItem("Wyświetl light box'y.", this::show);
        menu.addItem("Wybierz light box", this::showLightBox);
        menu.setLastItemLabel("Poprzednie menu");
        menu.show();
    }


    private void showLightBox() {
        if (lightBoxes.size() == 0) {
            System.out.println("Nie masz żadnych light box'ów");
            return;
        }
        printLightBoxList();
        System.out.println("Podaj index light box'a z listy powyżej: ");
        int index = scanner.nextInt();
        scanner.nextLine();
        lightBox = lightBoxes.get(index - 1);
        presenter.show(lightBox);
        selectedLightBoxActions();
    }

    private void selectedLightBoxActions() {
        Menu menu = new Menu(scanner);
        menu.setTitleLabel("Zarządzanie light box'em.");
        menu.addItem("Dodaj produkt do light box'a", () -> lightBox = addProductToLightBoxScreen.show(lightBox));
        menu.addItem("Zakup produkty z light box'a.", () -> purchaseLightBoxScreen.show(lightBox));
        menu.addItem("Pokaż zawartość light box'a", () -> presenter.show(lightBox));
        menu.addItem("Wybierz inny light box", this::showLightBox);
        menu.setLastItemLabel("Poprzednie menu");
        menu.show();
    }


    private void addNewLightBox() {
        System.out.println("Podaj nazwę nowego light box'a: ");

        String name = scanner.nextLine();
        String clientNumber = authenticationManager.getClientNumber();

        String currentLightBoxNumber = lightBoxManagement.create(clientNumber, name);

        lightBoxes = lightBoxManagement.getLightBoxes(clientNumber);

        System.out.println(String.format("Light box %s został dodany.", name));

        lightBox = lightBoxManagement.get(currentLightBoxNumber);

        selectedLightBoxActions();
    }
}