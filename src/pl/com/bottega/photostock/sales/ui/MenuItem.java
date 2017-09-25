package pl.com.bottega.photostock.sales.ui;

public class MenuItem {

    private int index;
    private String label;
    private Runnable action;

    public MenuItem(int index, String label, Runnable action) {
        this.index = index;
        this.label = label;
        this.action = action;
    }

    public void show() {
        System.out.println(String.format("%d. %s", index, label));
    }

    public void executeAction() {
        action.run();
    }

}
