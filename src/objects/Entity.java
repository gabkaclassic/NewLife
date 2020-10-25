package objects;

import java.util.Random;

public abstract class Entity {
    
    private static final int SIZE_FIELD = 640;
    protected static final Entity[] field = new Entity[SIZE_FIELD];
    
    protected int coordinate;
    protected boolean live;
    
    protected static Random random = new Random();
    
    public Entity() {
        
        location();
    }
    
    public static Entity[] getField() {
        
        return field;
    }
    
    protected abstract void interaction();
    
    protected void destroy() {
        
        field[coordinate] = null;
    }
    
    private void location() {
        
        while(field[coordinate] != null) coordinate = random.nextInt(field.length);
        
        field[coordinate] = this;
    }
    
    public int getCoordinate() {
        
        return coordinate;
    }
    
    public boolean isLive() {
        
        return live;
    }
}
