
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Benjamin
 */
public class SnakeGame {

    private static Grid game;
    private static Point oldHead;
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        int x, y;
        long seed;

        System.out.println("Size of grid x?");
        while (true) {
            try {
                x = Integer.parseInt(scn.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Not Valid!");
            }
        }
        System.out.println("Size of grid y?");
        while (true) {
            try {
                y = Integer.parseInt(scn.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Not Valid!");
            }
        }
        System.out.println("custom seed? (Type none for none)");
        while (true) {
            try {
                String s = scn.nextLine();
                if (s.equalsIgnoreCase("none")) {
                    seed = new Random().nextLong();
                } else {
                    seed = Integer.parseInt(scn.nextLine());
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Not Valid!");
            }
        }
        init(x,y);
        /*while(true){
            if(false)
                break;
            playGame(100, Direction.valueOf(scn.nextLine()));
        }*/
        /*test();
        playGame(100, Direction.UP);*/
        while (true) {
            SnakeAI ai = new SnakeAI(game.createMap(), game.SnakeStart, game.apple);
            try {
                List<Direction> thing = ai.AStar();
                //System.out.println(thing);
                playGame(0, thing);
            } catch (NoPathException ex) {
                System.out.println("Derp");
                playGame(1000, Direction.UP);
            }
        }
    }

    public static void test() {
            playGame(100, Direction.UP);
            playGame(100, Direction.RIGHT);
    }
    public static void init(int x, int y){
        game = new Grid(x, y, new Random().nextLong()); //Add randome seed later.
        StdDraw.setXscale(0, x);
        StdDraw.setYscale(0, y);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledRectangle(x / 2, y / 2, x / 2, y / 2);
        oldHead = game.SnakeStart;
    }

    public static void playGame(long waitTime, Direction dir) {
        game.move(dir);
        if (game.appled) {
            game.appled = false;
            fillRectangle(game.apple, -1);
        }else{
            fillRectangle(game.SnakeEnd, 0);
        }
        fillRectangle(game.SnakeStart, 2);
        fillRectangle(game.SnakePrevStart,1);
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException ex) {
            Logger.getLogger(SnakeGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        StdDraw.showFrame();
    }

    public static void playGame(long waitTime, List<Direction> dir) {
        for (Direction d : dir) {
            playGame(waitTime, d);
        }
    }

    public static void fillRectangle(int x, int y, int status) {
        switch (status) {
            case 1:
                StdDraw.setPenColor(StdDraw.BLACK); //Snake Body Color
                break;
            case 0:
                StdDraw.setPenColor(StdDraw.WHITE); //No snake color
                break;
            case -1:
                StdDraw.setPenColor(StdDraw.RED); //Apple Color
                break;
            case 2:
                StdDraw.setPenColor(StdDraw.PINK);// Snake Head color
                break;
        }
        StdDraw.filledRectangle(x, y, .45, .45);
    }

    public static void fillRectangle(Point p, int status) {
        fillRectangle(p.getX(), p.getY(), status);
    }

}
