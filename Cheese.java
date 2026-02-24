
import java.awt.*;

public class Cheese extends Entity implements Food {
    private int timesEaten;
    private boolean isAnimalProduct;
    private boolean isVegetableProduct;
    private int nutritionalValue;

    private static int cheeseCount = 0;

    public Cheese(int x, int y) {
        super("Cheese #" + cheeseCount++, x, y);
        this.isAnimalProduct = false;
        this.isVegetableProduct = true;
        this.nutritionalValue = 10;
        this.timesEaten = 0;
    }

    @Override
    public boolean isAnimalProduct() { return isAnimalProduct; }

    @Override
    public boolean isVegetableProduct() { return isVegetableProduct; }

    @Override
    public void tick(Zoo z) {
        age++;
        if (age > 400 && Zoo.rand.nextDouble() < 0.01) {
            alive = false;
            Zoo.log(name + " got old and rotted away.");
        }
    }

    @Override
    public void beEaten(Animal a) {
        int nutrition;
        if (timesEaten == 0) nutrition = 10;
        else if (timesEaten == 1) nutrition = 8;
        else nutrition = 5;

        timesEaten++;

        if (age > 400) {
            if (!(a instanceof Rat)) {
                a.isSick = true;
                Zoo.log(a.getClass().getSimpleName() + " " + a.name + " got food poisioning from eating " + name + ".");
            }
            Zoo.log(a.getClass().getSimpleName() + " " + a.name + " ate " + name + ", gaining 0 nutrition!");
        } else {
            int minHunger = (a instanceof Rat) ? 20 : 0;
            a.hunger = Math.max(minHunger, a.hunger - nutrition);
            Zoo.log(a.getClass().getSimpleName() + " " + a.name + " ate " + name + ", gaining " + nutrition + " nutrition!");
        }

        if (timesEaten >= 3) alive = false;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        int px = Zoo.wrap(x, Zoo.ZOO_COLS) * Zoo.SCALE;
        int py = Zoo.wrap(y, Zoo.ZOO_ROWS) * Zoo.SCALE;
        g.fillOval(px + 5, py + 5, 20, 20);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Consolas", Font.PLAIN, 10));
        g.drawString("Cheese", px + 0, py + 20);
    }
}
