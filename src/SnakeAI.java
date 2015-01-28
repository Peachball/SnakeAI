
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
    public static void main(String[] args){
        int[][] grid = {{0,-1,0,0,0},
                        {1,1,1,1,0},
                        {0,0,0,0,0},
                        {0,-2,0,0,0}  
        };
        CurrentAI = new SnakeAI(grid);
        CurrentAI.pathFind();
    }
    public SnakeAI(int[][] grid/*, int snakeLength*/) {
        openSet = new LinkedList<Node>();
        closedSet = new HashMap<Point, Node>();
        this.grid = grid;
    }

    private static void insert(List<Node> a, Node thing) throws AlreadyInListException {
        ListIterator itr = a.listIterator();
        while(itr.hasNext()){
            Node n = (Node)itr.next();
            if(n.fScore() > thing.fScore()){
                itr.previous();
                itr.add(thing);
                break;
            }else if(n.equals(thing))
                throw new AlreadyInListException(n);
        }
        itr.add(thing);
    }

    public Point getTarget() {
        return target;
    }

    private void makeNode(Point p, Node parent) {
        if (!closedSet.containsKey(p)) {
            Node process = new Node(p, parent);
            try{
            insert(openSet, process);
            }catch(AlreadyInListException e){
                e.getNode().checkBetter(parent);
            }
        }
    }

    public void addSurround(Node parent) {
        Point p = parent.getPoint();
        try {
            if (grid[p.getX() + 1][p.getY()] <= 0) {
                makeNode(new Point(p.getX() + 1, p.getY()), parent);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            if (grid[p.getX() - grid.length + 1][p.getY()] <= 0) {
                makeNode(new Point(p.getX() + 1, p.getY()), parent);
            }
        }
        try {
            if (grid[p.getX() - 1][p.getY()] <= 0) {
                makeNode(new Point(p.getX() - 1, p.getY()), parent);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            if (grid[p.getX() + grid.length - 1][p.getY()] <= 0) {
                makeNode(new Point(p.getX() + 1, p.getY()), parent);
            }
        }
        try {
            if (grid[p.getX()][p.getY() + 1] <= 0) {
                makeNode(new Point(p.getX(), p.getY() + 1), parent);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            if (grid[p.getX()][p.getY() - grid[0].length + 1] <= 0) {
                makeNode(new Point(p.getX() + 1, p.getY()), parent);
            }
        }
        try {
            if (grid[p.getX()][p.getY() - 1] <= 0) {
                makeNode(new Point(p.getX(), p.getY() - 1), parent);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            if (grid[p.getX()][p.getY() + grid[0].length - 1] <= 0) {
                makeNode(new Point(p.getX() + 1, p.getY()), parent);
            }
        }
    }

    public void pathFind() {

        int counter = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] == -2) {
                    Node n = new Node(i, j, null);
                    System.out.println("Started with " + n);
                    openSet.add(n);
                    start = new Point(i, j);
                } else if (grid[i][j] == -1) {
                    target = new Point(i, j);
                }
            }
        }
        while (!openSet.isEmpty() && !checkDone()) {
            Node thing = openSet.get(openSet.size() - 1);
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
        while (true) {
            Node process = toSender.get(0);
            toSender.add(0, process);
            if (process.self.equals(start)) {
                break;
            }
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
            this.parent = parent.getPoint();
            this.gScore = parent.fScore() + 1;
            this.hScore = Math.abs(SnakeAI.CurrentAI.getTarget().getX() - loc.getX() + SnakeAI.CurrentAI.getTarget().getX() - loc.getY());
        }

        public Node(int x, int y, Node parent) {
            this.self = new Point(x, y);
            if(parent != null){
            this.parent = parent.getPoint();
            this.gScore = parent.fScore() + 1;
            }else{
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
        public String toString(){
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
        public boolean equals(Object other){
            if(other instanceof Node && other != null){
                return ((Node) other).self.equals(this.self);
            }else{
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
        public int hashCode(){
            return (x*y + y + x)%Integer.MAX_VALUE;
        }
        @Override
        public String toString(){
            return "(" + x + "," + y + ")";
        }
    }
    static class AlreadyInListException extends Exception{
        Node already;
        public AlreadyInListException(Node n){
            this.already = n;
        }
        public Node getNode(){
            return already;
        }
    }
}
