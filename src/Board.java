public class Board {

    public int[][] board;
    public int appleX;
    public int appleY;
    public boolean apple;
    public int speed;
    public Snake snake1;
    public boolean appleGenerator;

    public Board(int x, int y) {

        //Initialize the JFrame, and pray to god it works
        board = new int[y][x];
        StdDraw.setXscale(0, x);
        StdDraw.setYscale(0, y);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledRectangle(x / 2, y / 2, x / 2, y / 2);

        //Initialize the Snake
        snake1 = new Snake();
        snake1.snakeX = (int) x / 2;
        snake1.snakeY = (int) y / 2;
        snake1.snakeLength = 1;
        snake1.snakeDirection = 1;
        StdDraw.showFrame();
        speed = 100;
        appleGenerator = false;
    }

    public void setDirection(Snake snake, int direction) {
        if (snake.snakeDirection % 2 == direction % 2) {
            return;
        } else {
            snake.snakeDirection = direction;
        }
    }

    public void nextIteration() {
        //Create new areas for the snake
        switch (snake1.snakeDirection) {
            case 1:
                snake1.snakeY++;   //1 up
                if (snake1.snakeY >= board.length) {
                    snake1.snakeY = board.length - snake1.snakeY;
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
                    snake1.snakeY = board.length + snake1.snakeY - 1;
                }
                break;
            case 4:
                snake1.snakeX--;   //4 left
                if (snake1.snakeX < 0) {
                    snake1.snakeX = board[0].length + snake1.snakeX - 1;
                }
                break;
        }

        //Set nonsnake parts to be white:
        for (int counterX = 0; counterX < board[0].length; counterX++) {
            for (int counterY = 0; counterY < board.length; counterY++) {
                board[counterY][counterX]--;
                if (board[counterY][counterX] == 0 || board[counterY][counterX] == -1) {
                    board[counterY][counterX] = 0;
                    fillRectangle(counterX, counterY, 2);
                }
                if (board[counterY][counterX] == -2) {
                    board[counterY][counterX]++;
                }
                if (board[counterY][counterX] > 0) {
                    fillRectangle(counterX, counterY, 1);
                }
            }
        }

        //Death Checker+Other misc checkers
        if (board[snake1.snakeY][snake1.snakeX] > 0) {
            endGame(false);
        }
        if (board[snake1.snakeY][snake1.snakeX] == -1) {
            snake1.snakeLength++;
            apple = false;
        }
        if (appleGenerator && !apple) {
            generateApple();
        }
        fillRectangle(snake1.snakeX, snake1.snakeY, 4);
        board[snake1.snakeY][snake1.snakeX] = snake1.snakeLength;
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
                StdDraw.setPenColor(StdDraw.DARK_GRAY);// Snake Head color
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
        System.exit(0);
    }

    public void generateApple() {
        appleX = (int) (Math.random() * board[0].length);
        appleY = (int) (Math.random() * board.length);
        int counter = 0;
        while (board[appleY][appleX] > 0) {
            appleX = (int) (Math.random() * board[0].length);
            appleY = (int) (Math.random() * board.length);
        }
        board[appleY][appleX] = -1;
        fillRectangle(appleX, appleY, 3);
        apple = true;
    }
}

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
}
