
import java.util.*;
import java.awt.*;

public class Cat extends Animal {
    private int livesLeft;

    // names from claude
    private static final String[] NAMES = {
        "Whiskers", "Mittens", "Shadow", "Smokey", "Patches", "Tiger", "Felix", "Salem",
        "Luna", "Bella", "Lily", "Cleo", "Nala", "Stella", "Zoe", "Pearl",
        "Onyx", "Raven", "Ghost", "Storm", "Noir", "Eclipse", "Phantom", "Ash",
        "Biscuit", "Mochi", "Waffle", "Pretzel", "Noodle", "Dumpling", "Cocoa", "Butterscotch",
        "Cleopatra", "Artemis", "Athena", "Caesar", "Merlin", "Osiris", "Freya", "Odin",
        "Lord Fluffington", "Sir Pounce", "Chairman Meow", "Catrick Swayze", "Purrlock Holmes"
    };
    private static int nameIdx = 0;

    public Cat(int x, int y) {
        super(NAMES[nameIdx % NAMES.length], x, y);
        nameIdx++;
        this.livesLeft = 9;
    }

    @Override
    public void tick(Zoo z) {
        age++;
        hunger++;

        double deathChance = 0;
        if (age > 500) {
            deathChance = isSick ? 0.10 : 0.01;
        }
        if (isSick && deathChance < 0.001) {
            deathChance = 0.001;
        }
        if (deathChance > 0 && Zoo.rand.nextDouble() < deathChance) {
            livesLeft--;
            if (livesLeft <= 0) {
                alive = false;
                Zoo.log("Cat " + name + " died of old age. R.I.P.");
                return;
            }
            age = 0;
        }

        for (Entity e : z.at(x, y)) {
            if (e instanceof Food && e.isAlive()) {
                eat((Food) e);
            }
        }

        if (age % 10 == 0) {
            move(z);
        }

        boolean foundAdjacentCat = false;
        outer:
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                for (Entity e : z.at(x + dx, y + dy)) {
                    if (e instanceof Cat) {
                        foundAdjacentCat = true;
                        break outer;
                    }
                }
            }
        }
        if (foundAdjacentCat && Zoo.rand.nextDouble() < 0.10) {
            int nx = Zoo.wrap(x + Zoo.rand.nextInt(3) - 1, Zoo.ZOO_COLS);
            int ny = Zoo.wrap(y + Zoo.rand.nextInt(3) - 1, Zoo.ZOO_ROWS);
            z.add(new Cat(nx, ny));
        }
    }

    @Override
    public void eat(Food f) {
        if (!f.isAlive()) return;
        if (f.isAnimalProduct() && hunger > 25 && Zoo.rand.nextDouble() < 0.99) {
            f.beEaten(this);
        }
    }

    @Override
    public void move(Zoo z) {
        int[] ddx = {0, 0, -1, 1};
        int[] ddy = {-1, 1, 0, 0};

        for (int i = 0; i < 4; i++) {
            int nx = x + ddx[i];
            int ny = y + ddy[i];
            for (Entity e : z.at(nx, ny)) {
                if (e instanceof Food && e.isAlive()) {
                    x = Zoo.wrap(nx, Zoo.ZOO_COLS);
                    y = Zoo.wrap(ny, Zoo.ZOO_ROWS);
                    return;
                }
            }
        }

        int dir = Zoo.rand.nextInt(4);
        int nx = x + ddx[dir];
        int ny = y + ddy[dir];

        boolean hasAnimal = false;
        for (Entity e : z.at(nx, ny)) {
            if (e instanceof Animal && !(e instanceof Rat)) {
                hasAnimal = true;
                break;
            }
        }
        if (hasAnimal) {
            int opposite = (dir + 2) % 4;
            nx = x + ddx[opposite];
            ny = y + ddy[opposite];
        }

        x = Zoo.wrap(nx, Zoo.ZOO_COLS);
        y = Zoo.wrap(ny, Zoo.ZOO_ROWS);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.setFont(new Font("Consolas", Font.PLAIN, 10));
        int px = Zoo.wrap(x, Zoo.ZOO_COLS) * Zoo.SCALE;
        int py = Zoo.wrap(y, Zoo.ZOO_ROWS) * Zoo.SCALE;
        g.drawString(name, px, py+8);
        g.drawString(getClass().getSimpleName(), px, py + 18);
        // once again, removing the icon as it has way too much stuff going on
        //g.drawString(" ^-^ ", px, py + 8);
        //g.drawString("/. .\\", px, py + 18);
        //g.drawString("\\_o_/", px, py + 28);
    }
}
