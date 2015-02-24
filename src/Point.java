
/**
 *This class stores integer coordinates for the snake game.
 * @author Benjamin
 */
public class Point {

        public int x, y;

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
        @Override
        public boolean equals(Object otherPoint) {
            if (otherPoint instanceof Point) {
                return x == ((Point)otherPoint).getX() && y == ((Point)otherPoint).getY();
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
        public Direction subtract(Point p, int xLim, int yLim){
            if(Grid.modNumber(p.getX() - this.getX(), xLim) == 1)
                return Direction.RIGHT;
            if(Grid.modNumber(p.getX() - this.getX(), xLim) == xLim - 1)
                return Direction.LEFT;
            if(Grid.modNumber(p.getY() - this.getY(), yLim) == 1)
                return Direction.UP;
            if(Grid.modNumber(p.getY() - this.getY(), yLim) == yLim - 1)
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
    }