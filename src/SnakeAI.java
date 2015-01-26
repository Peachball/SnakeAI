
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SnakeAI {

    private Point target;
    private Point start;
    private List<Node> openSet;
    private Map<Point, Node> closedSet;
    public static final int apple = -1;
    public static final int snakeHead = 1;
    public static SnakeAI placeholder = new SnakeAI(null, 0);
    private int[][] grid;

    public SnakeAI(int[][] grid, int snakeLength) {
        openSet = new ArrayList<Node>();
        closedSet = new HashMap<Point, Node>();
        this.grid = grid;
    }

    public static void binaryInsert(List<Node> list, Node thing) {
        list.add(binarySearch(list, thing.getPoint()), thing);
    }

    private static int binarySearch(List<Node> a, Point thing) {
        int low = 0;
        int high = a.size() - 1;
        int mid;
        while (low <= high) {
            mid = (low + high) / 2;
            if (a.get(mid).getPoint().equals(thing)) {
                high = mid - 1;
            } else if (a.get(mid).getPoint().equals(thing)) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    public Point getTarget() {
        return target;
    }

    private void makeNode(Point p, Node parent) {
        if (!closedSet.containsKey(p)) {
            int index = binarySearch(openSet, p);
            if (index == -1) {
                openSet.add(index, new Node(p, parent));
            } else {
                openSet.get(index).checkBetter(parent);
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
                if (grid[i][j] == 1) {
                    openSet.add(new Node(i, j, null));
                    start = new Point(i, j);
                } else if (grid[i][j] == -1) {
                    target = new Point(i, j);
                }
            }
        }
        while (!openSet.isEmpty() && !checkDone()) {
            addSurround(openSet.get(openSet.size() - 1));
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
            this.hScore = Math.abs(SnakeAI.placeholder.getTarget().getX() - loc.getX() + SnakeAI.placeholder.getTarget().getX() - loc.getY());
        }

        public Node(int x, int y, Node parent) {
            this.self = new Point(x, y);
            this.parent = parent.getPoint();
            this.gScore = parent.fScore() + 1;
            this.hScore = Math.abs(SnakeAI.placeholder.getTarget().getX() - x + SnakeAI.placeholder.getTarget().getX() - y);
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

        public boolean checkBetter(Node n) {
            if (n.gScore + 1 < gScore()) {
                gScore = n.fScore() + 1;
                parent = n.getPoint();
                return true;
            }
            return false;
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
    }
}
