package pl.com.bottega.photostock.sales.application;

import pl.com.bottega.photostock.sales.model.Money;

import java.util.Collection;
import java.util.HashSet;

public class delete {
    public static void main(String[] args) {
        Collection<Money> monies = new HashSet<>();
        monies.add(Money.valueOf(10));
        monies.add(Money.valueOf(10));
        System.out.println(monies.size());

        int x = 14;
        anInt(x);
        anInt(x);
        System.out.println(x);
    }

    private static void anInt(int x) {
        x++;
    }


}
