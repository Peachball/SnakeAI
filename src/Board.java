import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Board {

    //Apple is represented on the board as -1
    //Snake is represented on the board as how many iterations it will stay on the board
    public int[][] board;
    public int appleX;
    public int appleY;
    public boolean apple;
    public int speed;
    public Snake snake1;
    public boolean appleGenerator;
    public boolean saves;
    public ArrayList<Person> people;
    public String player;
    public boolean troll;
    public LinkedList<Coord> snake;
    public Coord tail;

    public Board(int x, int y) {

        //Initialize the JFrame, and pray to god it works
        board = new int[y][x];
        StdDraw.setXscale(0, x);
        StdDraw.setYscale(0, y);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledRectangle(x / 2, y / 2, x / 2, y / 2);

        snake = new LinkedList<Coord>();

        //Initialize the Snake
        snake1 = new Snake();
        snake1.snakeX = (int) x / 2;
        snake1.snakeY = (int) y / 2;
        snake.add(new Coord(snake1.snakeX, snake1.snakeY));
        snake1.snakeLength = 1;
        snake1.snakeDirection = 1;
        StdDraw.showFrame();
        speed = 50;
        //appleGenerator = false;
        saves = false;
        people = new ArrayList<Person>();
        player = "AI";
        troll = false;
        tail = new Coord(snake1.snakeX,snake1.snakeY);
    }

    public void setSpeed(int i) {
        speed = i;
    }

    public void setSaveState(boolean status) {
        saves = status;
    }

    public void setCanvasSize(int x, int y) {
        StdDraw.setCanvasSize(x, y);
    }

    public void saveScore(String name, int score) {
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter("stats.txt", true));
            out.println(name + " " + score + " " + speed);
            out.close();
        } catch (IOException ex) {
            System.out.println("We screwed up somewhere while saving your stats. Sorry");
        }
    }

    public void setLength(int x) {
        snake1.snakeLength = x;
    }

    /**
     *
     * @param snake
     * @param direction
     */
    public void setDirection(Snake snake, int direction) {
        if (snake.snakeDirection % 2 == direction % 2) {
        } else {
            snake.snakeDirection = direction;
        }
    }

    public boolean isSnake(int x, int y) {
        return board[y][x] > 0;
    }
    public boolean isSnake(Coord coord) {
        return board[coord.y][coord.x] > 0;
    }

    public void nextIteration() {
        //Create new areas for the snake
        switch (snake1.snakeDirection) {
            case 0:
                break;
            case 1:
                snake1.snakeY++;   //1 is up
                if (snake1.snakeY >= board.length) {
                    snake1.snakeY = board.length - snake1.snakeY; //Loops around. 
                }
                break;
            case 2:
                snake1.snakeX++;   //2 right
                if (snake1.snakeX >= board[0].length) {
                    snake1.snakeX = board.length - snake1.snakeX;
                }
                break;
            case 3:
                snake1.snakeY--;   //3 down
                if (snake1.snakeY < 0) {
                    snake1.snakeY = board.length + snake1.snakeY;
                }
                break;
            case 4:
                snake1.snakeX--;   //4 left
                if (snake1.snakeX < 0) {
                    snake1.snakeX = board[0].length + snake1.snakeX;
                }
                break;
        }
        if (board[snake1.snakeY][snake1.snakeX] == -1) {//We got the apple
            snake1.snakeLength++;
            apple = false;
        }
        if (snake1.snakeLength == board.length * board[0].length){
            endGame(true);
            System.out.println("Wow. Just. Wow.\nAre you greedy searching?");
        }
        //Set nonsnake parts to be white:
        //We need to record change rather than just nuking everything.
        for (int counterX = 0; counterX < board[0].length; counterX++) {
            for (int counterY = 0; counterY < board.length; counterY++) {
                board[counterY][counterX]--;
                if(board[counterY][counterX] == 0){
                    tail = new Coord(counterX,counterY);
                }
                if (board[counterY][counterX] == -1||board[counterY][counterX] == 0) {
                    board[counterY][counterX] = 0;
                    fillRectangle(counterX, counterY, 2);
                    continue;
                }
                if (board[counterY][counterX] == -2) {
                    fillRectangle(counterX, counterY, 3);
                    board[counterY][counterX]++;
                    continue;
                }
                if (board[counterY][counterX] > 0 && apple) {
                    fillRectangle(counterX, counterY, 1);
                    continue;
                }
                if (board[counterY][counterX] > 0 && !apple) {
                    board[counterY][counterX]++;
                    fillRectangle(counterX, counterY, 1);
                }
                
            }
        }

        //Death Checker+Other misc checkers
        if (board[snake1.snakeY][snake1.snakeX] > 0) {
            endGame(false);
        }
        fillRectangle(snake1.snakeX, snake1.snakeY, 4);
        board[snake1.snakeY][snake1.snakeX] = snake1.snakeLength;
        if (appleGenerator && !apple) {
            if (troll) {
                trollgenerateApple();//Remember to remove this later.
            } else {
                generateApple();
            }
        }
        snake.add(new Coord(snake1.snakeX, snake1.snakeY));
        if (!(snake.size() < snake1.snakeLength)) {
            snake.remove(snake.size() - 1);
        }

        StdDraw.show(speed);
    }

    public void fillRectangle(int x, int y, int status) {
        switch (status) {
            case 1:
                StdDraw.setPenColor(StdDraw.BLACK); //Snake Body Color
                break;
            case 2:
                StdDraw.setPenColor(StdDraw.WHITE); //No snake color
                break;
            case 3:
                StdDraw.setPenColor(StdDraw.RED); //Apple Color
                break;
            case 4:
                StdDraw.setPenColor(StdDraw.PINK);// Snake Head color
                break;
        }
        StdDraw.filledRectangle(x, y, .45, .45);
    }

    public void endGame(boolean status) {
        System.out.println("You got " + snake1.snakeLength + " points");
        if (status) {
            System.out.println("You win!");
        } else {
            System.out.println("You lose...");
        }
        saveScore(player, snake1.snakeLength);
        System.exit(0);
    }

    public void generateApple() { //Remeber to add a function to place an apple that's not random
        int randomNum = (int) (Math.random() * (board[0].length * board.length - snake1.snakeLength));
        boolean status = false;
        for (int counterX = 0; counterX < board[0].length; counterX++) {
            for (int counterY = 0; counterY < board.length; counterY++) {
                if (board[counterY][counterX] > 0) {
                    continue;
                }
                randomNum--;
                if (randomNum <= 0 && !(board[counterY][counterX] > 0)) {
                    appleY = counterY;
                    appleX = counterX;
                    status = true;
                    break;
                }
            }
            if (status) {
                break;
            }
            if (counterX == board[0].length - 1) {
                counterX = 0;
            }
        }
        board[appleY][appleX] = -1;
        fillRectangle(appleX, appleY, 3);
        apple = true;
    }

    public void trollgenerateApple() {
        int newDirection = snake1.snakeDirection + 1;
        if (newDirection > 4) {
            newDirection = 1;
        }
        int counter = 0;
        Coord apple = main.newCoord(this, newDirection, new Coord(snake1.snakeX, snake1.snakeY));
        while (main.isSnake(this, apple) && counter < 10) {
            newDirection++;
            if (newDirection > 4) {
                newDirection = 1;
            }
            apple = main.newCoord(this, newDirection, new Coord(snake1.snakeX, snake1.snakeY));
            counter++;
        }
        if (counter == 10) {
            generateApple();
            return;
        } else {
            board[apple.y][apple.x] = -1;
            fillRectangle(appleX, appleY, 3);
            this.apple = true;
        }
    }
}

//This comment is made to obfuscate the code, for any readers
class Snake {

    public int snakeX;
    public int snakeY;
    public int snakeLength;
    public int snakeDirection;

    public Snake() {
        snakeX = 0;
        snakeY = 0;
        snakeLength = 1;
        snakeDirection = 1;
    }

    public Snake(int x, int y, int length) {
        snakeX = x;
        snakeY = y;
        snakeLength = length;
        snakeDirection = 1;
    }

    public Snake(int x, int y, int length, int direction) {
        snakeX = x;
        snakeY = y;
        snakeLength = length;
        snakeDirection = direction;
    }
}

class Person {

    public String name;
    public int score;

    public Person(String name, int score) {
        this.name = name;
        this.score = score;
    }
}

class NameComparator implements Comparator<Person> {

    @Override
    public int compare(Person t, Person t1) {
        return t.name.compareTo(t1.name);
    }

}
