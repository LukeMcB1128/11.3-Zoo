// really liked this project, gonna turn it into my own evolution project.


import javax.swing.*;
import java.awt.*;

import java.util.*;

// in general it would be better practice to separate the data and display functionalities of Zoo
// into separate classes - they're combined here for simplicity and ease of understanding
public class Zoo extends JPanel {

    // zoo grid size
    public static final int ZOO_ROWS = 30; // grid height
    public static final int ZOO_COLS = 40; // grid width
    // screen size is the zoo grid sizes * SCALE
    public static final int SCALE = 30;

    // use this single Random object to generate ANY and ALL random numbers you need
    // there's an important reason why that's too long to explain in comments but ask me if you're curious 
    public static Random rand = new Random();

    //

    private int width, height;
    private ArrayList<ArrayList<LinkedList<Entity>>> grid;

    // write the updates in the top left corner of simulation
    private static final int maxLog = 8;
    public static final ArrayList<String> logs = new ArrayList<>(maxLog);

    public static void log(String msg) {
        logs.add(msg);
        if (logs.size() > maxLog) logs.remove(0);
    }

    public Zoo(int w, int h) {
        // initalize the grid using ArrayLists for the rows and colums and LinkedLists for the cell stack
        grid = new ArrayList<>(h);

        for(int y = 0; y < h; y++) {
            ArrayList<LinkedList<Entity>> row = new ArrayList<>(w);
            for(int x = 0; x < w; x++) {
                row.add(new LinkedList<Entity>());
            }
            grid.add(row);
        }
        width = w;
        height = h;
    }

	public void paintComponent(Graphics g){
		super.paintComponent(g); 
		setBackground(Color.GREEN);

        // draw cell grids
        g.setColor(new Color(0, 200, 0)); // dark green
        for(int y = 0; y < height; y++) {
            g.drawLine(0, y * SCALE, width * SCALE, y * SCALE);
        }
        for(int x = 0; x < width; x++) {
            g.drawLine(x * SCALE, 0, x * SCALE, height * SCALE);
        }

        // draw Entities
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                for(Entity e : grid.get(y).get(x)) {
                    e.draw(g);
                }
            }
        }

        // draw the logs
        g.setColor(new Color(0,0,0,150));
        g.fillRect(5, 5, 350, maxLog * 15 + 10);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.PLAIN, 11));
        for (int i = 0; i < logs.size(); i++) {
            g.drawString(logs.get(i), 10, 20 + i * 15);
        }
	}

    // iterates through each cell in the grid, calling tick(Zoo)
    public void tick() {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {

                // iterate backwards through the list because we might remove elements
                // removing an element in forward iteration would cause the remaining elements to shift back
                // causing us to skip the element immediately following the skipped element
                for(int i = grid.get(y).get(x).size() - 1; i >= 0; i--) {

                    // removing e from the grid means that e cannot interact with itself
                    // in its tick(Zoo) method
                    Entity e = grid.get(y).get(x).remove(i);

                    if(e.isAlive()) {
                        e.tick(this);
                        grid.get(wrap(e.getY(), height)).get(wrap(e.getX(), width)).add(e);
                    }
                }
            }
        }
    }

    // get a list of Entities at position x, y in the grid
    public ArrayList<Entity> at(int x, int y) {
        int atX = wrap(x, width);
        int atY = wrap(y, height);
        // ArrayList constructor copies references from the passed LinkedList
        return new ArrayList<Entity>(grid.get(atY).get(atX));
    }

    // add an Entity to the grid
    public void add(Entity e) {
        int atX = wrap(e.getX(), width);
        int atY = wrap(e.getY(), height);
        grid.get(atY).get(atX).add(e);
    }

    // wrap a val between 0 and thresh
    public static int wrap(int val, int thresh) {
        if(val >= 0) {
            return val % thresh;
        }
        else {
            return (thresh - val) % thresh;
        }
    }

    public static void main(String[] args) {
        // main Zoo object
        Zoo zoo = new Zoo(ZOO_COLS, ZOO_ROWS);

        JFrame frame = new JFrame("Zoo");
		frame.setSize(ZOO_COLS * SCALE + SCALE/2, ZOO_ROWS * SCALE + SCALE/2 + 23);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.add(zoo);
		frame.setVisible(true);

        for (int i = 0; i < 5; i++) {
            zoo.add(new Cat(rand.nextInt(ZOO_COLS), rand.nextInt(ZOO_ROWS)));
            zoo.add(new Dog(rand.nextInt(ZOO_COLS), rand.nextInt(ZOO_ROWS)));
        }
        for (int i = 0; i < 3; i++) {
            zoo.add(new Rat(rand.nextInt(ZOO_COLS), rand.nextInt(ZOO_ROWS)));
        }
        for (int i = 0; i < 10; i++) {
            zoo.add(new Cheese(rand.nextInt(ZOO_COLS), rand.nextInt(ZOO_ROWS)));
            zoo.add(new Ham(rand.nextInt(ZOO_COLS), rand.nextInt(ZOO_ROWS)));
        }

        int tickCount = 0;
        while(true) {
            try {
                Thread.sleep(100);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }

            if (tickCount % 50 == 0 && tickCount > 0) {
                zoo.add(new Cheese(rand.nextInt(ZOO_COLS), rand.nextInt(ZOO_ROWS)));
                zoo.add(new Ham(rand.nextInt(ZOO_COLS), rand.nextInt(ZOO_ROWS)));
            }

            zoo.tick();

            // redraw the frame
            zoo.revalidate();
            zoo.repaint();

            tickCount++;
        }
    }

}
