
import java.awt.Graphics;

public abstract class Entity {
    private static int lastID = 0;
    protected int id;
    protected String name;
    protected int age;
    protected int x;
    protected int y;
    protected boolean alive;

    public Entity(String name, int x, int y) {
        this.id = lastID;
        lastID = lastID + 1;
        this.name = name;
        this.x = x;
        this.y = y;
        this.age = 0;
        this.alive = true;
    }

    public abstract void draw(Graphics g);
    public abstract void tick(Zoo z);

    public boolean isAlive() {
        return alive;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
