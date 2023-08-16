public class Treasure extends GameObject {
    
    private final int x;
    private final int y;



    public Treasure(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String getSymbol() {
        return String.format("%s%s%s","\033[33m","T", "\033[0m");
    }

    @Override
    public boolean getBlock() {
        return false;
    }

    @Override
    public void move(int x, int y) {

    }


}
