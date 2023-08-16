import java.util.ArrayList;

public class Trap extends GameObject {
    private final int x;
    private final int y;

    public Trap(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public String getSymbol() {
        return String.format("%s%s%s","\033[31m","M", "\033[0m");
    }

    @Override
    public boolean getBlock() {
        return false;
    }

    @Override
    public void move(int x, int y) {

    }

    @Override
    public boolean fight(ArrayList<String> messages) {
    return false;
    }
}
