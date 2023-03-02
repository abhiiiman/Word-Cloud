import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Font;
// import java.awt.Graphics;
import java.awt.Graphics2D;
// import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.ImageIcon;
import java.awt.FontMetrics;
import java.awt.*;
// import javax.swing.*;

public class WordCloudTool extends JFrame {
    
    private JPanel panel;
    private JLabel title;
    private JLabel label;
    private JLabel name;
    // private JLabel rollno;
    private JTextField textField;
    private JLabel about;
    private JLabel about1;

    public class RoundedButton extends JButton {
    
        private int arcWidth;
        private int arcHeight;
        
        public RoundedButton(String text, int arcWidth, int arcHeight) {
            super(text);
            this.arcWidth = arcWidth;
            this.arcHeight = arcHeight;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
            g2.setColor(getForeground());
            g2.drawString(getText(), (getWidth() - g2.getFontMetrics().stringWidth(getText())) / 2,
                (getHeight() - g2.getFontMetrics().getHeight()) / 2 + g2.getFontMetrics().getAscent());
            g2.dispose();
        }
        
        @Override
        public Dimension getPreferredSize() {
            Dimension size = super.getPreferredSize();
            size.width += arcWidth * 2;
            size.height += arcHeight * 2;
            return size;
        }
        
    }
    
    public WordCloudTool() {
        super("Word Cloud Tool");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        panel = new JPanel();
        add(panel);

        // panel.setBackground(Color.PINK);
        Color bgcolor = new Color(156, 136, 255);
        panel.setBackground(bgcolor);

        // ImageIcon imageIcon = new ImageIcon("javaFXProject\\src\\imggif.gif");
        // JLabel imageLabel = new JLabel(imageIcon);
        // panel.setLayout(null);
        // imageLabel.setBounds(100, 100, 50, 50);
        // panel.add(imageLabel);

        title = new JLabel("Word Cloud Generator Tool");
        title.setFont(new Font("jokerman", Font.BOLD, 50));
        panel.add(title);

        // creating the signature here.
        name = new JLabel("                                                       Developed By : Abhijit Mandal");
        name.setFont(new Font("poppins", Font.BOLD,25));
        panel.add(name);

        about = new JLabel("<html><br>What is Word Cloud?<br></html>");
        about.setFont(new Font("jokerman", Font.BOLD, 25));
        about.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(about, BorderLayout.CENTER);
        // panel.add(about);

        about1 = new JLabel("<html>A word cloud is a graphical representation of text data,where the size of each word <br>in the cloud is proportional to its frequency or importance in the data set.<br>Word clouds are commonly used to visualize the content of a document,a website,<br>or social media posts.<br>They can be used to quickly identify the most frequently occurring words,<br>as well as to get a sense of the main themes or topics in the text.<br>Word clouds are popular for their visual appeal and ease of interpretation.<br>They are often used in presentations, reports,and websites to make information <br>more engaging and accessible.</html>");
        about1.setFont(new Font("arial", Font.BOLD, 15));
        about1.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(about1, BorderLayout.CENTER);
        // panel.add(about1);

        label = new JLabel("<html><br>Select a file to generate word cloud:<br></html>");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        // panel.add(label);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);

        // creating a select file button here to select the wordlist file to generate.
        // button = new JButton("Select File");
        RoundedButton button = new RoundedButton("Select Word File", 12, 5);
        button.setFont(new Font("Comicsans", Font.BOLD, 20));
        button.setBorderPainted(false);
        // panel.add(rollno);
        // button.setBackground(Color.YELLOW);
        Color myColor = new Color(140, 122, 230);
        button.setBackground(myColor);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    textField.setText(filePath);
                    try {
                        generateWordCloud(filePath);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        button.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(button, BorderLayout.CENTER);
        // panel.add(button);
        
        textField = new JTextField(50);
        // textField.setBounds(100, 150, 50, 100);
        panel.add(textField);
        
        setVisible(true);
    }
    
    private void generateWordCloud(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        Pattern pattern = Pattern.compile("\\b(\\w+)\\b");
        Map<String, Integer> wordCounts = new HashMap<>();
        while ((line = reader.readLine()) != null) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String word = matcher.group(1);
                int count = wordCounts.getOrDefault(word, 0);
                wordCounts.put(word, count + 1);
            }
        }
        reader.close();
        Map<String, Integer> sortedWordCounts = new TreeMap<>(wordCounts);
        int maxSize = sortedWordCounts.values().stream().max(Integer::compareTo).orElse(0);
        int minSize = sortedWordCounts.values().stream().min(Integer::compareTo).orElse(0);
        int width = 600;
        int height = 600;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        Font font = new Font("Arial", Font.PLAIN, 12);
        Random random = new Random();
        for (Map.Entry<String, Integer> entry : sortedWordCounts.entrySet()) {
            String word = entry.getKey();
            int count = entry.getValue();
            int size = (int) Math.ceil((double) (count - minSize) / (maxSize - minSize) * 40) + 10;
            font = font.deriveFont((float) size);
            g2d.setFont(font);
            g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(word);
            int textHeight = fm.getHeight();
            int x = random.nextInt(width - textWidth);
            int y = random.nextInt(height - textHeight);
            g2d.drawString(word, x, y + fm.getAscent());
        }
        g2d.dispose();
        JLabel imageLabel = new JLabel(new ImageIcon(image));
        JFrame frame = new JFrame("Word Cloud");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(imageLabel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        new WordCloudTool();
    }
}

// This is a complete program that creates a GUI-based word cloud tool using Java. When the program is run, a window will appear with a "Select File" button. When the button is clicked, the user can select a text file to generate a word cloud. The word cloud will be displayed in a new JFrame window.