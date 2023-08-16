import java.util.ArrayList;
import java.util.Random;

public class Enemy extends GameObject {
    private int x;
    private int y;

    private String symbol = String.format("%s%s%s", "\033[91m", "E", "\033[0m");

    private boolean isdead = false;

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
            return symbol;

    }

    @Override
    public boolean getBlock() {
        return true;
    }

    @Override
    public void move(int x, int y) {
        if (!isdead) {
            this.x += x;
            this.y += y;
        }
    }

    @Override
    public boolean fight(ArrayList<String> messages) throws InterruptedException {
        //for now, I will just make it random with only two outcomes
        Random rand = new Random();
        int outcome = rand.nextInt(2);

        switch (outcome){
            case 0:
                messages.add("An enemy attacks the hero! Watch out!");
                //System.out.println("An enemy attacks the hero! Watch out!");
                break;
            case 1:
                messages.add("The hero gets the upper hand and slays the enemy");
                //System.out.println("The hero gets the upper hand and slays the enemy");
                return true;

        }
        return false;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
