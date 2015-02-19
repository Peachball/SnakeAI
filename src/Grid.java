import java.util.Random;
/**
 * The Grid class stores the game logic and the 
 * @author Benjamin
 */
public class Grid {

    public Tile[][] grid;
    private Random random;
    private Point apple;
    private Point SnakeStart;
    private Point SnakeEnd;
    private Direction currDirection;
    public Grid(int x, int y){
        grid = new Tile[x][y];
        random = new Random();
        startGame();
    }
    public Grid(int x, int y, long seed){
        grid = new Tile[x][y];
        random = new Random(seed);
        startGame();
    }
    private void genApple(){
        int x = random.nextInt(grid.length);
        int y = random.nextInt(grid[0].length);
        grid[x][y] = new Apple();
        apple = new Point(x,y);
    }
    private void startGame(){
        genApple();
        int x = random.nextInt(grid.length-2);
        int y = random.nextInt(grid[0].length-1);
        grid[x][y] = new SnakePart(null);
        grid[x + 1][y] = new SnakePart(new Point(x,y));
        SnakeStart = new Point(x,y);
        SnakeEnd = new Point(x+1, y);
    }
    public void move(Direction dir){
        if(!checkOpposite(dir, currDirection))
            return;
        Point newSnakeStart = SnakeStart.add(dir);
        if(get(newSnakeStart) instanceof SnakePart){
            die();
        }
        else{
            
            if(get(newSnakeStart) instanceof Apple){
                
            }else{
                Tile end = get(SnakeEnd);
                if(end instanceof SnakePart){ //Honestly, I'm not sure what to do if it isn't.
                    set(SnakeEnd, null);
                    SnakeEnd = ((SnakePart)end).getParent();
                }
            }
            set(newSnakeStart, new SnakePart(null));
            ((SnakePart)get(SnakeStart)).setParent(newSnakeStart);
        }
        currDirection = dir;
    }
    public void move(){
        move(currDirection);
    }
    public Tile get(Point p){
        return grid[p.getX()][p.getY()];
    }
    public void set(Point p, Tile t){
        grid[p.getX()][p.getY()] = t;
    }
    public static int modNumber(int a, int mod) {
        return (a + mod) % mod;
    }
    /**
     * Check if one direction is the opposite of another. Returns false if they are, true if they aren't.
     * Returns true if any are null.
     * @param dir Direction to be compared
     * @param otherDir Other direction to be compared
     * @return Whether the directions are not opposites of each other.
     */
    public static boolean checkOpposite(Direction dir, Direction otherDir){
        switch(dir){
            case UP:
                return otherDir != Direction.DOWN;
            case DOWN:
                return otherDir != Direction.UP;
            case RIGHT:
                return otherDir != Direction.LEFT;
            case LEFT:
                return otherDir != Direction.RIGHT;
            default:
                return true;
        }
    }
    public void die(){
        die(); //haha
    }
}