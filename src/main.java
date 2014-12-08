
import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        int decision;
        Board board = new Board(15, 15);
        board.appleGenerator = true;
        System.out.println("Choose your game mode:");
        System.out.println("1:Normal player mode");
        System.out.println("2:Simple AI mode");
        System.out.println("3: Perfect super fast AI mode (still in development)");
        Scanner in = new Scanner(System.in);
        decision = Integer.parseInt(in.nextLine());
        switch(decision){
            case 1:
                normalMode(board);
                break;
            case 2:
                easyAI(board);
                break;
            case 3:
                aStarAI(board);
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
            if(board.snake1.snakeX == 0){
                board.setDirection(board.snake1,3);
                board.nextIteration();
                board.setDirection(board.snake1,2);
            }
            if(StdDraw.hasNextKeyTyped()){
                switch(StdDraw.nextKeyTyped()){
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
        return Math.sqrt(Math.pow(x - board.snake1.snakeX, 2) + Math.pow(y - board.snake1.snakeY, 2));
    }

    static void normalMode(Board board) {
        while (true) {
            board.nextIteration();
            if (StdDraw.hasNextKeyTyped()) {
                board.setDirection(board.snake1, keyboardListener());
            }
        }
    }
    static void aStarAI(Board board){
        
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
