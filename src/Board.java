public class Board {

    public int[][] board;
    public int snakeDirection;
    public int snakeLength;
    public int snakeX;
    public int snakeY;
    public int appleX;
    public int appleY;
    public boolean apple;
    public int speed;

    public Board(int x, int y) {

        //Initialize the JFrame, and pray to god it works
        board = new int[y][x];
        StdDraw.setXscale(0, x);
        StdDraw.setYscale(0, y);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledRectangle(x / 2, y / 2, x / 2, y / 2);

        //Initialize the Snake
        snakeX = (int) x / 2;
        snakeY = (int) y / 2;
        snakeLength = 16;
        snakeDirection = 1;
        StdDraw.showFrame();
        speed = 200;
    }
    
    public void nextIteration() {
        //Create new areas for the snake
        switch (snakeDirection) {
            case 1:
                snakeY++;   //1 up
                if (snakeY >= board.length) {
                    snakeY = board.length - snakeY;
                }
                break;
            case 2:
                snakeX++;   //2 right
                if (snakeX >= board[0].length) {
                    snakeX = board.length - snakeX;
                }
                break;
            case 3:
                snakeY--;   //3 down
                if (snakeY <= 0) {
                    snakeY = board.length + snakeY;
                }
                break;
            case 4:
                snakeX--;   //4 left
                if (snakeX <= 0) {
                    snakeX = board[0].length + snakeX;
                }
                break;
        }
        if(board[snakeY][snakeX] > 0){
            endGame(false);
        }
        fillRectangle(snakeX, snakeY, true);
        board[snakeY][snakeX] = snakeLength;

        //Set nonsnake parts to be white:
        for (int counterX = 0; counterX < board[0].length; counterX++) {
            for (int counterY = 0; counterY < board.length; counterY++) {
                board[counterY][counterX]--;
                if (board[counterY][counterX] <= 0) {
                    board[counterY][counterX] = 0;
                    fillRectangle(counterX, counterY, false);
                }
            }
        }
        StdDraw.show(speed);
    }

    public void fillRectangle(int x, int y, boolean status) {
        if (status) {
            StdDraw.setPenColor(StdDraw.BLACK);
        } else {
            StdDraw.setPenColor(StdDraw.WHITE);
        }
        StdDraw.filledRectangle(x, y, .5, .5);
    }

    public void endGame(boolean status) {
        System.out.println("You got " + snakeLength + " points");
        if (status) {
            System.out.println("You win!");
        } else {
            System.out.println("You lose...");
        }
        System.exit(0);
    }

    public void generateApple() {
        appleX = (int) (Math.random() * board[0].length);
        appleY = (int) (Math.random() * board.length);
        while (board[appleY][appleX] > 0) {
            appleX = (int) (Math.random() * board[0].length);
            appleY = (int) (Math.random() * board.length);
        }
        apple = true;
    }
}
