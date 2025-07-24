package P3_RockPaperScissor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException; // Required for font loading if using custom fonts
import java.io.InputStream; // Required for font loading if using custom fonts

public class GUI extends JFrame implements ActionListener {
    // Player buttons
    JButton rockButton, paperButton, scissorButton;

    // Will display the choice of the computer
    JLabel computerChoiceLabel; // Renamed for clarity

    // Will display the score of the computer and the player
    JLabel computerScoreLabel, playerScoreLabel;

    // Game logic instance
    RockPaperScissor rockPaperScissor;

    // --- Modern Color Palette ---
    private static final Color BACKGROUND_LIGHT = new Color(240, 242, 245); // Very light grey/blue for main background
    private static final Color PRIMARY_ACCENT = new Color(60, 179, 113); // Medium Sea Green - strong accent
    private static final Color SECONDARY_ACCENT = new Color(255, 160, 0); // Orange - for computer score
    private static final Color TEXT_DARK = new Color(50, 50, 50); // Dark grey for general text
    private static final Color TEXT_LIGHT = Color.WHITE; // White for text on colored buttons
    private static final Color BORDER_COLOR = new Color(180, 180, 180); // Light grey for borders
    private static final Color BUTTON_HOVER_COLOR = new Color(70, 190, 120); // Slightly darker green for hover

    // --- Custom Fonts (Optional but highly recommended for professional look) ---
    private Font headerFont;
    private Font scoreFont;
    private Font buttonFont;
    private Font choiceFont;
    private Font dialogFont;

    public GUI() {
        super("Rock Paper Scissors");
        setSize(450, 600); // Slightly adjusted height
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false); // Often good for simple games to maintain design integrity

        // Load custom fonts
        loadFonts();

        // Use the defined subtle background color
        getContentPane().setBackground(BACKGROUND_LIGHT);

        rockPaperScissor = new RockPaperScissor();

