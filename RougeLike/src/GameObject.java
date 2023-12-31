import java.util.ArrayList;

public abstract class GameObject {
    public abstract int getX();
    public abstract int getY();

    public abstract String getSymbol();

    public abstract boolean getBlock();

    public abstract void move(int x, int y);

    public abstract boolean fight(ArrayList<String> messages) throws InterruptedException;



}
