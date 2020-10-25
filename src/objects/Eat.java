package objects;

public class Eat extends Entity {
    
    private static final int CALORIES = 20;
    
    private boolean poison;
    
    public Eat() {
        
        super();
        
        poison = random.nextBoolean();
    }
    
    protected void interaction() {
        
        if(poison) poison = false;
        else destroy();
    }
    
    public boolean isPoison() {
        
        return poison;
    }
    
    public int getCalories() {
        
        return CALORIES;
    }
}
