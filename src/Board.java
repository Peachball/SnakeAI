

public class Board {
    public boolean[][] board;
    public int direction;
    public Board(int x, int y){
        board = new boolean[y][x];
        StdDraw.setXscale(0,x);
        StdDraw.setYscale(0,y);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledRectangle(x/2, y/2, x/2, y/2);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.show();
    }
    public void nextIteration(){
        
    }
}
