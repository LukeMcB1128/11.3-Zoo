
import java.awt.*;

public class Rat extends Animal implements Food {
    private int direction;
    private int dirChangeTimer;

    // rat names supplied by claude
    private static final String[] NAMES = {
        "Pip", "Remy", "Biscuit", "Nugget", "Peanut", "Bean", "Pebble", "Dot",
        "Sir Squeaks", "Lord Cheese", "Captain Nibbles", "Professor Whiskers", "Tater Tot",
        "Raven", "Shadow", "Onyx", "Venom", "Ghost",
        "Caesar", "Napoleon", "Bismarck", "Voltaire", "Machiavelli",
        "Pretzel", "Waffle", "Mochi", "Churro", "Nacho", "Dumpling"
    };
    private static int nameIdx = 0;

    public Rat(int x, int y) {
        super(NAMES[nameIdx % NAMES.length], x, y);
        nameIdx++;
        this.direction = Zoo.rand.nextInt(4);
        this.dirChangeTimer = 40 + Zoo.rand.nextInt(21);
        this.hunger = 20;
    }

    @Override
    public void tick(Zoo z) {
        age++;
        hunger++;
        hunger = Math.max(20, hunger);

        double deathChance = 0;
        if (age > 500) {
            deathChance = isSick ? 0.20 : 0.015;
        }
        if (isSick && deathChance < 0.01) {
            deathChance = 0.01;
        }
        if (deathChance > 0 && Zoo.rand.nextDouble() < deathChance) {
            alive = false;
            Zoo.log("Rat " + name + " died of old age. R.I.P.");
            return;
        }

        for (Entity e : z.at(x, y)) {
            if (e instanceof Food && e.isAlive()) {
                eat((Food) e);
            }
        }

        dirChangeTimer--;
        if (dirChangeTimer <= 0) {
            direction = Zoo.rand.nextInt(4);
            dirChangeTimer = 40 + Zoo.rand.nextInt(21);
        }

        if (age % 5 == 0) {
            move(z);
        }

        if (age % 50 == 0 && Zoo.rand.nextDouble() < 0.10) {
            z.add(new Rat(Zoo.wrap(x + Zoo.rand.nextInt(3) - 1, Zoo.ZOO_COLS),
                          Zoo.wrap(y + Zoo.rand.nextInt(3) - 1, Zoo.ZOO_ROWS)));
        }
    }

    @Override
    public void eat(Food f) {
        if (!f.isAlive()) return;
        if (f instanceof Cheese) {
            f.beEaten(this);
        } else if (hunger > 50) {
            f.beEaten(this);
        }
    }

    @Override
    public void beEaten(Animal a) {
        alive = false;
        a.hunger = Math.max(0, a.hunger - 10);
        Zoo.log(a.getClass().getSimpleName() + " " + a.name + " ate " + name + ", gaining 10 nutrition!");
    }

    @Override
    public boolean isAnimalProduct() { return true; }

    @Override
    public boolean isVegetableProduct() { return false; }

    @Override
    public void move(Zoo z) {
        int[] ddx = {0, 1, 0, -1};
        int[] ddy = {-1, 0, 1, 0};

        for (int i = 0; i < 4; i++) {
            int nx = x + ddx[i];
            int ny = y + ddy[i];
            for (Entity e : z.at(nx, ny)) {
                if (e instanceof Cheese && e.isAlive()) {
                    x = Zoo.wrap(nx, Zoo.ZOO_COLS);
                    y = Zoo.wrap(ny, Zoo.ZOO_ROWS);
                    return;
                }
            }
        }

        x = Zoo.wrap(x + ddx[direction], Zoo.ZOO_COLS);
        y = Zoo.wrap(y + ddy[direction], Zoo.ZOO_ROWS);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        g.setFont(new Font("Consolas", Font.PLAIN, 10));
        int px = Zoo.wrap(x, Zoo.ZOO_COLS) * Zoo.SCALE;
        int py = Zoo.wrap(y, Zoo.ZOO_ROWS) * Zoo.SCALE;
        g.drawString(name, px, py+8);
        g.drawString(getClass().getSimpleName(), px, py+18);
        // removing this as it had way too much clutter
        //g.drawString(" ,__, ", px, py + 8);
        //g.drawString("(o  o)", px, py + 18);
        //g.drawString(" ~--~ ", px, py + 28);
    }
}
