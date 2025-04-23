package Part2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RaceGUI extends JFrame {
    // Configuration components.
    private JTextField raceLengthField, lanesField;
    private JComboBox<String> trackShapeCombo, weatherCombo;
    private JButton startButton;
    private RacePanel racePanel;
    private BettingPanel bettingPanel;
    
    public RaceGUI() {
        setTitle("Horse Race Simulator - GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Top configuration panel.
        JPanel configPanel = new JPanel(new FlowLayout());
        configPanel.add(new JLabel("Race Length:"));
        raceLengthField = new JTextField("30", 5);
        configPanel.add(raceLengthField);
        
        configPanel.add(new JLabel("Number of Lanes:"));
        lanesField = new JTextField("3", 5);
        configPanel.add(lanesField);
        
        configPanel.add(new JLabel("Track Shape:"));
        String[] shapes = {"Straight", "Oval", "Figure-Eight", "Custom"};
        trackShapeCombo = new JComboBox<>(shapes);
        configPanel.add(trackShapeCombo);
        
        configPanel.add(new JLabel("Weather:"));
        String[] weatherOptions = {"Dry", "Muddy", "Icy"};
        weatherCombo = new JComboBox<>(weatherOptions);
        configPanel.add(weatherCombo);
        
        startButton = new JButton("Start Race");
        configPanel.add(startButton);
        add(configPanel, BorderLayout.NORTH);
        
        // Create RacePanel and BettingPanel.
        racePanel = new RacePanel();
        bettingPanel = new BettingPanel(racePanel);
        racePanel.setBettingPanel(bettingPanel);  // Link the two panels.
        
        add(racePanel, BorderLayout.CENTER);
        add(bettingPanel, BorderLayout.EAST);
        
        // Start race button action.
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Read configuration values.
                int raceLength = Integer.parseInt(raceLengthField.getText());
                int lanes = Integer.parseInt(lanesField.getText());
                String trackShape = (String) trackShapeCombo.getSelectedItem();
                String weather = (String) weatherCombo.getSelectedItem();
                
                // Start the race.
                racePanel.startNewRace(raceLength, lanes, trackShape, weather);
                // Update the betting panel with the names of the horses.
                bettingPanel.updateHorseList(racePanel.getHorseNames());
            }
        });
        
        setSize(1000, 600);
        setLocationRelativeTo(null);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RaceGUI gui = new RaceGUI();
            gui.setVisible(true);
        });
    }
}
