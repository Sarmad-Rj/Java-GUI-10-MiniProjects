package P8_Weather_App;

import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUI extends JFrame {
    private JSONObject weatherData;

    private ImageIcon loadImage(String path) {
        try {
            BufferedImage img = ImageIO.read(new File(path));
            return new ImageIcon(img);
        } catch (IOException e) {
            System.out.println("Image not found: " + path);
            return null;
        }
    }

    public GUI() {
        super("Weather App");
        setSize(450, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 450, 650);

        // Background
//        JLabel background = new JLabel(loadImage("src/P8_Weather_App/assets/bg.jpg"));
//        background.setBounds(0, 0, 450, 650);
//        layeredPane.add(background, Integer.valueOf(0));

        // Search field
        JTextField searchTextField = new JTextField("sydney");
        searchTextField.setBounds(15, 15, 351, 45);
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));
        layeredPane.add(searchTextField, Integer.valueOf(1));

        // Search button
        JButton searchButton = new JButton(loadImage("src/P8_Weather_App/assets/search.png"));
        searchButton.setBounds(375, 13, 47, 45);
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        layeredPane.add(searchButton, Integer.valueOf(1));

        // Weather image
        JLabel weatherConditionImage = new JLabel(loadImage("src/P8_Weather_App/assets/cloudy.png"));
        weatherConditionImage.setBounds(0, 125, 450, 217);
        layeredPane.add(weatherConditionImage, Integer.valueOf(1));

        // Temperature
        JLabel temperatureText = new JLabel("10 C", SwingConstants.CENTER);
        temperatureText.setBounds(0, 350, 450, 54);
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));
        layeredPane.add(temperatureText, Integer.valueOf(1));

        // Weather description
        JLabel weatherConditionDesc = new JLabel("Cloudy", SwingConstants.CENTER);
        weatherConditionDesc.setBounds(0, 405, 450, 36);
        weatherConditionDesc.setFont(new Font("Dialog", Font.PLAIN, 32));
        layeredPane.add(weatherConditionDesc, Integer.valueOf(1));

        // Humidity
        layeredPane.add(new JLabel(loadImage("src/P8_Weather_App/assets/humidity.png")) {{
            setBounds(15, 500, 74, 66);
        }}, Integer.valueOf(1));
        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setBounds(90, 500, 85, 55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        layeredPane.add(humidityText, Integer.valueOf(1));

        // Windspeed
        layeredPane.add(new JLabel(loadImage("src/P8_Weather_App/assets/windspeed.png")) {{
            setBounds(220, 500, 74, 66);
        }}, Integer.valueOf(1));
        JLabel windspeedText = new JLabel("<html><b>Windspeed</b> 15km/h</html>");
        windspeedText.setBounds(310, 500, 85, 55);
        windspeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        layeredPane.add(windspeedText, Integer.valueOf(1));

        // Search action
        searchButton.addActionListener(e -> {
            String userInput = searchTextField.getText().trim();
            if (userInput.isEmpty()) return;

            weatherData = WeatherApp.getWeatherData(userInput);
            String weatherCondition = (String) weatherData.get("weather_condition");

            switch (weatherCondition) {
                case "Clear" -> weatherConditionImage.setIcon(loadImage("src/P8_Weather_App/assets/clear.png"));
                case "Cloudy" -> weatherConditionImage.setIcon(loadImage("src/P8_Weather_App/assets/cloudy.png"));
                case "Rain" -> weatherConditionImage.setIcon(loadImage("src/P8_Weather_App/assets/rain.png"));
                case "Snow" -> weatherConditionImage.setIcon(loadImage("src/P8_Weather_App/assets/snow.png"));
            }

            double temperature = (double) weatherData.get("temperature");
            long humidity = (long) weatherData.get("humidity");
            double windspeed = (double) weatherData.get("windspeed");

            temperatureText.setText(temperature + " C");
            weatherConditionDesc.setText(weatherCondition);
            humidityText.setText("<html><b>Humidity</b> " + humidity + "%</html>");
            windspeedText.setText("<html><b>Windspeed</b> " + windspeed + "km/h</html>");
        });

        add(layeredPane);
    }
}
