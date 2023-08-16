<<<<<<< HEAD
public class Enemy extends GameObject {
    private int x;
    private int y;

    public Enemy(int x, int y) {
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
        return String.format("%s%s%s","\033[91m","E", "\033[0m");
    }

    @Override
    public boolean getBlock() {
        return true;
    }

    @Override
    public void move(int x, int y) {
        this.x += x;
        this.y += y;
    }
}
=======
public class Enemy extends GameObject {
    private int x;
    private int y;

    public Enemy(int x, int y) {
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
        return String.format("%s%s%s","\033[91m","E", "\033[0m");
    }

    @Override
    public boolean getBlock() {
        return true;
    }

    @Override
    public void move(int x, int y) {
        this.x += x;
        this.y += y;
    }
}
>>>>>>> fab4c20efb4f1a66f4408c57d2a2009c05b0cc4c
