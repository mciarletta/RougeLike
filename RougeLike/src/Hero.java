public class Hero extends GameObject {

    private final String name;
    private final String symbol;


    private int x;
    private int y;

    // Create a hero with a name and an initial position.
    public Hero(String name, String symbol, int x, int y) {
        this.name = name;
        this.symbol = symbol;
        this.x = x;
        this.y = y;
    }

    // getters
    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
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


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}




