public class main {

    public static void main(String[] args) {
        Board board = new Board(15, 15);
        board.appleGenerator = true;
        while (true) {
            board.nextIteration();
            if (StdDraw.hasNextKeyTyped()) {
                board.setDirection(board.snake1, keyboardListener());
            }
        }
        //Write the AI here...
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
    static int AI(){
        return -1;
    }
}
