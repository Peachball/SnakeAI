
/**
 *
 * @author Benjamin
 */
public class Point {

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
        public Point add(Direction d){
            switch(d){
                case RIGHT:
                    return new Point(this.x + 1, this.y);
                case LEFT:
                    return new Point(this.x - 1, this.y);
                case UP:
                    return new Point(this.x, this.y + 1);
                case DOWN:
                    return new Point(this.x, this.y - 1);
            }
            return null; //HOW DID YOU GET HERE. HOW.
        }
        public Point modDimentions(int x, int y){
            return new Point(SnakeAI.modNumber(this.x, x),SnakeAI.modNumber(this.y, y));
        }
        public static int getVal(int[][] grid, Point p){
            return grid[p.getX()][p.getY()];
        }
    }