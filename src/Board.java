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
    public boolean isSnake(int x, int y){
        if(board[y][x] >0){
            return true;
        }
        return false;
    }
    public boolean isSnake(Coord coord){
        if(board[coord.y][coord.x] >0){
            return true;
        }
        return false;
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
        if (board[snake1.snakeY][snake1.snakeX] == -1) {
            snake1.snakeLength++;
            apple = false;
        }
        if (snake1.snakeLength == board.length * board[0].length) {
            endGame(true);
        }
        //Set nonsnake parts to be white:
        for (int counterX = 0; counterX < board[0].length; counterX++) {
            for (int counterY = 0; counterY < board.length; counterY++) {
                board[counterY][counterX]--;
                if (board[counterY][counterX] == 0 || board[counterY][counterX] == -1) {
                    board[counterY][counterX] = 0;
                    fillRectangle(counterX, counterY, 2);
                    continue;
                }
                if (board[counterY][counterX] == -2) {
//                    fillRectangle(counterX, counterY, 3);
                    board[counterY][counterX]++;
                    continue;
                }
                if (board[counterY][counterX] > 0 && apple) {
//                    fillRectangle(counterX, counterY, 1);
                    continue;
                }
                if (board[counterY][counterX] > 0 && !apple) {
                    board[counterY][counterX]++;
//                    fillRectangle(counterX, counterY, 1);
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
            generateApple();
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
