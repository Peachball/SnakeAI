public class main {
    public static void main(String[] args){
        Board board = new Board(15,15);
        board.snakeDirection = 2;
        while(true){
            board.nextIteration();
        }
        //Write the AI here...
    }
}
