package P8_Weather_App;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                new GUI().setVisible(true);
            }
        });
    }
}
