package WordCloud;

import java.net.URL;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Controller implements Initializable {

    private HashMap<String, Integer> wordMap;
    private int wordCount;

    @FXML
    private AnchorPane root;

    @FXML
    private TextArea textArea;

    @FXML
    private Canvas canvas;

    @FXML
    private Label wordCountLabel;

    @FXML
    private Button generateButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        wordMap = new HashMap<>();
        wordCount = 0;
    }

    @FXML
    private void generateWordCloud() {
        String text = textArea.getText().trim();
        String[] words = text.split("\\s+");
        // Counting the frequency of each word
        for (String word : words) {
            if (!wordMap.containsKey(word)) {
                wordMap.put(word, 1);
            } else {
                int count = wordMap.get(word);
                wordMap.put(word, count + 1);
            }
            wordCount++;
        }

        wordCountLabel.setText("Word Count: " + wordCount);

        // Drawing the word cloud
        drawWordCloud();
    }

    private void drawWordCloud() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        Random rand = new Random();

        Set<String> words = wordMap.keySet();
        double minFontSize = 10;
        double maxFontSize = 60;
        double sizeRange = maxFontSize - minFontSize;

        double x = canvas.getWidth() / 2;
        double y = canvas.getHeight() / 2;

        for (String word : words) {
            int frequency = wordMap.get(word);
            double fontSize = minFontSize + (sizeRange * (double) frequency / wordCount);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
            gc.setFill(Color.rgb(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
            gc.fillText(word, x, y);

            // update x and y coordinates
            x += gc.getFont().getSize() * word.length() / 2;
            y += gc.getFont().getSize();

            // check if the word goes beyond the canvas
            if (x > canvas.getWidth() || y > canvas.getHeight()) {
                x = canvas.getWidth() / 2;
                y = canvas.getHeight() / 2;
            }
        }
    }

}
