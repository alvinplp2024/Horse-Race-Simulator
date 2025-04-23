package Part2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public class BettingPanel extends JPanel {
    private JLabel balanceLabel;
    private JComboBox<String> betHorseCombo;
    private JTextField betAmountField;
    private JButton placeBetButton;
    private JLabel betStatusLabel;
    private double userBalance = 10000.0;  // Initial balance
    private String selectedBetHorse = null;
    private double betAmount = 0.0;
    private double oddsMultiplier = 1.0;
    private Random random;
    private RacePanel racePanel; // Reference to the RacePanel

    public BettingPanel(RacePanel racePanel) {
        this.racePanel = racePanel;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Virtual Betting System"));
        random = new Random();
        
        balanceLabel = new JLabel("Balance: $" + String.format("%.2f", userBalance));
        add(balanceLabel);
        add(Box.createVerticalStrut(10));
        
        add(new JLabel("Bet on Horse:"));
        betHorseCombo = new JComboBox<>();
        add(betHorseCombo);
        add(Box.createVerticalStrut(10));
        
        add(new JLabel("Bet Amount:"));
        betAmountField = new JTextField();
        add(betAmountField);
        add(Box.createVerticalStrut(10));
        
        placeBetButton = new JButton("Place Bet");
        add(placeBetButton);
        add(Box.createVerticalStrut(10));
        
        betStatusLabel = new JLabel("No bet placed.");
        add(betStatusLabel);
        
        placeBetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                placeBet();
            }
        });
    }
    
    private void placeBet() {
        String selected = (String) betHorseCombo.getSelectedItem();
        if (selected == null || selected.isEmpty()) {
            betStatusLabel.setText("Please select a horse.");
            return;
        }
        
        try {
            betAmount = Double.parseDouble(betAmountField.getText());
        } catch (NumberFormatException ex) {
            betStatusLabel.setText("Invalid bet amount.");
            return;
        }
        
        if (betAmount <= 0 || betAmount > userBalance) {
            betStatusLabel.setText("Bet amount must be >0 and <= balance.");
            return;
        }
        
        selectedBetHorse = selected;
        oddsMultiplier = generateOddsMultiplier();
        
        // Deduct the bet amount immediately.
        userBalance -= betAmount;
        updateBalance(userBalance);
        
        betStatusLabel.setText("Bet placed on " + selectedBetHorse + " for $" + betAmount + " (Odds: " + String.format("%.2f", oddsMultiplier) + ")");
    }
    
    public void updateBalance(double newBalance) {
        userBalance = newBalance;
        balanceLabel.setText("Balance: $" + String.format("%.2f", userBalance));
    }
    
    public void updateHorseList(List<String> horseNames) {
        betHorseCombo.removeAllItems();
        for (String name : horseNames) {
            betHorseCombo.addItem(name);
        }
    }
    
    public void resetBettingInfo() {
        selectedBetHorse = null;
        betAmount = 0.0;
        betStatusLabel.setText("No bet placed.");
        betAmountField.setText("");
    }
    
    /**
     * Settles the bet upon race completion.
     * If the selected horse wins, profit is added to the user balance.
     * If the horse loses (or has fallen), the bet stands as lost.
     *
     * @param winningHorse The name of the winning horse.
     * @return The result message.
     */
    public String settleBet(String winningHorse) {
        String resultMessage = "";
        
        if (selectedBetHorse == null) {
            resultMessage = "No bet was placed.";
            betStatusLabel.setText(resultMessage);
            return resultMessage;
        }
        
        // Check if the chosen horse has fallen.
        for (ExtendedHorse horse : racePanel.getHorses()) {
            if (horse.getName().equals(selectedBetHorse) && horse.hasFallen()) {
                resultMessage = selectedBetHorse + " has fallen! Bet lost.";
                betStatusLabel.setText(resultMessage);
                updateBalance(userBalance);
                resetBettingInfo();
                final String finalMsg = resultMessage;
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(BettingPanel.this, finalMsg, "Bet Result", JOptionPane.INFORMATION_MESSAGE);
                });
                return resultMessage;
            }
        }
        
        // Normal bet settlement.
        if (selectedBetHorse.equals(winningHorse)) {
            double netProfit = betAmount * (oddsMultiplier - 1);
            userBalance += netProfit;
            resultMessage = "You won! Profit: $" + String.format("%.2f", netProfit);
            betStatusLabel.setText(resultMessage);
        } else {
            resultMessage = "You lost! $" + String.format("%.2f", betAmount) + " lost.";
            betStatusLabel.setText(resultMessage);
        }
        
        updateBalance(userBalance);
        final String finalResultMessage = resultMessage;
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(BettingPanel.this, finalResultMessage, "Bet Result", JOptionPane.INFORMATION_MESSAGE);
        });
        resetBettingInfo();
        return resultMessage;
    }
    
    private double generateOddsMultiplier() {
        // Generate odds multiplier between 1.5 and 5.0.
        return 1.5 + random.nextDouble() * 3.5;
    }
}
