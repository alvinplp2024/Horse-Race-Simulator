package Part2;

import Part1.Horse;

public class ExtendedHorse extends Horse {
    private String breed;
    @SuppressWarnings("unused")
    private String coatColor;
    private String equipment;
    private int racesWon;
    private int racesParticipated;
    
    public ExtendedHorse(char symbol, String name, double confidence, String breed, String coatColor, String equipment) {
        super(symbol, name, confidence);
        this.breed = breed;
        this.coatColor = coatColor;
        this.equipment = equipment;
        this.racesWon = 0;
        this.racesParticipated = 0;
    }
    
    /**
     * Call this when the race ends:
     * @param won true if the horse won the race
     */
    public void updateRaceStats(boolean won) {
        racesParticipated++;
        if (won) {
            racesWon++;
        }
    }
    
    public double getWinRatio() {
        if (racesParticipated == 0)
            return 0;
        return (double) racesWon / racesParticipated * 100;
    }
    
    public void adjustPerformanceBasedOnAttributes() {
        // Example adjustments based on breed
        if ("Thoroughbred".equalsIgnoreCase(breed)) {
            setConfidence(getConfidence() + 0.05);
        } else if ("Arabian".equalsIgnoreCase(breed)) {
            setConfidence(getConfidence() - 0.05);
        }
        // Equipment adjustments
        if ("Lightweight Horseshoes".equalsIgnoreCase(equipment)) {
            setConfidence(getConfidence() + 0.02);
        } else if ("Sturdy Horseshoes".equalsIgnoreCase(equipment)) {
            setConfidence(getConfidence() - 0.02);
        }
    }
}
