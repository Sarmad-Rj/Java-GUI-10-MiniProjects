package P2_Paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GUI extends JFrame {
    public GUI() {
        super("PAINT");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 600));
        pack();
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(240, 240, 240));

        addGuiComponents();
    }

    public void addGuiComponents() {
        JPanel canvasPanel = new JPanel();
        SpringLayout springLayout = new SpringLayout();
        canvasPanel.setLayout(springLayout);
        canvasPanel.setBackground(new Color(240, 240, 240));

        // 1. Canvas
        Canvas canvas = new Canvas(1500, 1000);
        canvasPanel.add(canvas);
        springLayout.putConstraint(SpringLayout.NORTH, canvas, 50, SpringLayout.NORTH, canvasPanel);
        springLayout.putConstraint(SpringLayout.WEST, canvas, 10, SpringLayout.WEST, canvasPanel);
        springLayout.putConstraint(SpringLayout.EAST, canvas, -10, SpringLayout.EAST, canvasPanel);
        springLayout.putConstraint(SpringLayout.SOUTH, canvas, -10, SpringLayout.SOUTH, canvasPanel);
        canvas.setBackground(Color.WHITE); // Ensure the drawing area is clearly white
        canvas.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));

        // 2. Color chooser

        JButton colorChooseButton = new JButton("Choose Color");
        colorChooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color c = JColorChooser.showDialog(null, "Choose a color", Color.BLACK);
                colorChooseButton.setBackground(c);
                canvas.setColor(c);
            }
        });
        canvasPanel.add(colorChooseButton);
        springLayout.putConstraint(SpringLayout.NORTH, colorChooseButton, 10, SpringLayout.NORTH, canvasPanel);
        springLayout.putConstraint(SpringLayout.WEST, colorChooseButton, 25, SpringLayout.WEST, canvasPanel);

        // 3. Line Size
        String[] lineSizes = {"1", "5", "10", "20", "50", "100", "200"};
        JComboBox<String> lineSizeBox = new JComboBox<>(lineSizes);
        lineSizeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int strokeSize = Integer.parseInt((String) lineSizeBox.getSelectedItem());
                canvas.setStrokeSize(strokeSize);
            }
        });
        canvasPanel.add(lineSizeBox);
        springLayout.putConstraint(SpringLayout.NORTH, lineSizeBox, 10, SpringLayout.NORTH, canvasPanel);
        springLayout.putConstraint(SpringLayout.WEST, lineSizeBox, 150, SpringLayout.WEST, canvasPanel);

        // 4. Reset Button
        JButton resetButton = new JButton("RESET");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.resetCanvas();
            }
        });
        canvasPanel.add(resetButton);
        springLayout.putConstraint(SpringLayout.NORTH, resetButton, 10, SpringLayout.NORTH, canvasPanel);
        springLayout.putConstraint(SpringLayout.WEST, resetButton, 210, SpringLayout.WEST, canvasPanel);
        this.getContentPane().add(canvasPanel);

        // 5. Save Button
        JButton saveButton = new JButton("SAVE");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save Canvas as Image");
                int userSelection = fileChooser.showSaveDialog(null);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    // Ensure it has a .png extension
                    if (!fileToSave.getName().toLowerCase().endsWith(".png")) {
                        fileToSave = new File(fileToSave.getAbsolutePath() + ".png");
                    }
                    canvas.saveCanvasAsImage(fileToSave);
                    JOptionPane.showMessageDialog(null, "Image saved successfully!");
                }
            }
        });
        canvasPanel.add(saveButton);
        springLayout.putConstraint(SpringLayout.NORTH, saveButton, 10, SpringLayout.NORTH, canvasPanel);
        springLayout.putConstraint(SpringLayout.WEST, saveButton, 300, SpringLayout.WEST, canvasPanel);

    }
}