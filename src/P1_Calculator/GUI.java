package P1_Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener {
    // App configs
    public static final String APP_NAME = "Calculator";
    public static final int[] APP_SIZE = {310, 430};

    private final SpringLayout springLayout = new SpringLayout();
    private CalculatorService calculatorService;

    // Text Field
    private JTextField displayField;

    // Buttons
    private JButton[] buttones;

    // flags
    private boolean pressedOperator =  false;
    private boolean pressedEquals =  false;

    public GUI(){
        super(APP_NAME);
        setSize(APP_SIZE[0], APP_SIZE[1]);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(springLayout);

        getContentPane().setBackground(new Color(30, 30, 30));

        addGuiComponents();
        calculatorService = new CalculatorService();
    }

    public void addGuiComponents(){
        // add GUI components
        addDisplayFieldcomponents();
        // add button components
        addButtonComponents();
    }

    public void addDisplayFieldcomponents(){
        JPanel displayFieldpPanel = new JPanel();
        displayFieldpPanel.setBackground(new Color(30, 30, 30));

        displayField = new JTextField(7);
        displayField.setFont(new Font("Dialog", Font.BOLD, 40));
        displayField.setEditable(false);
        displayField.setText("0");
        displayField.setHorizontalAlignment(SwingConstants.RIGHT);
        displayField.setBackground(new Color(50, 50, 50));
        displayField.setForeground(Color.WHITE);
        displayField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        displayFieldpPanel.add(displayField);

        this.getContentPane().add(displayFieldpPanel);
        springLayout.putConstraint(SpringLayout.NORTH, displayFieldpPanel, 25, SpringLayout.NORTH, this.getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, displayFieldpPanel, 15, SpringLayout.WEST, this.getContentPane());
//        springLayout.putConstraint(SpringLayout.EAST, displayFieldpPanel, -20, SpringLayout.EAST, this.getContentPane());
    }

    public void addButtonComponents(){
        GridLayout gridLayout = new GridLayout(4,4);
        JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(gridLayout);
        buttonpanel.setBackground(new Color(30, 30, 30));

        buttones = new JButton[16];
        for(int i = 0; i < 16; i++){
            JButton button = new JButton(getButtonLable(i));
            button.setFont( new Font( "Dialog", Font.BOLD, 28));
            button.addActionListener(this);

            button.setBackground(new Color(70, 70, 70));
            button.setForeground(Color.WHITE); // White text
            button.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50), 1));
            button.setFocusPainted(false); // Remove focus border

            // Specific styling for operators and equals button
            String buttonLabel = getButtonLable(i);
            if (buttonLabel.matches("[/x\\-+]")) { // Operators
                button.setBackground(new Color(255, 140, 0));
                button.setForeground(Color.WHITE);
            } else if (buttonLabel.equals("=")) { // Equals button
                button.setBackground(new Color(0, 128, 0));
                button.setForeground(Color.WHITE);
                button.setFont(new Font("Dialog", Font.BOLD, 38));
            } else if (buttonLabel.equals(".")) { // Decimal button
                button.setBackground(new Color(70, 70, 70));
                button.setForeground(Color.WHITE);
            }

            buttonpanel.add(button);
        }

        gridLayout.setHgap(10);
        gridLayout.setVgap(10);

        this.getContentPane().add(buttonpanel);
        springLayout.putConstraint(SpringLayout.NORTH, buttonpanel, 100, SpringLayout.NORTH, this.getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, buttonpanel, 20, SpringLayout.WEST, this.getContentPane()); // Adjust for consistency
        springLayout.putConstraint(SpringLayout.EAST, buttonpanel, -20, SpringLayout.EAST, this.getContentPane()); // Adjust for consistency
        springLayout.putConstraint(SpringLayout.SOUTH, buttonpanel, -20, SpringLayout.SOUTH, this.getContentPane()); // Adjust to fill space
    }

    public String getButtonLable(int buttonIndex){
        switch(buttonIndex){
            case 0:
                return "7";
            case 1:
                return "8";
            case 2:
                return "9";
            case 3:
                return "/";
            case 4:
                return "4";
            case 5:
                return "5";
            case 6:
                return "6";
            case 7:
                return "x";
            case 8:
                return "1";
            case 9:
                return "2";
            case 10:
                return "3";
            case 11:
                return "-";
            case 12:
                return "0";
            case 13:
                return ".";
            case 14:
                return "+";
            case 15:
                return "=";

        }
        return "";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        System.out.println(e.getActionCommand()); // tp check which button is being pressed
        String buttonCommand = e.getActionCommand();

        if(buttonCommand.matches("[0-9]")) {
            if (pressedEquals || pressedOperator || displayField.getText().equals("0")) {
                displayField.setText(buttonCommand);
            } else {
                displayField.setText(displayField.getText() + buttonCommand);
            }

            pressedEquals = false;
            pressedOperator = false;

        } else if (buttonCommand.equals("=")) {
            calculatorService.setNum2(Double.parseDouble(displayField.getText()));

            double result = 0;
            switch (calculatorService.getMathSymbol()) {
                case '+':
                    result = calculatorService.add();
                    break;
                case '-':
                    result = calculatorService.subtract();
                    break;
                case 'x':
                    result = calculatorService.multiply();
                    break;
                case '/':
                    result = calculatorService.divide();
                    break;
            }

            // Handle potential division by zero
            if (Double.isInfinite(result) || Double.isNaN(result)) {
                displayField.setText("Error");
            } else {
                if (result == (long) result) {
                    displayField.setText(String.format("%d", (long) result));
                } else {
                    displayField.setText(String.format("%.2f", result));
                }
            }


            pressedEquals = true;
            pressedOperator = false;

        } else if (buttonCommand.equals(".")) {
            if (!displayField.getText().contains(".")) {
                displayField.setText(displayField.getText() + ".");
            }

        } else if (!pressedOperator) {
            calculatorService.setNum1(Double.parseDouble(displayField.getText()));
            calculatorService.setMathSymbol(buttonCommand.charAt(0));
            pressedOperator = true;
            pressedEquals = false;
        }
    }
}