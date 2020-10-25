package objects;

import java.util.*;
import java.util.stream.IntStream;

public class Being extends Entity {
    
    private static final int CHANCE_MUTATION = 10;
    private static final int LIFE_DEFAULT = 20;
    private static final int ACTION_VARIATION = 8;
    private static final int ACTIONS_AMOUNT = 3;
    private static final int ACTIONS_MATRIX_SIZE = 32;
    private static final int EDGE_FIELD_SIZE = (int)Math.sqrt(field.length);
    private static final Map<Class, Integer> transitionMap = new HashMap<>();
    
    static {
    
        transitionMap.put(Wall.class, 2);
        transitionMap.put(Eat.class, 4);
        transitionMap.put(Being.class, 3);
    }
    
    private int[] actions;
    
    private int[] orientation;
    
    private int lifeCount;
    private int currentAction;
    
    public Being() {
        
        super();
        setOrientation();
        setActions();
        lifeCount = LIFE_DEFAULT;
        currentAction = 0;
    }
    
    private Being(int[] actions) {
        
        this();
        
        this.actions = Arrays.copyOf(actions, actions.length);
        
        if(random.nextInt(100) < CHANCE_MUTATION) mutation();
    }
    
    public void move() {
    
        int action = (actions[currentAction] / ACTION_VARIATION);
        int actionVariation = (actions[currentAction] % ACTION_VARIATION);
    
        switch(action) {
            
            case 0 -> motion(actionVariation);
            case 1 -> rotation(actionVariation);
            case 2 -> grab(actionVariation);
        }
        
        int targetCoordinate = coordinate + orientation[actionVariation];
        
        if(checkTargetCoordinate(targetCoordinate)) {
            
            Entity entity = field[targetCoordinate];
            int step;
    
            if(entity != null) step = transitionMap.get(entity.getClass());
            else step = 1;
    
            if ((step + currentAction) < actions.length) currentAction += step;
            else currentAction = (currentAction + step - actions.length);
        }
        if(lifeCount <= 0) destroy();
    }
    
    public List<Being> getClones(int cloneNumber) {
        
        List<Being> clones = new ArrayList<>();
        
        for(int i = 0; i < cloneNumber; i++)
            clones.add(new Being(actions));
        
        return clones;
    }
    
    protected void interaction() {
        
        lifeCount--;
    }
    
    private void motion(int variation) {
        
        int dCoordinate = coordinate + orientation[variation];
        
        if(checkTargetCoordinate(dCoordinate)) {
            
            Entity target = field[dCoordinate];
            
            if(target != null) {
                
                if(target instanceof Wall)
                    lifeCount--;
                else if(target instanceof Eat) {
                    
                    eat((Eat)target);
                    
                    field[dCoordinate] = this;
                }
            }
            else field[dCoordinate] = this;
        }
    }
    
    private void rotation(int variation) {
    
        if(variation > 3) {
            
            variation -= 3;
            
            for(int i = 0; i < variation; i++) {
                
                int val = orientation[0];
    
                System.arraycopy(orientation, 1, orientation, 0, orientation.length - 1);
                
                orientation[orientation.length - 1] = val;
            }
        }
        else {
    
            for(int i = 0; i < variation; i++) {
        
                int val = orientation[orientation.length - 1];
    
                System.arraycopy(orientation, 0, orientation, 1, orientation.length - 1);
                
                orientation[0] = val;
            }
        }
    }
    
    private void grab(int variation) {
        
        int targetCoordinate = coordinate + orientation[variation];
        
        if(checkTargetCoordinate(targetCoordinate) && (field[targetCoordinate] != null))
            field[targetCoordinate].interaction();
    }
    
    private void eat(Eat eat) {
        
        if(eat.isPoison()) {
            
            eat.destroy();
            destroy();
        }
        else eat.destroy();
    }
    
    private boolean checkTargetCoordinate(int coordinate) {
        
        return (coordinate >= 0) && (coordinate < field.length)
                && !((this.coordinate % EDGE_FIELD_SIZE == 0) && ((coordinate % EDGE_FIELD_SIZE) == (EDGE_FIELD_SIZE - 1)))
                && !((this.coordinate % EDGE_FIELD_SIZE == (EDGE_FIELD_SIZE - 1)) && ((coordinate % EDGE_FIELD_SIZE) == 0))
                && (this.coordinate != coordinate);
    }
    
    private void setActions() {
    
        actions = IntStream.generate(() -> random.nextInt(ACTION_VARIATION * ACTIONS_AMOUNT))
                .limit(ACTIONS_MATRIX_SIZE).toArray();
    }
    
    private void setOrientation() {
        
        orientation = new int[]{
                -EDGE_FIELD_SIZE - 1,  //up left
                -EDGE_FIELD_SIZE,      //up
                -EDGE_FIELD_SIZE + 1,  //up right
                1,               //right
                EDGE_FIELD_SIZE + 1,   //down right
                EDGE_FIELD_SIZE,       //down
                EDGE_FIELD_SIZE - 1,   //down left
                -1               //left
        };
    }
    
    private void mutation() {
    
        for(int i = 0; i < actions.length; i++)
            if(random.nextBoolean()) actions[i] += -1 + random.nextInt(3);
    }
    
    public static int getEdgeFieldSize() {
        
        return EDGE_FIELD_SIZE;
    }
}
