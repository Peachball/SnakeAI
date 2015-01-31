import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class SnakeAI {

    private Point target;
    private Point start;
    private List<Node> openSet;
    private Map<Point, Node> closedSet;
    public static final int apple = -1;
    public static final int snakeHead = 1;
    public static SnakeAI CurrentAI;
    private int[][] grid;
    private int counter = 1;
    public static void main(String[] args) {
        //Testing Code
        int[][] grid = 
        {{10, 11, 12, 13, 0},
        {9, 0, 2, 1, 0},
        {8, -1, 3, 0, 0},
        {7, 0, 4, 5, 6},
        {0, 0, 0, 0, 0}
        };
        int[][] out = {{0, 0, 0, 0, 0},
        {0, 0, 0, 1, 0},
        {0, -1, 0, 0, 0},
        {0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0}
        };;
        CurrentAI = new SnakeAI(grid);
        CurrentAI.pathFind();
        List<Node> thing = CurrentAI.reconstruct();
        for(Node n : thing){
            out[n.self.x][n.self.y] = 3;
            System.out.println(n.self);
        }
        for(int[] n : out){
            for(int m : n)
                System.out.print(m + " ");
            System.out.println();
        }
    }

    public SnakeAI(int[][] grid/*, int snakeLength*/) {
        openSet = new LinkedList<Node>();
        closedSet = new HashMap<Point, Node>();
        this.grid = grid;
    }

    private static void insert(List<Node> a, Node thing) throws AlreadyInListException {
        ListIterator itr = a.listIterator();
        while (itr.hasNext()) {
            Node n = (Node) itr.next();
            if (n.fScore() > thing.fScore()) {
                itr.previous();
                itr.add(thing);
                break;
            } else if (n.equals(thing)) {
                throw new AlreadyInListException(n);
            }
        }
        itr.add(thing);
    }
    public Point getTarget() {
        return target;
    }
    private void makeNode(Point p, Node parent) {
        if (!closedSet.containsKey(p)) {
            Node process = new Node(p, parent);
            try {
                insert(openSet, process);
            } catch (AlreadyInListException e) {
                e.getNode().checkBetter(parent);
            }
        }
    }
    public void addSurround(Node parent) {
        Point p = parent.getPoint();
        if(grid[modNumber(p.getX() + 1,grid.length)][p.getY()] <= counter){
            makeNode(new Point(modNumber(p.getX() + 1,grid.length), p.getY()), parent);
        }
        if(grid[modNumber(p.getX() - 1,grid.length)][p.getY()] <= counter){
            makeNode(new Point(modNumber(p.getX() - 1,grid.length), p.getY()), parent);
        }
        if(grid[p.getX()][modNumber(p.getY() + 1,grid[0].length)] <= counter){
            makeNode(new Point(p.getX(), modNumber(p.getY() + 1,grid[0].length)), parent);
        }
        if(grid[p.getX()][modNumber(p.getY() - 1,grid[0].length)] <= counter){
            makeNode(new Point(p.getX(), modNumber(p.getY() - 1,grid[0].length)), parent);
        }
    }
    public int modNumber(int a, int mod){
        return (a+mod) % mod;
    }
    public void pathFind() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] == 1) {
                    start = new Point(i,j);
                } else if (grid[i][j] == -1) {
                    target = new Point(i,j);
                }
            }
        }
        Node n = new Node(start, null);
        System.out.println("Started with " + n);
        openSet.add(n);
        while (!openSet.isEmpty() && !checkDone()) {
            Node thing = openSet.get(0);
            addSurround(thing);
            openSet.remove(thing);
            closedSet.put(thing.self, thing);
            System.out.println("Added " + thing + " to the closed set.");
        }
        

    }

    public boolean checkDone() {
        return closedSet.containsKey(target);
    }

    public LinkedList<Node> reconstruct() {
        LinkedList<Node> toSender = new LinkedList<>();
        toSender.add(closedSet.get(target));
        while (true) {
            Node process = toSender.get(0);
            if (process.parent.equals(start)) {
                break;
            }
            toSender.add(0, closedSet.get(process.parent));
        }
        return toSender;
    }

    class Node {

        private Point parent;
        private final Point self;
        private int gScore;
        private final int hScore;

        public Node(Point loc, Node parent) {
            this.self = loc;
            if (parent != null) {
                this.parent = parent.getPoint();
                this.gScore = parent.fScore() + 1;
            } else {
                this.parent = null;
                this.gScore = 1;
            }
            this.hScore = Math.abs(SnakeAI.CurrentAI.getTarget().getX() - loc.getX() + SnakeAI.CurrentAI.getTarget().getX() - loc.getY());
        }

        public Node(int x, int y, Node parent) {
            this.self = new Point(x, y);
            if (parent != null) {
                this.parent = parent.getPoint();
                this.gScore = parent.fScore() + 1;
            } else {
                this.parent = null;
                this.gScore = 1;
            }
            this.hScore = Math.abs(SnakeAI.CurrentAI.getTarget().getX() - x + SnakeAI.CurrentAI.getTarget().getX() - y);
        }

        public int fScore() {
            return gScore + hScore;
        }

        public int gScore() {
            return gScore;
        }

        public int[] getCoord() {
            return self.getCoord();
        }

        public Point getPoint() {
            return self;
        }

        @Override
        public String toString() {
            return self + " has parent of " + parent + " with gScore " + gScore + " with hScore " + hScore;
        }

        public boolean checkBetter(Node n) {
            if (n.gScore + 1 < gScore()) {
                gScore = n.fScore() + 1;
                parent = n.getPoint();
                System.out.println("Changing " + self + "'s parent to " + n.getPoint());
                return true;
            }
            return false;
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof Node && other != null) {
                return ((Node) other).self.equals(this.self);
            } else {
                return false;
            }
        }
    }

    class Point {

        private int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int[] getCoord() {
            return new int[]{x, y};
        }

        @Override
        public boolean equals(Object otherPoint) {
            if (otherPoint instanceof Point) {
                int[] targ = ((Point) otherPoint).getCoord();
                return x == targ[0] && y == targ[1];
            }
            return false;
        }

        @Override
        public int hashCode() {
            return (x * y + y + x) % Integer.MAX_VALUE;
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }

    static class AlreadyInListException extends Exception {

        Node already;

        public AlreadyInListException(Node n) {
            this.already = n;
        }

        public Node getNode() {
            return already;
        }
    }
}
