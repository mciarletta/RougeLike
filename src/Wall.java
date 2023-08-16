<<<<<<< HEAD
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
}
=======
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
}
>>>>>>> fab4c20efb4f1a66f4408c57d2a2009c05b0cc4c
