import java.util.ArrayList;

public class Wall extends GameObject{
    private final int x;
    private final int y;

    public Wall(int x, int y) {
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
        return "#";
    }

    @Override
    public boolean getBlock() {
        return true;
    }

    @Override
    public void move(int x, int y) {

    }

    @Override
    public boolean fight(ArrayList<String> messages) {
    return false;
    }
}
