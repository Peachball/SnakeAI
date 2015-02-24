
/**
 * This class is here to be able to efficiently iterate snakes without needing to search through an entire grid.
 * A more detailed explanation can be found in the Grid class.
 * @author Benjamin
 */
public class SnakePart implements Tile{
    /**
     * child represents the coordinate of the point that comes after the current point.
     */
    private Point parent;
    /**
     * setChild sets the parent only if the current child is not set.
     * @param parent The parent to be set.
     */
    public SnakePart(Point parent){
        this.parent = parent;
    }
    public void setParent(Point parent){
        if(this.parent == null)
            this.parent = parent;
    }
    /**
     * getChild returns the coordinate of the child
     * @return child Coordinate.
     */
    public Point getParent(){
        return parent;
    }
}