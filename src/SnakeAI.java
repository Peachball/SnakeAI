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
    private boolean left = true;
    public static void main(String[] args) {
        //Testing Code
        int[][] grid = 
        {{  10,     11,    12,      13,     0},
        {   9,      0,      2,      1,      0},
        {   8,      -1,     3,      0,      0},
        {   7,      0,      4,      5,      6},
        {   0,      0,      0,      0,      0}
        };
        int[][] out = {{0, 0, 0, 0, 0},
        {0, 0, 0, 1, 0},
        {0, -1, 0, 0, 0},
        {0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0}
        };
        CurrentAI = new SnakeAI(grid);
        try{
        CurrentAI.pathFind();
        List<Node> thing = CurrentAI.reconstruct();
        for(Node n : thing){
            out[n.self.x][n.self.y] = 3;
            System.out.println(n.self);
        }
        for(int[] n : out){
            for(int m : n)
                System.out.printf("%3d", m);
            System.out.println();
        }
        List<Direction> list = CurrentAI.findPath(thing);
        for(Direction d : list){
            System.out.println(d.name());
        }
        }catch(NoPathException e){
            System.out.println("No Path!");
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
    private Point getTarget() {
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
    private void addSurround(Node parent) {
        Point p = parent.getPoint();
        if(grid[Grid.modNumber(p.getX() + 1,grid.length)][p.getY()] <= counter){
            makeNode(new Point(Grid.modNumber(p.getX() + 1,grid.length), p.getY()), parent);
        }
        if(grid[Grid.modNumber(p.getX() - 1,grid.length)][p.getY()] <= counter){
            makeNode(new Point(Grid.modNumber(p.getX() - 1,grid.length), p.getY()), parent);
        }
        if(grid[p.getX()][Grid.modNumber(p.getY() + 1,grid[0].length)] <= counter){
            makeNode(new Point(p.getX(), Grid.modNumber(p.getY() + 1,grid[0].length)), parent);
        }
        if(grid[p.getX()][Grid.modNumber(p.getY() - 1,grid[0].length)] <= counter){
            makeNode(new Point(p.getX(), Grid.modNumber(p.getY() - 1,grid[0].length)), parent);
        }
    }
    public void pathFind() throws NoPathException {
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
        while (true) {
            Node thing = openSet.get(0);
            addSurround(thing);
            openSet.remove(thing);
            closedSet.put(thing.self, thing);
            if(checkDone())
                break;
            if(openSet.isEmpty())
                throw new NoPathException();
        }
    }
    public boolean checkDone() {
        return closedSet.containsKey(target);
    }
    public LinkedList<Direction> AStar() throws NoPathException{
            pathFind();
            List<Node> nodes = reconstruct();
            LinkedList<Direction> directions = new LinkedList<Direction>();
            Node n = nodes.remove(0);
            Node m;
            while(!nodes.isEmpty()){
                m = nodes.remove(0);
                directions.add(n.self.subtract(m.self));
                n = m;
            }
            return directions;
    }
     private LinkedList<Node> reconstruct() {
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
     private LinkedList<Node> reconstruct(Node end) {
        LinkedList<Node> toSender = new LinkedList<>();
        toSender.add(end);
        while (true) {
            Node process = toSender.get(0);
            if (process.parent.equals(start)) {
                break;
            }
            toSender.add(0, closedSet.get(process.parent));
        }
        return toSender;
    }
     public List<Direction> findPath(List<Node> path){
         Node prev = path.remove(0);
         List<Direction> total = new LinkedList<Direction>();
         while(!path.isEmpty()){
             Node curr = path.remove(0);
             total.add(curr.self.subtract(prev.self));
         }
         return total;
     }
     public Direction stall(){
        int right = left ? -1 : 1;
        if(grid[Grid.modNumber(target.getX() + 1,grid.length)][target.getY()] == 2){
            if(grid[target.getX()][Grid.modNumber(target.getY() + right,grid[0].length)] == 0){
                return Direction.UP;
            }
            if(grid[target.getX()][Grid.modNumber(target.getY() - right,grid[0].length)] == 0){
                return Direction.DOWN;
            }
        }
        else if(grid[Grid.modNumber(target.getX() - 1,grid.length)][target.getY()] == 2){
            if(grid[target.getX()][Grid.modNumber(target.getY() - right,grid[0].length)] == 0){
                return Direction.DOWN;
            }
            if(grid[target.getX()][Grid.modNumber(target.getY() + right,grid[0].length)] == 0){
                return Direction.UP;
            }
        }
        else if(grid[target.getX()][Grid.modNumber(target.getY() + 1,grid[0].length)] == 2){
            if(grid[Grid.modNumber(target.getX() + right,grid.length)][target.getY()] == 0){
                return Direction.RIGHT;
            }
            else if(grid[Grid.modNumber(target.getX() - right,grid.length)][target.getY()] == 0){
                return Direction.LEFT;
            }
        }
        else if(grid[target.getX()][Grid.modNumber(target.getY() - 1,grid[0].length)] == 2){
            if(grid[Grid.modNumber(target.getX() - right,grid.length)][target.getY()] == 0){
                return Direction.LEFT;
            }
            else if(grid[Grid.modNumber(target.getX() + right,grid.length)][target.getY()] == 0){
                return Direction.RIGHT;
            }
        }
        return null; //If you got here, something's wrong.
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
        public Direction subtract(Point p){
            if(p.getX() - this.getX() == 1)
                return Direction.RIGHT;
            if(p.getX() - this.getX() == -1)
                return Direction.LEFT;
            if(p.getY() - this.getY() == 1)
                return Direction.UP;
            if(p.getY() - this.getY() == -1)
                return Direction.DOWN;
            return null;//Not Supposed to get here.
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
