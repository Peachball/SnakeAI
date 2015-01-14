
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

public class SnakeAI {
    private int[] target;
    private List<Node> openSet;
    private List<Node> closedSet;
    public static final int apple = -1;
    public static final int snakeHead = 1;
    public static SnakeAI placeholder = new SnakeAI(null,0);
    public SnakeAI(int[][] grid, int snakeLength){
        openSet = new LinkedList<Node>();
        closedSet = new LinkedList<Node>();
        int counter = 0;
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid.length; j++){
                if(grid[i][j] == 1)
                    openSet.add(new Node(i,j,null, 0));
                else if(grid[i][j] == -1)
                    target = new int[] {i,j};
            }
        }
        while(!openSet.isEmpty()){ //TODO: inefficient, fix LATERR
            int bestScore = 0;
            int index = -1;
            for(int i = 0; i < openSet.size(); i++){
                if(openSet.get(i).fScore() >= bestScore){
                    index = i;
                    bestScore = openSet.get(i).fScore();
                }
            }
            Node process = openSet.get(index);
            if(grid[nod])
        }
    }
    public int[] getTarget(){
        return target;
    }
    class Node{
        private Node parent;
        private int x;
        private int y;
        private int gScore;
        private int hScore;
        public Node(int x, int y, Node parent, int gScore){
            this.x = x;
            this.y=y;
            this.parent = parent;
            this.gScore = gScore;
            this.hScore = Math.abs(SnakeAI.placeholder.getTarget()[0] - x + SnakeAI.placeholder.getTarget()[1] - y);
        }
        public int fScore(){
            return gScore + hScore;
        }
        public int[] getCoord(){
            return new int[] {x,y};
        }
    }
}
