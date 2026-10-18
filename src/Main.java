import GUI.FoodStoreSwing;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FoodStoreSwing app = new FoodStoreSwing();
            app.setVisible(true);
        });
    }
}