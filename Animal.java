public abstract class Animal extends Entity {
    protected int hunger;
    protected boolean isSick;

    public Animal(String name, int x, int y) {
        super(name, x, y);
        this.hunger = 0;
        this.isSick = false;
        Zoo.log(getClass().getSimpleName() + " " + name + " was born today!");
    }

    public abstract void eat(Food f);
    public abstract void move(Zoo z);
}
