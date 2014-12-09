import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        int decision;
        Board board = new Board(16, 16);
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

    static double shortestPath(Board board, Coord location) {
        return Math.abs(board.snake1.snakeX - location.x) + Math.abs(board.snake1.snakeY - location.y);
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
        LinkedList<Coord> closedList = new LinkedList<Coord>();
        while (true) {

            int shortestDistance[] = new int[4];
            int shortestDirection = board.snake1.snakeDirection;
            boolean idleState = false;
            Coord buffer;

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

            //Check which direction has shortest route: (F = G + H, the G cost)
            for (int counter = 0; counter < 4; counter++) {

                if (!board.isSnake(newCoord(board, counter+1, board.snake1.snakeX, board.snake1.snakeY))) {
                    shortestDistance[counter] = (int) shortestPath(board, newCoord(board,
                            counter+1, board.appleX, board.appleY));
                }
                if (board.isSnake(newCoord(board, counter+1, board.snake1.snakeX, board.snake1.snakeY))) {
                    shortestDistance[counter] = 1000000;
                }
            }
            int minDistance = shortestDistance[0];
            int minCounter = 0;
            for (int counter = 0; counter < 4; counter++) {
                if (minDistance > shortestDistance[counter]) {
                    minDistance = shortestDistance[counter];
                    minCounter = counter;
                }
            }
            board.setDirection(board.snake1, minCounter+1);
            board.nextIteration();
            
        }

    }

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
