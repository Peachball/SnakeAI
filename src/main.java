import java.util.ArrayList;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        int decision;
        Board board = new Board(30, 30);
        board.appleGenerator = true;
        System.out.println("Choose your game mode:");
        System.out.println("1:Normal player mode");
        System.out.println("2:Simple AI mode");
        System.out.println("3: Super Simple Greedy AI mode (still in development)");
        System.out.println("4: Another AI that I plan to write in the future...");
        Scanner in = new Scanner(System.in);
        decision = Integer.parseInt(in.nextLine());
        switch (decision) {
            case 1:
                normalMode(board);
                break;
            case 2:
                easyAI(board);
                break;
            case 3:
                GreedySearch(board);
            case 4:
                AI3(board);
        }
    }

    static void AI3(Board board) {

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
        int distanceTraveled = 0;
        ArrayList<Integer> distanceFromStart = new ArrayList<Integer>();
        while (true) {
            Coord buffer;
            Coord snake;
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
                    case 'g':
                        board.speed = 150;
                }
            }
            distanceTraveled++;
            //Mark the death regions:
            for (int counterX = 0; counterX < board.board[0].length; counterX++) {
                for (int counterY = 0; counterY < board.board.length; counterY++) {
                    if (isSnake(board, counterX, counterY)) {
                        distances[counterY][counterX] = -1;
                        continue;
                    }
                }
            }

            //Mark each place on board with distance to apple
            snake = new Coord(board.snake1.snakeX, board.snake1.snakeY);
            for (int counterX = 0; counterX < board.board[0].length; counterX++) {

                for (int counterY = 0; counterY < board.board.length; counterY++) {

                    if (isSnake(board, counterX, counterY)) {
                        distances[counterY][counterX] = -board.board[counterY][counterX];
                    }
                    distances[counterY][counterX] += shortestPath(board, new Coord(counterX, counterY), snake);
                }
            }
            //Mark each point with closest distance to previous point (Dijkstras method first)
            
            //Add the point that is closest to the apple into the list:
            int counterX = -1;
            int counterY = -1;
            
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

    static Coord newCoord(Board board, int direction, Coord coord) {
        switch (direction) {
            case 1:
                coord.y++;   //1 up
                if (coord.y >= board.board.length) {
                    coord.y = board.board.length - coord.y;
                }
                break;
            case 2:
                coord.x++;   //2 right
                if (coord.x >= board.board[0].length) {
                    coord.x = board.board[0].length - coord.x;
                }
                break;
            case 3:
                coord.y--;   //3 down
                if (coord.y < 0) {
                    coord.y = board.board.length + coord.y;
                }
                break;
            case 4:
                coord.x--;   //4 left
                if (coord.x < 0) {
                    coord.x = board.board[0].length + coord.x;
                }
                break;
        }
        return coord;
    }

    static Coord newCoord(Board board, int direction, int x, int y) {
        switch (direction) {
            case 1:
                y++;   //1 up
                if (y >= board.board.length) {
                    y = board.board.length - y;
                }
                break;
            case 2:
                x++;   //2 right
                if (x >= board.board[0].length) {
                    x = board.board[0].length - x;
                }
                break;
            case 3:
                y--;   //3 down
                if (y < 0) {
                    y = board.board.length + y;
                }
                break;
            case 4:
                x--;   //4 left
                if (x < 0) {
                    x = board.board[0].length + x;
                }
                break;
        }
        return new Coord(x, y);
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
}
