
import java.util.Random;

/**
 * The Grid class stores the game logic.
 *
 * @author Benjamin
 */
public class Grid {

    public Tile[][] grid;
    private Random random;
    public Point apple;
    public Point SnakeStart;
    /**
     * this is a helper for the graphics portion, so that the graphics engine knows which square to mark black
     */
    public Point SnakePrevStart;
    public Point SnakeEnd;
    private Direction currDirection;
    public boolean appled;

    public Grid(int x, int y) {
        grid = new Tile[x][y];
        random = new Random();
        startGame();
    }

    public Grid(int x, int y, long seed) {
        grid = new Tile[x][y];
        random = new Random(seed);
        startGame();
    }

    private void genApple() {
        int x = random.nextInt(grid.length);
        int y = random.nextInt(grid[0].length);
        if(grid[x][y] == null){
            grid[x][y] = new Apple();
            apple = new Point(x, y);
            appled = true;
        }else{
            genApple();
        }
    }

    private void startGame() {
        genApple();
        int x = random.nextInt(grid.length - 3);
        int y = random.nextInt(grid[0].length - 1);
        grid[x][y] = new SnakePart(null);
        SnakeStart = new Point(x, y);
        grid[x+1][y] = new SnakePart(SnakeStart);
        grid[x+2][y] = new SnakePart(new Point(x+1, y));
        SnakeEnd = new Point(x+2, y);
    }

    public void move(Direction dir) {
        if (!checkOpposite(dir, currDirection)) {
            move();
            return;
        }
        Point newSnakeStart = modPoint(SnakeStart.add(dir));
        if (get(newSnakeStart) instanceof SnakePart) {
            die();
        } else {

            if (get(newSnakeStart) instanceof Apple) {
                genApple();
            } else {
                if (!SnakeEnd.equals(SnakeStart)) {
                    Tile end = get(SnakeEnd);
                    if (end instanceof SnakePart) { //Honestly, I'm not sure what to do if it isn't.
                        set(SnakeEnd, null);
                        SnakeEnd = ((SnakePart) end).getParent();
                    }
                }
            }
            SnakePrevStart = SnakeStart;
            set(newSnakeStart, new SnakePart(null));
            ((SnakePart) get(SnakeStart)).setParent(newSnakeStart);
            SnakeStart = newSnakeStart;
        }
        currDirection = dir;
    }

    public void move() {
        if (currDirection != null) {
            move(currDirection);
        }
    }
    public Direction stall(){
        Direction checkDirection = currDirection;
        for(int i = 0; i < 4; i++){
            if(get(SnakeStart.add(checkDirection)) instanceof SnakePart){
                checkDirection = checkDirection.next();
            }else{
                return checkDirection;
            }
        }
        return Direction.UP;//It doesn't matter what we return here because we're completely surroudned.
    }
    public Tile get(Point p) {
        p = modPoint(p);
        return grid[p.getX()][p.getY()];
    }

    public void set(Point p, Tile t) {
        p = modPoint(p);
        grid[p.getX()][p.getY()] = t;
    }

    public static int modNumber(int a, int mod) {
        return (a + mod) % mod;
    }

    public int[][] createMap() {
        int[][] toSender = new int[grid.length][grid[0].length];
        Point target = SnakeEnd;
        int counter = 1;
        while (target != null) {
            toSender[target.getX()][target.getY()] = counter++;
            target = ((SnakePart) get(target)).getParent();
        }
        toSender[apple.getX()][apple.getY()] = -1;
        return toSender;
    }

    /**
     * Check if one direction is the opposite of another. Returns false if they
     * are, true if they aren't. Returns true if any are null.
     *
     * @param dir Direction to be compared
     * @param otherDir Other direction to be compared
     * @return Whether the directions are not opposites of each other.
     */
    public static boolean checkOpposite(Direction dir, Direction otherDir) {
        switch (dir) {
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

    public Point modPoint(Point p) {
        return new Point(modNumber(p.getX(), grid.length), modNumber(p.getY(), grid[0].length));
    }

    public static Point modPoint(Point p, int xLen, int yLen) {
        return new Point(modNumber(p.getX(), xLen), modNumber(p.getY(), yLen));
    }

    public void die() {
        //die(); //haha //why did I put this here.
        System.out.println("You died!");
        System.exit(0);
    }
}
