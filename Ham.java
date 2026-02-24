
import java.awt.*;

public class Ham extends Entity implements Food {
    private boolean isAnimalProduct;
    private boolean isVegetableProduct;
    private static int hamCount = 0;

    public Ham(int x, int y) {
        super("Ham #" + hamCount++, x, y);
        this.isAnimalProduct = true;
        this.isVegetableProduct = false;
    }

    @Override
    public boolean isAnimalProduct() { return isAnimalProduct; }

    @Override
    public boolean isVegetableProduct() { return isVegetableProduct; }

    @Override
    public void tick(Zoo z) {
        age++;
    }

    @Override
    public void beEaten(Animal a) {
        alive = false;
        if (age >= 200) {
            a.hunger += 5;
            a.isSick = true;
            Zoo.log(a.getClass().getSimpleName() + " " + a.name + " got food poisioning from eating " + name + ".");
            Zoo.log(a.getClass().getSimpleName() + " " + a.name + " ate " + name + ", gaining -5 nutrition!");
        } else {
            a.hunger = Math.max(0, a.hunger - 15);
            Zoo.log(a.getClass().getSimpleName() + " " + a.name + " ate " + name + ", gaining 15 nutrition!");
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(220, 100, 100));
        int px = Zoo.wrap(x, Zoo.ZOO_COLS) * Zoo.SCALE;
        int py = Zoo.wrap(y, Zoo.ZOO_ROWS) * Zoo.SCALE;
        g.fillRect(px + 3, py + 3, 24, 24);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Consolas", Font.PLAIN, 10));
        g.drawString("Ham", px+4, py + 20);
    }
}
