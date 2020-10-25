package ui;

import objects.Being;
import objects.Eat;
import objects.Entity;
import objects.Wall;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Panel extends JPanel implements Constants {
    
    private static final int SIZE_EDGE = 10;
    
    private EntityManager entityManager;
    
    public Panel() {
    
        entityManager = new EntityManager();
        
        start();
    }
    
    public void paintComponent(Graphics g) {
        
        Graphics2D gr = (Graphics2D)g;
        super.paintComponent(g);
        
        paintField(gr);
        paintEntities(gr);
    }
    
    private void paintEntities(Graphics2D gr) {
    
        super.paintComponent(gr);
        
        for(Entity entity: EntityManager.field) {
        
            if(entity != null) {
            
                if(entity instanceof Eat)
                    gr.setColor((((Eat) entity).isPoison()) ? Color.GREEN : Color.RED);
                else if(entity instanceof Wall) gr.setColor(Color.DARK_GRAY);
                else gr.setColor(Color.BLUE);
            
                int x = (entity.getCoordinate() % EntityManager.EDGE_FIELD_SIZE) * SIZE_EDGE;
                int y = (entity.getCoordinate() / EntityManager.EDGE_FIELD_SIZE) * SIZE_EDGE;
            
                gr.fillRect(x, y, (x + SIZE_EDGE), (y + SIZE_EDGE));
            }
        }
    }
    
    private void paintField(Graphics2D gr) {
        
        super.paintComponent(gr);
        
        gr.setColor(Color.BLACK);
    
        for(int i = 0; i < (SIZE_X / SIZE_EDGE); i += SIZE_EDGE)
            gr.drawLine(i, 0, i, SIZE_Y);
    
        for(int i = 0; i < (SIZE_Y / SIZE_EDGE); i += SIZE_EDGE)
            gr.drawLine(0, i, SIZE_Y, i);
    }
    
    private void start() {
        
        while(!entityManager.isEmpty()) {
            
            System.out.println(1);
            entityManager.nextMove();
            repaint();
    
            try {
                
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                
                e.printStackTrace();
            }
        }
    }
    
    private static class EntityManager {
        
        private static final int RESTART_BOUND = 8;
        private static final int WALL_START_AMOUNT = 8;
        private static final int EAT_START_AMOUNT = RESTART_BOUND * 5;
        private static final int CHANCE_EAT = 40;
        
        private static final int EDGE_FIELD_SIZE = Being.getEdgeFieldSize();
        
        private static final Entity[] field = Entity.getField();
    
        private static final Random random = new Random();
        
        private EntityManager() {
    
            for(int i = 0; i < (RESTART_BOUND * 3); i++) new Being();
            for(int i = 0; i < WALL_START_AMOUNT; i++) new Wall();
            for(int i = 0; i < EAT_START_AMOUNT; i++) new Eat();
        }
        
        boolean isEmpty() {
        
            return Arrays.stream(field).noneMatch(Being.class::isInstance);
        }
    
        public void nextMove() {
        
            List<Being> beings = Arrays.stream(field)
                    .filter(Being.class::isInstance)
                    .map(Being.class::cast)
                    .collect(Collectors.toList());
            
            beings.forEach(Being::move);
            
            if(beings.size() <= RESTART_BOUND)
                beings.forEach(b -> beings.addAll(b.getClones(RESTART_BOUND)));
            
            if(random.nextInt(100) < CHANCE_EAT) new Eat();
        }
    }
}
