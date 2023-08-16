import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class Game {

    // constants
    private static int HEIGHT;

    private static int WIDTH;
    private static int VP_HEIGHT = 10;
    private static int VP_WIDTH = 40;

    //this displays how much you can see around the hero
    private static int TORCH_RADIUS = 5;
    private static String BLACK_SPACE = String.format("%s%s%s","\033[36m","*", "\033[0m");

    private final static String EMPTY_CHARACTER = " ";

    private final Scanner console = new Scanner(System.in);
    private Hero hero;
    private boolean isOver;
    private Random rand = new Random();

    private ArrayList<String> messages = new ArrayList<>();


    private ArrayList<GameObject> gameObjects = new ArrayList<>();

    public void run() throws InterruptedException {
        setUp();
        while (!isOver) {
            //hero turn
            printWorld();
            moveHero();
            eraseWorld();
            printWorld();
            updateTouches();
            if (isOver) break;
            checkWinningCondition();

            //enemy turn
            moveEnemies();
            eraseWorld();
            updateTouches();
        }
        printWorld();

    }

    private void setUp() {
        System.out.print("What is the name of your hero?: ");
        String name = console.nextLine();

        String heroSym = setHeroSymbol(); //set the hero name


        setWorldWidth(); //update the WIDTH to a user input

        //call a method that makes the room
        makeRoom();

        //add the hero
        gameObjects.add((createGameObject("Hero", name, heroSym)));

        //add some treasure
        int random = rand.nextInt(3) + 1; //+1 so that it is never 0
        for (int i = 0; i < random; i++) {
            gameObjects.add(createGameObject("Treasure"));
        }

        //add some traps
        random = rand.nextInt(5) + 1; //+1 so that it is never 0
        for (int i = 0; i < random; i++) {
            gameObjects.add(createGameObject("Trap"));
        }

        //add some monsters
        random = rand.nextInt(10) + 1; //+1 so that it is never 0
        for (int i = 0; i < random; i++) {
            gameObjects.add(createGameObject("Enemy"));
        }

    }

    private void printWorld() {
        //print VP top bar
        System.out.println("-".repeat(VP_WIDTH));

        //for the messages
        Iterator<String> i = messages.iterator();

        //adjust for the view port if the room is bigger than it
        //get the hero location first
        int heroX = hero.getX();
        int heroY = hero.getY();
        int midY = VP_HEIGHT/2;
        int midX = VP_WIDTH/2;
        int yOffSetTop = 0;
        int xOffsetRight = 0;
        int yOffSetBottom = 0;
        int xOffsetLeft = 0;

        //check to see if an offset is needed

        //print the top part and only midY of the bottom
        if (heroY <= midY){
            yOffSetTop = 2*midY;
        } else {
            //if the hero is closer to the bottom, we still want to print up to midY
            if (HEIGHT - heroY < midY){
                yOffSetTop = HEIGHT; //print to the height
                yOffSetBottom = HEIGHT - 2*midY;
            } else {
                //the hero is in the middle somewhere
                yOffSetTop = heroY + midY;
                //find the bottom offset
                yOffSetBottom = heroY - midY;
            }
        }

        if (heroX <= midX){
            xOffsetRight = 2*midX;
        } else {
            //if the hero is closer to the right
            if (WIDTH - heroX < midX){
                xOffsetRight = WIDTH; //print to the width
                xOffsetLeft = WIDTH - 2*midX;
            } else {
                //the hero is in the middle somewhere
                xOffsetRight =heroX + midX;
                //find the bottom offset
                xOffsetLeft =heroX - midX;
            }
        }


        for (int row = yOffSetBottom; row < yOffSetTop; row++) {
            for (int col = xOffsetLeft; col < xOffsetRight; col++) {
                if (Math.abs(row - hero.getY()) < TORCH_RADIUS && Math.abs(col - hero.getX()) < TORCH_RADIUS) {
                    String toPrint = EMPTY_CHARACTER;
                    for (GameObject obj : gameObjects) {
                        if (row == obj.getY() && col == obj.getX()) {
                            toPrint = obj.getSymbol();
                        }
                    }
                    System.out.print(toPrint);
                } else {
                    System.out.print(BLACK_SPACE);
                }
            }
            //reached the end of the columns
            //print messages
            if (i.hasNext()){
                System.out.printf("\t %s", i.next());
                i.remove();
            }
            // so print a line break
            System.out.println();
        }
        //print VP bottom bar
        System.out.println("-".repeat(VP_WIDTH));

    }


    private void moveHero() {

        System.out.print(hero.getName() + ", move [WASD]: ");
        String move = console.nextLine().trim().toUpperCase();

        if (move.length() != 1) {
            return;
        }
        int xMod = 0;
        int yMod = 0;

        switch (move.charAt(0)) {
            case 'W':
                yMod = -1;
                break;
            case 'A':
                xMod = -1;
                break;
            case 'S':
                yMod = 1;
                break;
            case 'D':
                xMod = 1;
                break;
        }

        if (getObjectPositions(hero, xMod, yMod)) hero.move(xMod, yMod);


    }

    private void moveEnemies() {

        for (GameObject obj : gameObjects) {
            if (obj instanceof Enemy) {
                if (rand.nextInt(10) > 7) {
                    moveRandomly(obj);
                } else {
                    moveToHero(obj);
                }
            }
        }
    }

    private void moveRandomly(GameObject obj) {

        int random = rand.nextInt(4);
        int xMod = 0;
        int yMod = 0;
        switch (random) {
            case 0:
                yMod = -1;
                break;
            case 1:
                xMod = -1;
                break;
            case 2:
                yMod = 1;
                break;
            default:
                xMod = 1;
                break;
        }
        if (getObjectPositions(obj, xMod, yMod)) obj.move(xMod, yMod);

    }

    private void moveToHero(GameObject obj) {

        int xDif = hero.getX() - obj.getX();
        int yDif = hero.getY() - obj.getY();
        int xMod = 0;
        int yMod = 0;
        if (Math.abs(xDif) >= Math.abs(yDif)) {
            //move horizontally
            if (xDif > 0) {
                //move right
                xMod = 1;
            } else {
                //move left
                xMod = -1;
            }
        } else if (yDif > 0){
            //move up
            yMod = 1;
        } else {
            //move down
            yMod = -1;
        }

        if (getObjectPositions(obj, xMod, yMod)) obj.move(xMod, yMod);


    }


    private void setWorldWidth() {
        System.out.print("What would you like the average room width to be? : ");
        HEIGHT = Integer.parseInt(console.nextLine());
        //for now LENGTH = WIDTH, but for the future, it might be useful to have a length
        WIDTH = HEIGHT;
    }

    private String setHeroSymbol() {
        System.out.print("What symbol would you like for your hero? : ");
        String input = console.nextLine();
        input = String.format("%s%s%s","\033[92m",input, "\033[0m");
        return input;
    }

    public GameObject createGameObject(String type, String name, String symbol) {
        int x;
        int y;
        int objX = 0;
        int objY = 0;
        int[] positions = new int[2];
        GameObject gameObj;

        do {
            x = rand.nextInt(HEIGHT);
            y = rand.nextInt(HEIGHT);

            //if an object has these coordinates, break out of the loop to make new x and y
            for (GameObject obj : gameObjects) {
                objX = obj.getX();
                objY = obj.getY();
                if ((objX == x) && (objY == y)) {
                    break;
                }
            }
        } while (objX == x && objY == y);

        positions = new int[]{x, y};


        if (type.equals("Treasure")) {
            gameObj = new Treasure(positions[0], positions[1]);

        } else if (type.equals("Hero")) {
            hero = new Hero(name, symbol, positions[0], positions[1]);
            gameObj = hero;

        } else if (type.equals("Trap")) {
            gameObj = new Trap(positions[0], positions[1]);

        } else if (type.equals("Enemy")) {
            gameObj = new Enemy(positions[0], positions[1]);

        }else { //default for return
            gameObj = new Treasure(positions[0], positions[1]);

        }

        return gameObj;
    }

    public GameObject createGameObject(String type) {
        return createGameObject(type, "blank", "x");
    }


    public void foundTreasure() {
        messages.add(hero.getName() + " found the treasure! Nice.");
        //System.out.println(hero.getName() + " found the treasure! Nice.");
    }

    public void gameOver(String result) {
        switch (result) {
            case "Win":
                messages.add("Congratulations! You found all the treasure.");
                //System.out.println("Congratulations! You found all the treasure.");
                isOver = true;
                break;
            case "Lose":
                messages.add("You died! Game over, man.");
                //System.out.println("You died! Game over, man.");
                isOver = true;
                break;

        }
    }

    public void checkWinningCondition() {
        //check for the winning condition: no more treasure to be found

        int treasures = 0;
        for (GameObject gobj : gameObjects) {
            if (gobj instanceof Treasure) {
                treasures++;
            }
        }
        if (treasures == 0) {
            gameOver("Win");
        }
    }

    public void makeRoom() {
        //will need walls equal to 4*WIDTH plus 4 corners
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                if (row == 0 || row == HEIGHT - 1) {
                    GameObject wall = new Wall(row, col);
                    gameObjects.add(wall);
                } else if (col == 0 || col == WIDTH - 1) {
                    GameObject wall = new Wall(row, col);
                    gameObjects.add(wall);
                }
            }
        }
    }

    public boolean getObjectPositions(GameObject character, int xMod, int yMod) {
        boolean canMove = true;
        for (GameObject obj : gameObjects) {
            //check to see if there is an object in the way
            //if the object .getBlock is true, then you cannot walk on it
            if (obj.getBlock() &&
                    character.getX() + xMod == obj.getX() &&
                    character.getY() + yMod == obj.getY()) {
                return false;
            }

        }
        return canMove;
    }

    public void updateTouches() throws InterruptedException {
        Iterator<GameObject> i = gameObjects.iterator();
        while (i.hasNext()){
            GameObject obj = i.next();
            if (hero.getX() == obj.getX() && hero.getY() == obj.getY()) {
                if (obj instanceof Treasure) {
                    foundTreasure();
                    i.remove();
                }
                if (obj instanceof Trap) {
                    messages.add("You fell into a lava trap! What's wrong with you?");
                    gameOver("Lose");
                    return;
                }
            }
            //check if enemies are touching
            if (obj instanceof Enemy
                    &&
                    (Math.abs(hero.getX() - obj.getX()) <= 1 && hero.getY() == obj.getY())
                    ||
                    (Math.abs(hero.getY() - obj.getY()) <= 1) && hero.getX() == obj.getX()) {
                //begin combat sequence with this enemy
                //if (fight(obj)) i.remove();
                if (obj.fight(messages)) i.remove();
            }
        }
    }

    private void eraseWorld(){
        System.out.print("\033[s"); //save the cursor position

        for (int i = 0; i < HEIGHT +3; i++){
            System.out.print("\033[1A"); //move up 1 lines
            System.out.print("\033[2K"); //erase the entire line

        }
        //System.out.print("\033[u"); //return the cursor to the saved position


    }
}

