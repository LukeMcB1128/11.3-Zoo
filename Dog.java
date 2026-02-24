
import java.awt.*;

public class Dog extends Animal {
    private int direction;

    // supplied by claude
    private static final String[] NAMES = {
        "Buddy", "Max", "Charlie", "Cooper", "Duke", "Bear", "Rocky", "Jack",
        "Bella", "Luna", "Daisy", "Sadie", "Molly", "Rosie", "Lola", "Ruby",
        "Diesel", "Ranger", "Maverick", "Gunner", "Blaze", "Tank", "Titan", "Rex",
        "Biscuit", "Waffle", "Nacho", "Pretzel", "Nugget", "Tater", "Brisket", "Chip",
        "Caesar", "Ares", "Atlas", "Thor", "Odin", "Zeus", "Apollo", "Nero",
        "Sir Barks", "Lord Floof", "Captain Bork", "Señor Paws", "Baron Woofs"
    };
    private static int nameIdx = 0;

    public Dog(int x, int y) {
        super(NAMES[nameIdx % NAMES.length], x, y);
        nameIdx++;
        this.direction = Zoo.rand.nextInt(4);
    }

    @Override
    public void tick(Zoo z) {
        age++;
        hunger++;

        double deathChance = 0;
        if (age > 1000) {
            deathChance = isSick ? 0.01 : 0.001;
        }
        if (deathChance > 0 && Zoo.rand.nextDouble() < deathChance) {
            alive = false;
            Zoo.log("Dog " + name + " died of old age. R.I.P.");
            if (Zoo.rand.nextDouble() < 0.50) {
                z.add(new Dog(Zoo.wrap(x + Zoo.rand.nextInt(3) - 1, Zoo.ZOO_COLS),
                              Zoo.wrap(y + Zoo.rand.nextInt(3) - 1, Zoo.ZOO_ROWS)));
            }
            if (Zoo.rand.nextDouble() < 0.25) {
                z.add(new Dog(Zoo.wrap(x + Zoo.rand.nextInt(3) - 1, Zoo.ZOO_COLS),
                              Zoo.wrap(y + Zoo.rand.nextInt(3) - 1, Zoo.ZOO_ROWS)));
            }
            return;
        }

        for (Entity e : z.at(x, y)) {
            if (e instanceof Food && e.isAlive()) {
                eat((Food) e);
            }
        }

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                for (Entity e : z.at(x + dx, y + dy)) {
                    if (e instanceof Dog && Zoo.rand.nextDouble() < 0.25) {
                        ((Dog) e).direction = this.direction;
                    }
                }
            }
        }

        if (age % 15 == 0) {
            move(z);
        }
    }

    @Override
    public void eat(Food f) {
        if (!f.isAlive()) return;
        if (!f.isAnimalProduct()) return;
        if (hunger > 50) {
            f.beEaten(this);
        } else if (Zoo.rand.nextDouble() < 0.01) {
            f.beEaten(this);
        }
    }

    @Override
    public void move(Zoo z) {
        int[] ddx = {0, 1, 0, -1};
        int[] ddy = {-1, 0, 1, 0};

        int dir = (Zoo.rand.nextDouble() < 0.75) ? direction : Zoo.rand.nextInt(4);

        x = Zoo.wrap(x + ddx[dir], Zoo.ZOO_COLS);
        y = Zoo.wrap(y + ddy[dir], Zoo.ZOO_ROWS);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(139, 90, 43));
        g.setFont(new Font("Consolas", Font.PLAIN, 10));
        int px = Zoo.wrap(x, Zoo.ZOO_COLS) * Zoo.SCALE;
        int py = Zoo.wrap(y, Zoo.ZOO_ROWS) * Zoo.SCALE;
        g.drawString(name, px, py+8);
        g.drawString(getClass().getSimpleName(), px, py + 18);
        // icon for dog, way too distracting
        //g.drawString(" ___ ", px, py + 8);
        //g.drawString("(o o)", px, py + 18);
        //g.drawString(" U U ", px, py + 28);
    }
}
