package pl.com.bottega.photostock.sales.ui;

import pl.com.bottega.photostock.sales.application.LightBoxManagement;
import pl.com.bottega.photostock.sales.model.LightBox;

import java.util.Scanner;


public class AddProductToLightBoxScreen {

    private LightBoxManagement lightBoxManagement;
    private Scanner scanner;

    public AddProductToLightBoxScreen(LightBoxManagement lightBoxManagement, Scanner scanner) {
        this.lightBoxManagement = lightBoxManagement;
        this.scanner = scanner;
    }

    public void show(LightBox lightBox) {
        System.out.println("Podaj numer obrazka, który chcesz dodać: ");
        Long pictureNumber = scanner.nextLong();
        scanner.nextLine();
        lightBoxManagement.add(lightBox.getNumber(), pictureNumber);
        System.out.println(String.format("Obrazek o numerze %d został dodany.", pictureNumber));
    }
}