        addGuiComponents();
    }

    // Method to load custom fonts from resources
    private void loadFonts() {
        try {
            // Example of loading a font from resources (e.g., a 'fonts' folder in your project)
            // You'd need to add a .ttf or .otf file to your project resources
            InputStream is = getClass().getResourceAsStream("/fonts/Montserrat-Bold.ttf"); // Example path
            if (is != null) {
                Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(baseFont);

                headerFont = baseFont.deriveFont(Font.BOLD, 36f);
                scoreFont = baseFont.deriveFont(Font.BOLD, 24f);
                buttonFont = baseFont.deriveFont(Font.BOLD, 18f);
                choiceFont = baseFont.deriveFont(Font.BOLD, 50f); // Larger for "?"
                dialogFont = baseFont.deriveFont(Font.BOLD, 16f);
            } else {
//                System.err.println("Font file not found. Using default fonts.");
                setDefaultFonts();
            }
        } catch (FontFormatException | IOException e) {
            System.err.println("Error loading custom font: " + e.getMessage());
            setDefaultFonts();
        }
    }

    // Fallback to default Swing fonts if custom fonts fail to load
    private void setDefaultFonts() {
        headerFont = new Font("Segoe UI", Font.BOLD, 36);
        scoreFont = new Font("Segoe UI", Font.BOLD, 24);
        buttonFont = new Font("Segoe UI", Font.BOLD, 18);
        choiceFont = new Font("Segoe UI", Font.BOLD, 50);
        dialogFont = new Font("Segoe UI", Font.BOLD, 16);
    }


    private void addGuiComponents() {
        // Use GridBagLayout for flexible and professional alignment
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding between components

        // --- Header ---
        JLabel titleLabel = new JLabel("Rock Paper Scissors!");
        titleLabel.setFont(headerFont);
        titleLabel.setForeground(PRIMARY_ACCENT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3; // Span across all three columns
        gbc.weighty = 0.1; // Give it some vertical space
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        // --- Computer Score Label ---
        computerScoreLabel = new JLabel("Computer: 0");
        computerScoreLabel.setFont(scoreFont);
        computerScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        computerScoreLabel.setForeground(SECONDARY_ACCENT); // Orange for computer
        gbc.gridy = 1;
        gbc.weighty = 0; // Don't stretch vertically
        add(computerScoreLabel, gbc);

        // --- Computer Choice Display ---
        computerChoiceLabel = new JLabel("?");
        computerChoiceLabel.setFont(choiceFont);
        computerChoiceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        computerChoiceLabel.setPreferredSize(new Dimension(100, 100)); // Fixed size
        computerChoiceLabel.setOpaque(true); // Make background visible
        computerChoiceLabel.setBackground(TEXT_LIGHT); // White background for the choice box
        computerChoiceLabel.setForeground(TEXT_DARK); // Dark text for the choice
        computerChoiceLabel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2, true)); // Rounded border
        gbc.gridy = 2;
        gbc.weighty = 0.3; // Give it more vertical space
        add(computerChoiceLabel, gbc);

        // --- Player Score Label ---
        playerScoreLabel = new JLabel("Player: 0");
        playerScoreLabel.setFont(scoreFont);
        playerScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playerScoreLabel.setForeground(PRIMARY_ACCENT); // Green for player
        gbc.gridy = 3;
        gbc.weighty = 0; // Don't stretch vertically
        add(playerScoreLabel, gbc);

        // --- Player Buttons Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Spacing between buttons
        buttonPanel.setBackground(BACKGROUND_LIGHT); // Match frame background

        // Rock button
        rockButton = createStyledButton("Rock");
        rockButton.addActionListener(this);
        buttonPanel.add(rockButton);

        // Paper button
        paperButton = createStyledButton("Paper");
        paperButton.addActionListener(this);
        buttonPanel.add(paperButton);

        // Scissor button
        scissorButton = createStyledButton("Scissors");
        scissorButton.addActionListener(this);
        buttonPanel.add(scissorButton);

        gbc.gridy = 4;
        gbc.weighty = 0.3; // Give buttons panel some vertical space
        gbc.anchor = GridBagConstraints.SOUTH; // Align to bottom
        add(buttonPanel, gbc);
    }

    // Helper method to create consistently styled buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(buttonFont);
        button.setBackground(PRIMARY_ACCENT); // Green button
        button.setForeground(TEXT_LIGHT); // White text
        button.setFocusPainted(false); // No ugly focus border
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_ACCENT.darker(), 1), // Subtle darker border
                new EmptyBorder(15, 25, 15, 25) // Inner padding
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover

        // Optional: Add hover effect (more advanced, often requires custom UI or listener)
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER_COLOR); // Change color on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_ACCENT); // Revert on exit
            }
        });

        return button;
    }

    // Displays a message dialog which will show the winner and a try again button to play again
    private void showDialog(String message) {
        JDialog resultDialog = new JDialog(this, "Game Over", true); // More generic title
        resultDialog.setSize(300, 180); // Adjusted size for better appearance
        resultDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        resultDialog.setResizable(false);
        resultDialog.setLayout(new BorderLayout(10, 10)); // Add spacing
        resultDialog.getContentPane().setBackground(BACKGROUND_LIGHT);
        resultDialog.getRootPane().setBorder(new EmptyBorder(10, 10, 10, 10)); // Dialog padding

        // Message label
        JLabel resultLabel = new JLabel(message);
        resultLabel.setFont(dialogFont);
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultLabel.setForeground(TEXT_DARK);
        resultDialog.add(resultLabel, BorderLayout.CENTER);

        // Try again button
        JButton tryAgainButton = new JButton("Play Again?");
        tryAgainButton.setFont(buttonFont.deriveFont(Font.PLAIN, 16f)); // Slightly smaller font for dialog button
        tryAgainButton.setBackground(PRIMARY_ACCENT);
        tryAgainButton.setForeground(TEXT_LIGHT);
        tryAgainButton.setFocusPainted(false);
        tryAgainButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        tryAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reset computer choice
                computerChoiceLabel.setText("?");
                computerScoreLabel.setText("Computer: " + rockPaperScissor.getComputerScore());
                playerScoreLabel.setText("Player: " + rockPaperScissor.getPlayerScore());
                resultDialog.dispose();
            }
        });

        JPanel dialogButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dialogButtonPanel.setBackground(BACKGROUND_LIGHT);
        dialogButtonPanel.add(tryAgainButton);
        resultDialog.add(dialogButtonPanel, BorderLayout.SOUTH);

        resultDialog.setLocationRelativeTo(this);
        resultDialog.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Get player choice
        String playerChoice = e.getActionCommand(); // Use getActionCommand directly

        // Play rock paper scissors and store result into string var
        String result = rockPaperScissor.playRockPaperScissor(playerChoice);

        // Load computer's choice
        computerChoiceLabel.setText(rockPaperScissor.getComputerChoice());

        // Update score
        computerScoreLabel.setText("Computer: " + rockPaperScissor.getComputerScore());
        playerScoreLabel.setText("Player: " + rockPaperScissor.getPlayerScore());

        // Display result dialog
        showDialog(result);
    }
}