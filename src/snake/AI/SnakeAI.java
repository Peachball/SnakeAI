package snake.AI;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import snake.Common.Direction;
import snake.Common.Point;
import snake.Game.Grid;
/**
 * This class implements A* for snake.
 * @author Benjamin
 */
public class SnakeAI {

    private Point target;
    private Point start;
    private LinkedList<Node> openSet;
    private Map<Point, Node> closedSet;
    public static final int apple = -1;
    public static final int snakeHead = 1;
    public static SnakeAI CurrentAI;
    private int[][] grid;
    private int counter = 0;
    private boolean left = true;

    public SnakeAI(int[][] grid, Point snakeStart, Point apple) {
        openSet = new LinkedList<Node>();
        closedSet = new HashMap<Point, Node>();
        this.grid = grid;
        start = snakeStart;
        target = apple;
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
            Node process = new Node(p, parent, target);
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
        if(grid[p.getX()][Grid.modNumber(p.getY() + 1,grid.length)] <= counter){
            makeNode(new Point(p.getX(), Grid.modNumber(p.getY() + 1,grid[0].length)), parent);
        }
        if(grid[p.getX()][Grid.modNumber(p.getY() - 1,grid[0].length)] <= counter){
            makeNode(new Point(p.getX(), Grid.modNumber(p.getY() - 1,grid[0].length)), parent);
        }
    }
    public void pathFind() throws NoPathException {
        Node n = new Node(start, null, target);
        openSet.add(n);
        while (true) {//Replace with do/while loop
            Node thing = openSet.removeFirst();
            addSurround(thing);
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
            LinkedList<Node> nodes = reconstruct();
            LinkedList<Direction> directions = new LinkedList<Direction>();
            Node n;
            while(!nodes.isEmpty()){
                n = nodes.removeFirst();
                directions.add(n.parent.subtract(n.self, grid.length, grid[0].length));
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
     public LinkedList<Direction> findPath(LinkedList<Node> path){
         Node prev = path.removeFirst();
         LinkedList<Direction> total = new LinkedList<Direction>();
         while(!path.isEmpty()){
             Node curr = path.remove(0);
             total.add(curr.self.subtract(prev.self, grid.length, grid[0].length));
             prev = curr;
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

        public Node(Point loc, Node parent, Point target) {
            this.self = loc;
            if (parent != null) {
                this.parent = parent.getPoint();
                this.gScore = parent.fScore() + 1;
            } else {
                this.parent = null;
                this.gScore = 1;
            }
            this.hScore = Math.abs(target.getX() - loc.getX()) + Math.abs(target.getY() - loc.getY());
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
        
        public Point getPoint() {
            return self;
        }
        /**
         * Returns the string representation of the Node
         * @return String representation of the node
         */
        @Override
        public String toString() {
            return self + " has parent of " + parent + " with gScore " + gScore + " with hScore " + hScore;
        }

        public boolean checkBetter(Node n) {
            if (n.gScore + 1 < gScore()) {
                gScore = n.fScore() + 1;
                parent = n.getPoint();
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
