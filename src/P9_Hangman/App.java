package P9_Hangman;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                new Hangman().setVisible(true);
            }
        });
    }
}
