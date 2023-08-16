<<<<<<< HEAD
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game {

    // constants
    private static int WIDTH;

    private static int LENGTH;

    //this displays how much you can see around the hero
    private static int TORCH_RADIUS = 5;
    private static String BLACK_SPACE = String.format("%s%s%s","\033[36m","*", "\033[0m");

    private final static String EMPTY_CHARACTER = " ";

    private final Scanner console = new Scanner(System.in);
    private Hero hero;
    private boolean isOver;
    private Random rand = new Random();


    private ArrayList<GameObject> gameObjects = new ArrayList<>();

    public void run() {
        setUp();
        while (!isOver) {
            printWorld();
            moveHero();
            //eraseWorld();
            printWorld();
            updateTouches();
            checkWinningCondition();
            moveEnemies();
            //eraseWorld();
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
        for (int row = 0; row < WIDTH; row++) {
            for (int col = 0; col < WIDTH; col++) {
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
            //reached the end of the columns, so print a line break
            System.out.println();
        }
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
        WIDTH = Integer.parseInt(console.nextLine());
        //for now LENGTH = WIDTH, but for the future, it might be useful to have a length
        LENGTH = WIDTH;
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
            x = rand.nextInt(WIDTH);
            y = rand.nextInt(WIDTH);

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
        System.out.println(hero.getName() + " found the treasure! Nice.");
    }

    public void gameOver(String result) {
        switch (result) {
            case "Win":
                System.out.println("Congratulations! You found all the treasure.");
                isOver = true;
                return;
            case "Lose":
                System.out.println("You died! Game over, man.");
                isOver = true;

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
        for (int row = 0; row < WIDTH; row++) {
            for (int col = 0; col < LENGTH; col++) {
                if (row == 0 || row == WIDTH - 1) {
                    GameObject wall = new Wall(row, col);
                    gameObjects.add(wall);
                } else if (col == 0 || col == LENGTH - 1) {
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

    public void updateTouches() {
        ArrayList<GameObject> toRemove = new ArrayList<>();
        for (GameObject obj : gameObjects) {
            if (hero.getX() == obj.getX() && hero.getY() == obj.getY()) {
                if (obj instanceof Treasure) {
                    foundTreasure();
                    toRemove.add(obj);
                }
                if (obj instanceof Trap) {
                    gameOver("Lose");
                    return;
                }
            }
            //check if enemies are touching
            if (obj instanceof Enemy &&
                    Math.abs(hero.getX() - obj.getX()) <= 1 &&
                    Math.abs(hero.getY() - obj.getY()) <= 1) {
                //begin combat sequence with this enemy
                if (fight(obj)) toRemove.add(obj);
            }
        }
        //remove objects
        for (GameObject obj : toRemove) {
            gameObjects.remove(obj);
        }
    }

    public boolean fight(GameObject obj){
        //for now, I will just make it random with only two outcomes
        int move = rand.nextInt(2);
        boolean enemyKilled = false;
        switch (move){
            case 0:
                System.out.println("An enemy attacks the hero! Watch out!");
                break;
            case 1:
                System.out.println("The hero gets the upper hand and slays the enemy");
                enemyKilled = true;
                break;
        }
        return enemyKilled;
    }

    private void eraseWorld(){
        System.out.print("\033[s"); //save the cursor position

        for (int i = 0; i < WIDTH+3; i++){
            System.out.print("\033[1A"); //move up 1 lines
            System.out.print("\033[2K"); //erase the entire line

        }
        //System.out.print("\033[u"); //return the cursor to the saved position


    }
}
=======
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game {

    // constants
    private static int WIDTH;

    private static int LENGTH;

    //this displays how much you can see around the hero
    private static int TORCH_RADIUS = 5;
    private static String BLACK_SPACE = String.format("%s%s%s","\033[36m","*", "\033[0m");

    private final static String EMPTY_CHARACTER = " ";

    private final Scanner console = new Scanner(System.in);
    private Hero hero;
    private boolean isOver;
    private Random rand = new Random();


    private ArrayList<GameObject> gameObjects = new ArrayList<>();

    public void run() {
        setUp();
        while (!isOver) {
            printWorld();
            moveHero();
            //eraseWorld();
            printWorld();
            updateTouches();
            checkWinningCondition();
            moveEnemies();
            //eraseWorld();
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
        for (int row = 0; row < WIDTH; row++) {
            for (int col = 0; col < WIDTH; col++) {
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
            //reached the end of the columns, so print a line break
            System.out.println();
        }
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
        WIDTH = Integer.parseInt(console.nextLine());
        //for now LENGTH = WIDTH, but for the future, it might be useful to have a length
        LENGTH = WIDTH;
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
            x = rand.nextInt(WIDTH);
            y = rand.nextInt(WIDTH);

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
        System.out.println(hero.getName() + " found the treasure! Nice.");
    }

    public void gameOver(String result) {
        switch (result) {
            case "Win":
                System.out.println("Congratulations! You found all the treasure.");
                isOver = true;
                return;
            case "Lose":
                System.out.println("You died! Game over, man.");
                isOver = true;

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
        for (int row = 0; row < WIDTH; row++) {
            for (int col = 0; col < LENGTH; col++) {
                if (row == 0 || row == WIDTH - 1) {
                    GameObject wall = new Wall(row, col);
                    gameObjects.add(wall);
                } else if (col == 0 || col == LENGTH - 1) {
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

    public void updateTouches() {
        ArrayList<GameObject> toRemove = new ArrayList<>();
        for (GameObject obj : gameObjects) {
            if (hero.getX() == obj.getX() && hero.getY() == obj.getY()) {
                if (obj instanceof Treasure) {
                    foundTreasure();
                    toRemove.add(obj);
                }
                if (obj instanceof Trap) {
                    gameOver("Lose");
                    return;
                }
            }
            //check if enemies are touching
            if (obj instanceof Enemy &&
                    Math.abs(hero.getX() - obj.getX()) <= 1 &&
                    Math.abs(hero.getY() - obj.getY()) <= 1) {
                //begin combat sequence with this enemy
                if (fight(obj)) toRemove.add(obj);
            }
        }
        //remove objects
        for (GameObject obj : toRemove) {
            gameObjects.remove(obj);
        }
    }

    public boolean fight(GameObject obj){
        //for now, I will just make it random with only two outcomes
        int move = rand.nextInt(2);
        boolean enemyKilled = false;
        switch (move){
            case 0:
                System.out.println("An enemy attacks the hero! Watch out!");
                break;
            case 1:
                System.out.println("The hero gets the upper hand and slays the enemy");
                enemyKilled = true;
                break;
        }
        return enemyKilled;
    }

    private void eraseWorld(){
        System.out.print("\033[s"); //save the cursor position

        for (int i = 0; i < WIDTH+3; i++){
            System.out.print("\033[1A"); //move up 1 lines
            System.out.print("\033[2K"); //erase the entire line

        }
        //System.out.print("\033[u"); //return the cursor to the saved position


    }
}
>>>>>>> fab4c20efb4f1a66f4408c57d2a2009c05b0cc4c
