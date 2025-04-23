package Part2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RacePanel extends JPanel {
    private List<ExtendedHorse> horses;
    private int raceLength;
    @SuppressWarnings("unused")
    private int lanes;
    private Timer timer;
    private Random rand;
    private boolean raceFinished;
    private String winnerName = "";
    @SuppressWarnings("unused")
    private int tickCount;
    private String trackShape;
    private String weather;
    private BettingPanel bettingPanel; // Reference to the betting panel

    public RacePanel() {
        rand = new Random();
        horses = new ArrayList<>();
        raceFinished = false;
        tickCount = 0;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 400));
    }
    
    // Allow BettingPanel to be set after construction
    public void setBettingPanel(BettingPanel bp) {
        this.bettingPanel = bp;
    }
    
    // Used by BettingPanel to check horse state
    public List<ExtendedHorse> getHorses() {
        return horses;
    }
    
    // Returns the list of horse names for the drop-down menu.
    public List<String> getHorseNames() {
        List<String> names = new ArrayList<>();
        for (ExtendedHorse h : horses) {
            names.add(h.getName());
        }
        return names;
    }
    
    public void startNewRace(int raceLength, int lanes, String trackShape, String weather) {
        this.raceLength = raceLength;
        this.lanes = lanes;
        this.trackShape = trackShape;
        this.weather = weather;
        horses.clear();
        raceFinished = false;
        tickCount = 0;
        winnerName = "";
        
        // Create sample horses based on lane count.
        if (lanes >= 1) horses.add(new ExtendedHorse('\u2658', "PIPPI LONGSTOCKING", 0.6, "Thoroughbred", "Brown", "Lightweight Horseshoes"));
        if (lanes >= 2) horses.add(new ExtendedHorse('\u265E', "KOKOMO", 0.6, "Arabian", "Black", "Sturdy Horseshoes"));
        if (lanes >= 3) horses.add(new ExtendedHorse('\u265F', "EL JEFE", 0.4, "Quarter Horse", "White", "Standard Equipment"));
        
        // Adjust each horse’s performance and reset their state.
        for (ExtendedHorse horse : horses) {
            horse.adjustPerformanceBasedOnAttributes();
            horse.reset();
        }
        
        // Set up a timer to simulate the race.
        timer = new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateRace();
            }
        });
        timer.start();
    }
    
    private void updateRace() {
        if (raceFinished)
            return;
        
        tickCount++;
        
        for (ExtendedHorse horse : horses) {
            if (!horse.hasFallen() && horse.getDistanceTravelled() < raceLength) {
                // Adjust speed based on track shape and weather.
                double speedFactor = trackShape.equals("Oval") ? 1.0 : trackShape.equals("Figure-Eight") ? 0.8 : 1.2;
                double weatherFactor = weather.equals("Muddy") ? 0.7 : weather.equals("Icy") ? 1.3 : 1.0;
                double finalChance = horse.getConfidence() * speedFactor * weatherFactor;
                
                if (rand.nextDouble() < finalChance) {
                    horse.moveForward();
                }
                
                // Determine falling chance.
                double fallChance = horse.getConfidence() * 0.1;
                if (weather.equals("Icy")) {
                    fallChance *= 1.5;
                }
                if (!horse.hasFallen() && rand.nextDouble() < fallChance) {
                    horse.fall();
                    horse.setConfidence(horse.getConfidence() - 0.05);
                    // Log falling (or add a popup if desired—but avoid too many popups).
                    System.out.println(horse.getName() + " has fallen!");
                }
                
                // If the horse finishes the race, declare it the winner.
                if (horse.getDistanceTravelled() >= raceLength && winnerName.isEmpty()) {
                    winnerName = horse.getName();
                    raceFinished = true;
                    horse.updateRaceStats(true);
                    timer.stop();
                    
                    // Announce the winner via popup.
                    JOptionPane.showMessageDialog(RacePanel.this, "Winner: " + winnerName + "!", "Race Finished", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Settle the bet if the betting panel exists.
                    if (bettingPanel != null) {
                        bettingPanel.settleBet(winnerName);
                    }
                }
            }
        }
        
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int laneHeight = getHeight() / (horses.size() + 1);
        int yPos = laneHeight;
        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        for (ExtendedHorse horse : horses) {
            // Draw the lane.
            g.drawLine(50, yPos, 50 + raceLength * 20, yPos);
            
            int xHorse = 50 + horse.getDistanceTravelled() * 20;
            // If the horse has fallen, draw an "X".
            String horseSymbol = horse.hasFallen() ? "X" : Character.toString(horse.getSymbol());
            g.drawString(horseSymbol, xHorse, yPos - 5);
            // Display the horse name and win ratio.
            g.drawString(horse.getName() + " (Win Ratio: " + String.format("%.1f", horse.getWinRatio()) + "%)", 50 + raceLength * 20 + 10, yPos);
            
            yPos += laneHeight;
        }
        
        // Draw the winner text centered at top if the race is finished.
        if (raceFinished && !winnerName.isEmpty()) {
            g.setColor(Color.RED);
            String winTxt = "Winner: " + winnerName + "!";
            int textWidth = g.getFontMetrics().stringWidth(winTxt);
            g.drawString(winTxt, (getWidth() - textWidth) / 2, 30);
        }
    }
}
