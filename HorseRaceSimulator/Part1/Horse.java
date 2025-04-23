package Part1;

public class Horse {
    private char symbol;
    private String name;
    private double confidence;
    private int distanceTravelled;
    private boolean fallen;
    
    public Horse(char symbol, String name, double confidence) {
        this.symbol = symbol;
        this.name = name;
        this.confidence = confidence;
        this.distanceTravelled = 0;
        this.fallen = false;
    }
    
    public char getSymbol() {
        return symbol;
    }
    
    public String getName() {
        return name;
    }
    
    public double getConfidence() {
        return confidence;
    }
    
    public void setConfidence(double conf) {
        // Clamp between 0 and 1
        this.confidence = Math.max(0.0, Math.min(conf, 1.0));
    }
    
    public int getDistanceTravelled() {
        return distanceTravelled;
    }
    
    public void moveForward() {
        distanceTravelled++;
    }
    
    public boolean hasFallen() {
        return fallen;
    }
    
    public void fall() {
        fallen = true;
    }
    
    public void reset() {
        distanceTravelled = 0;
        fallen = false;
    }
}
