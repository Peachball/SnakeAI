import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        int decision;
        Scanner in = new Scanner(System.in);
        Board board = new Board(100, 100);
        board.appleGenerator = true;
        System.out.println("Choose your game mode:");
        System.out.println("1:Normal player mode");
        System.out.println("2:Simple AI mode");
        System.out.println("3: Super Simple Greedy AI mode (OMFG IT WORKED)"); //These two are both attempts to
        System.out.println("4: Super Simple NOT GONNA DIE AI mode (Close enough)");// avoid A*...
        System.out.println("5: Attempt at A*...");

        decision = Integer.parseInt(in.nextLine());
        switch (decision) {
            case 1:
                System.out.println("Enter your name:");
                String buffer = in.nextLine();
                board.player = buffer;
                normalMode(board);
                break;
            case 2:
                easyAI(board);
                break;
            case 3:
                GreedySearch(board);
            case 4:
                AI3(board);
            case 5:
                AI4(board);
        }
    }

    static void AI4(Board board) {
        while (true) {
            Coord snake = new Coord(board.snake1.snakeX,board.snake1.snakeY);
            CoordNode parent = new CoordNode(snake);
            CoordNode currentNode = parent;
            //Speed Settings
            if (StdDraw.hasNextKeyTyped()) {
                switch (StdDraw.nextKeyTyped()) {
                    case 'q':
                        board.speed = 0;
                        break;
                    case 'a':
                        board.speed = 10;
                        break;
                    case 's':
                        board.speed = 25;
                        break;
                    case 'd':
                        board.speed = 50;
                        break;
                    case 'f':
                        board.speed = 100;
                        break;
                    case 'g':
                        board.speed = 150;
                }
            }
            
            //Attempt to go to end...
            
        }
    }

    static void AI3(Board board) {
        while (true) {
            //Some helpful things to have...
            boolean[][] deathRegions = new boolean[board.board.length][board.board[0].length];
            Coord snake = new Coord(board.snake1.snakeX, board.snake1.snakeY);
            Coord buffer;
            Coord apple = new Coord(board.appleX, board.appleY);

            //Speed Settings
            if (StdDraw.hasNextKeyTyped()) {
                switch (StdDraw.nextKeyTyped()) {
                    case 'q':
                        board.speed = 0;
                        break;
                    case 'a':
                        board.speed = 10;
                        break;
                    case 's':
                        board.speed = 25;
                        break;
                    case 'd':
                        board.speed = 50;
                        break;
                    case 'f':
                        board.speed = 100;
                        break;
                    case 'g':
                        board.speed = 150;
                }
            }

            //Mark the death regions
            boolean done = false;
            while (!done) {
                done = true;
                for (int counterX = 0; counterX < board.board[0].length; counterX++) {
                    for (int counterY = 0; counterY < board.board.length; counterY++) {
                        if (deathRegions[counterY][counterX]) {
                            continue;
                        }
                        if (isSnake(board, new Coord(counterX, counterY))) {
                            deathRegions[counterY][counterX] = true;
                            done = false;
                            continue;
                        }
                        int sum = 0;
                        for (int counter = 1; counter < 5; counter++) {
                            buffer = newCoord(board, counter, new Coord(counterX, counterY));
                            if (isSnake(board, buffer) || deathRegions[buffer.y][buffer.x]) {
                                sum++;
                            }
                        }
                        if (sum >= 3) {
                            deathRegions[counterY][counterX] = true;
                            done = false;
                        }
                    }
                }
            }

            //Super Greedy Search
            int shortestDistance = 10000;
            int shortestDirection = 1;
            for (int counter = 1; counter < 5; counter++) {

                buffer = newCoord(board, counter, snake);
                if (deathRegions[buffer.y][buffer.x]) {
                    continue;
                }
                if (shortestDistance > shortestPath(board, apple, buffer)) {
                    shortestDirection = counter;
                    shortestDistance = (int) shortestPath(board, apple, buffer);
                }
            }
            board.setDirection(board.snake1, shortestDirection);
            board.nextIteration();
        }
    }

    static void easyAI(Board board) {
        board.speed = 25;
        board.setDirection(board.snake1, 2);
        while (true) {
            board.nextIteration();
            if (board.snake1.snakeX == board.board[0].length - 1) {
                board.setDirection(board.snake1, 3);
                board.nextIteration();
                board.setDirection(board.snake1, 4);
            }
            if (board.snake1.snakeX == 0) {
                board.setDirection(board.snake1, 3);
                board.nextIteration();
                board.setDirection(board.snake1, 2);
            }
            if (StdDraw.hasNextKeyTyped()) {
                switch (StdDraw.nextKeyTyped()) {
                    case 'q':
                        board.speed = 0;
                        break;
                    case 'a':
                        board.speed = 10;
                        break;
                    case 's':
                        board.speed = 25;
                        break;
                    case 'd':
                        board.speed = 50;
                        break;
                    case 'f':
                        board.speed = 100;
                    case 'g':
                        board.speed = 150;
                }
            }
        }
    }

    static double shortestPath(Board board, int x, int y) {
        return Math.abs(board.snake1.snakeX - x) + Math.abs(board.snake1.snakeY - y);
    }

    static double shortestPath(Board board, Coord location1, Coord location2) {
        return Math.abs(location2.x - location1.x) + Math.abs(location2.y - location1.y);
    }

    static void normalMode(Board board) {
        while (true) {
            board.nextIteration();
            if (StdDraw.hasNextKeyTyped()) {
                board.setDirection(board.snake1, keyboardListener());
            }
        }
    }

    static void GreedySearch(Board board) {
        int[][] distances = new int[board.board.length][board.board[0].length];
        ArrayList<Coord> checked = new ArrayList<Coord>();
        ArrayList<Coord> nextChecked = new ArrayList<Coord>();
        board.nextIteration();
        while (true) {
            Coord buffer;
            Coord snake;
            Coord apple;
            apple = new Coord(board.appleX, board.appleY);
            snake = new Coord(board.snake1.snakeX, board.snake1.snakeY);
            //Speed Settings
            if (StdDraw.hasNextKeyTyped()) {
                switch (StdDraw.nextKeyTyped()) {
                    case 'q':
                        board.speed = 0;
                        break;
                    case 'a':
                        board.speed = 10;
                        break;
                    case 's':
                        board.speed = 25;
                        break;
                    case 'd':
                        board.speed = 50;
                        break;
                    case 'f':
                        board.speed = 100;
                        break;
                    case 'g':
                        board.speed = 150;
                }
            }
            //Simplest solution(hopefully)
            int shortestDistance = 10000;
            int shortestDirection = 1;
            for (int counter = 1; counter < 5; counter++) {

                buffer = newCoord(board, counter, snake);
                if (isSnake(board, buffer)) {
                    continue;
                }
                if (shortestDistance > shortestPath(board, apple, buffer)) {
                    shortestDirection = counter;
                    shortestDistance = (int) shortestPath(board, apple, buffer);
                }
            }
            board.setDirection(board.snake1, shortestDirection);
            board.nextIteration();

        }

    }

    static boolean isSnake(Board board, int x, int y) {
        return board.board[y][x] > 0;
    }

    static boolean isSnake(Board board, Coord coord) {
        return board.board[coord.y][coord.x] > 0;
    }
    /*
     * Directionally based, not for the distance
     */

    //NOTE TO SELF: YOU CAN CHANGE THE GIVEN VALUES...
    //E.G. YOU CAN CHANGE THE INITIAL VALUE OF SAY THE DIRECTION...
    //FML...
    static Coord newCoord(Board board, int direction, Coord coord) {
        Coord buffer = new Coord(0, 0);
        buffer.x = coord.x;
        buffer.y = coord.y;
        switch (direction) {
            case 1:
                buffer.y++;   //1 up
                if (buffer.y >= board.board.length) {
                    buffer.y = board.board.length - buffer.y;
                }
                break;
            case 2:
                buffer.x++;   //2 right
                if (buffer.x >= board.board[0].length) {
                    buffer.x = board.board[0].length - buffer.x;
                }
                break;
            case 3:
                buffer.y--;   //3 down
                if (buffer.y < 0) {
                    buffer.y = board.board.length + buffer.y;
                }
                break;
            case 4:
                buffer.x--;   //4 left
                if (buffer.x < 0) {
                    buffer.x = board.board[0].length + buffer.x;
                }
                break;
        }
        return buffer;
    }

    static Coord newCoord(Board board, int direction, int x, int y) {
        int bufferx = x;
        int buffery = y;
        switch (direction) {
            case 1:
                buffery++;   //1 up
                if (buffery >= board.board.length) {
                    buffery = board.board.length - buffery;
                }
                break;
            case 2:
                bufferx++;   //2 right
                if (bufferx >= board.board[0].length) {
                    bufferx = board.board[0].length - bufferx;
                }
                break;
            case 3:
                buffery--;   //3 down
                if (y < 0) {
                    buffery = board.board.length + buffery;
                }
                break;
            case 4:
                bufferx--;   //4 left
                if (x < 0) {
                    bufferx = board.board[0].length + bufferx;
                }
                break;
        }
        return new Coord(bufferx, buffery);
    }

    static int keyboardListener() {
        int direction = 1;
        switch (StdDraw.nextKeyTyped()) {
            case 'a':
                direction = 4;
                break;
            case 's':
                direction = 3;
                break;
            case 'd':
                direction = 2;
                break;
            case 'w':
                direction = 1;
                break;
            case 'q':
                direction = 0;
                break;
            case 'p':
                direction = 5;
                break;
        }
        return direction;
    }
}

class Coord {

    public int x;
    public int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}

class CoordComparator implements Comparator<Coord>{

    @Override
    public int compare(Coord t, Coord t1) {
        if(t.x!=t1.x){
            return t.x-t1.x;
        }
        else{
            return t.y-t1.y;
        }
    }
    
}

class CoordNode{
    public CoordNode d1;
    public CoordNode d2;
    public CoordNode d3;
    public CoordNode d4;
    public Coord position;
    public CoordNode parent;
    public CoordNode(Coord position){
        this.position = position;
    }
}
