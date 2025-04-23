package Part1;

import java.util.*;
import Part2.ExtendedHorse;

public class Race {
    private List<ExtendedHorse> horses;
    private int raceLength;
    private int lanes;
    private String trackShape;
    private String weather;
    private boolean raceFinished;
    private String winnerName;
    private Random random;
    
    public Race(int raceLength, int lanes, String trackShape, String weather) {
        this.raceLength = raceLength;
        this.lanes = lanes;
        this.trackShape = trackShape;
        this.weather = weather;
        this.horses = new ArrayList<>();
        this.raceFinished = false;
        this.winnerName = "";
        this.random = new Random();
        initHorses();
    }
    
    private void initHorses() {
        // Create sample horses as per lane count.
        if (lanes >= 1) 
            horses.add(new ExtendedHorse('\u2658', "PIPPI LONGSTOCKING", 0.6, "Thoroughbred", "Brown", "Lightweight Horseshoes"));
        if (lanes >= 2) 
            horses.add(new ExtendedHorse('\u265E', "KOKOMO", 0.6, "Arabian", "Black", "Sturdy Horseshoes"));
        if (lanes >= 3) 
            horses.add(new ExtendedHorse('\u265F', "EL JEFE", 0.4, "Quarter Horse", "White", "Standard Equipment"));
        
        // Adjust performance attributes.
        for (ExtendedHorse horse : horses) {
            horse.adjustPerformanceBasedOnAttributes();
        }
    }
    
    public List<ExtendedHorse> getHorses() {
        return horses;
    }
    
    public void simulateRace() {
        // Loop until a winner is declared or no interactive horses remain.
        while (!raceFinished) {
            try {
                // Delay between updates so the status is visible.
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            // Update each horse's status.
            for (ExtendedHorse horse : horses) {
                if (!horse.hasFallen() && horse.getDistanceTravelled() < raceLength) {
                    // Adjust movement based on track shape and weather.
                    double speedFactor = trackShape.equalsIgnoreCase("Oval") 
                            ? 1.0 
                            : trackShape.equalsIgnoreCase("Figure-Eight") ? 0.8 : 1.2;
                    double weatherFactor = weather.equalsIgnoreCase("Muddy") 
                            ? 0.7 : weather.equalsIgnoreCase("Icy") ? 1.3 : 1.0;
                    double finalChance = horse.getConfidence() * speedFactor * weatherFactor;
                    
                    if (random.nextDouble() < finalChance) {
                        horse.moveForward();
                    }
                    
                    // Determine falling chance.
                    double fallChance = horse.getConfidence() * 0.1;
                    if (weather.equalsIgnoreCase("Icy")) {
                        fallChance *= 1.5;
                    }
                    if (!horse.hasFallen() && random.nextDouble() < fallChance) {
                        horse.fall();
                        horse.setConfidence(horse.getConfidence() - 0.05);
                        System.out.println("ALERT: " + horse.getName() + " has fallen!");
                    }
                    
                    // If the horse reaches or exceeds the race length and is not fallen,
                    // declare that horse the winner.
                    if (horse.getDistanceTravelled() >= raceLength && !horse.hasFallen() && winnerName.isEmpty()) {
                        winnerName = horse.getName();
                        raceFinished = true;
                        horse.updateRaceStats(true);
                        break; // A winner has been found.
                    }
                }
            }
            
            // Check if any horse is still active (has not fallen and is below the finish).
            boolean anyActive = false;
            for (ExtendedHorse h : horses) {
                if (!h.hasFallen() && h.getDistanceTravelled() < raceLength) {
                    anyActive = true;
                    break;
                }
            }
            if (!anyActive) {
                // No horses can continue – end the race.
                raceFinished = true;
                if (winnerName.isEmpty()) {
                    winnerName = "No winner (all horses fell)";
                }
            }
            
            printRaceStatus();
        }
    }
    
    
    private void printRaceStatus() {
        System.out.println("\nRace Status:");
        for (ExtendedHorse horse : horses) {
            String status = horse.getName() + ": " + horse.getDistanceTravelled() + "/" + raceLength;
            if (horse.hasFallen()) {
                status += " (FALLEN)";
            }
            System.out.println(status);
        }
        System.out.println("-------------------------");
    }
    
    public String getWinner() {
        return winnerName;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Non-UI Console Race Simulator ===");
        
        // Get race configuration from user.
        System.out.print("Enter race length: ");
        int raceLength = scanner.nextInt();
        
        System.out.print("Enter number of lanes: ");
        int lanes = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        System.out.print("Enter track shape (Straight, Oval, Figure-Eight): ");
        String trackShape = scanner.nextLine();
        
        System.out.print("Enter weather (Dry, Muddy, Icy): ");
        String weather = scanner.nextLine();
        
        // Create and run the race simulation.
        Race race = new Race(raceLength, lanes, trackShape, weather);
        
        // --- Betting Section ---
        double userBalance = 10000.0;
        System.out.println("\nYour starting balance: $" + userBalance);
        System.out.println("Available horses:");
        List<ExtendedHorse> horses = race.getHorses();
        for (int i = 0; i < horses.size(); i++) {
            System.out.println((i + 1) + ". " + horses.get(i).getName());
        }
        System.out.print("Place your bet - select a horse by number: ");
        int betChoice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        String selectedBetHorse = horses.get(betChoice - 1).getName();
        
        System.out.print("Enter bet amount: ");
        double betAmount = scanner.nextDouble();
        if (betAmount <= 0 || betAmount > userBalance) {
            System.out.println("Invalid bet amount. Exiting.");
            scanner.close();
            return;
        }
        
        // Deduct the bet amount immediately.
        userBalance -= betAmount;
        System.out.println("Bet placed on " + selectedBetHorse + " for $" + betAmount);
        System.out.println("Balance after bet: $" + userBalance);
        
        // Start the race.
        System.out.println("\nRace is starting...\n");
        race.simulateRace();
        
        // Announce winner.
        String winner = race.getWinner();
        System.out.println("\n*** The winner is: " + winner + " ***");
        
        // Settle the bet.
        if (selectedBetHorse.equals(winner)) {
            // Randomly generate odds multiplier (between 1.5 and 5.0).
            double oddsMultiplier = 1.5 + (new Random().nextDouble() * 3.5);
            double netProfit = betAmount * (oddsMultiplier - 1);
            userBalance += netProfit;
            System.out.println("Congratulations! Your bet won. Profit: $" + String.format("%.2f", netProfit));
        } else {
            System.out.println("Sorry, you lost the bet. $" + betAmount + " has been deducted.");
        }
        
        System.out.println("Final balance: $" + String.format("%.2f", userBalance));
        scanner.close();
    }
}
